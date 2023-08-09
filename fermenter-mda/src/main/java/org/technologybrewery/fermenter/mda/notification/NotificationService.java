package org.technologybrewery.fermenter.mda.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.technologybrewery.fermenter.mda.generator.GenerationException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Listens for the end of the Maven Session and then outputs any accumulated manual action notifications.
 */
@Named
@Singleton
public class NotificationService extends AbstractMavenLifecycleParticipant {

    public static final String JSON = "json";
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final String NOTIFICATION_DIR_NAME = "manual-action-notifications";
    static final String NOTIFICATION_DIRECTORY_PATH = "target/" + NOTIFICATION_DIR_NAME + "/";
    public static final String GROUP = "group://";

    private MavenSession session;

    private boolean hideManualActions = false;

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * New instance.  This is a CDI-managed bean, so please use CDI to get any reference unless testing.  In the later
     * case, a CDI test contest of this constructor may prove useful.
     *
     * @param session Maven session
     */
    @Inject
    public NotificationService(MavenSession session) {
        this.session = session;
    }

    /**
     * Called by Maven after the Maven Session ends.  It is leveraged to display any notifications to the console.
     *
     * @param session Maven session
     */
    @Override
    public void afterSessionEnd(MavenSession session) {
        this.session = session;
        String rawHideManualActions = session.getUserProperties().getProperty("fermenter.hide.manual.actions");
        if (rawHideManualActions != null) {
            this.hideManualActions = Boolean.parseBoolean(rawHideManualActions);
        }

        try {
            displayNotifications();
        } catch (IOException e) {
            throw new GenerationException("Could not emit manual action notifications! Check target directories for more details.", e);
        }
    }

    static void addNotificationToPassedMap(String targetFile, Notification notification, Map<String, Map<String,
        Notification>> notificationsByFilenameMap) {

        Map<String, Notification> notificationsForFile = notificationsByFilenameMap.computeIfAbsent(targetFile,
            k -> new ConcurrentHashMap<>());

        String notificationKey = notification.getKey();
        if (!notificationsForFile.containsKey(notificationKey)) {
            notificationsForFile.put(notificationKey, notification);
        } else {
            notificationsForFile.get(notificationKey).addItems(notification.getItems());
        }
    }

    /**
     * Writes any encountered notifications to a file for use later.  These are written to the path defined by
     * NOTIFICATION_DIRECTORY_PATH.
     */
    public void recordNotifications(MavenProject project) {
        int manualActionCount = 0;
        File projectParentFile = new File(project.getBasedir(), NOTIFICATION_DIRECTORY_PATH);
        Map<String, Map<String, Notification>> collectorNotifications = NotificationCollector.getNotifications();
        for (Map.Entry<String, Map<String, Notification>> entry : collectorNotifications.entrySet()) {
            String fileName = entry.getKey();
            Map<String, Notification> notificationsForFile = entry.getValue();
            int i = 0;
            for (Map.Entry<String, Notification> subMapEntry : notificationsForFile.entrySet()) {
                File notificationParentFile = getNotificationParentFile(projectParentFile, subMapEntry.getValue());
                File outputFile = new File(notificationParentFile, FilenameUtils.getName(fileName + "-" + UUID.randomUUID().toString() + "." + JSON));
                try {
                    Notification notification = subMapEntry.getValue();
                    FileUtils.forceMkdir(notificationParentFile);
                    FileUtils.write(outputFile, getNotificationJson(notification), Charset.defaultCharset());
                    if (notification instanceof VelocityNotification) {
                        VelocityNotification velocityNotification = (VelocityNotification) notification;
                        velocityNotification.writeExternalVelocityContextProperties(project.getBasedir());
                    }
                    manualActionCount++;
                } catch (IOException e) {
                    throw new GenerationException("Could not write manual action notification to disk!", e);
                }
            }

            if (manualActionCount > 0) {
                logger.warn("{} manual action notification encountered - details written to {}", manualActionCount,
                    projectParentFile.getAbsoluteFile());
            }

        }

        NotificationCollector.cleanup();
    }

    /**
     * Grouped notifications will be placed in a sub-folder of the group name.
     *
     * @param projectParentFile project parent file
     * @param notification      notifcation
     * @return appropriate parent file location
     */
    private File getNotificationParentFile(File projectParentFile, Notification notification) {
        File result;
        String group = notification.getGroup();
        if (StringUtils.isBlank(group)) {
            result = projectParentFile;
        } else {
            result = new File(projectParentFile, FilenameUtils.getName(group));
        }

        return result;
    }

    private void displayNotifications() throws IOException {
        if (this.hideManualActions) {
            logger.debug("Hiding manual actions");

        } else if (logger.isWarnEnabled()) {
            emitNotifications();
        }
    }

    private void emitNotifications() throws IOException {
        // Get all notifications, then display them by file being modified
        Map<String, Set<Notification>> notificationMap = findNotificationForDisplay();
        GroupNotificationOutput notificationOutput = groupNotifications(notificationMap);

        if (MapUtils.isNotEmpty(notificationOutput.groupedNotificationMap)) {
            int i = 1;
            logger.warn("Manual action steps were detected by fermenter-mda in {} module(s)", notificationMap.size());
            logger.warn("");
            for (Map.Entry<String, List<Notification>> entry : notificationOutput.groupedNotificationMap.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith(GROUP)) {
                    denoteNewManualAction(i);
                    String groupName = key.replace(GROUP, "");
                    outputGroupNotification(entry, notificationOutput.groupedNotificationVelocityContextMap.get(groupName), groupName);

                } else {
                    for (Notification notification : entry.getValue()) {
                        denoteNewManualAction(i);
                        logger.warn(notification.getNotificationAsString());
                    }
                }

                i++;
            }

            logger.warn("To disable these messages, please use -Dfermenter.hide.manual.actions=true");

        }
    }

    private static void denoteNewManualAction(int i) {
        logger.warn("------------------------------------------------------------------------");
        logger.warn("Manual Action #{}", i);
        logger.warn("------------------------------------------------------------------------");
    }

    private Map<String, Set<Notification>> findNotificationForDisplay() {
        Map<String, Set<Notification>> resultMap = new HashMap<>();
        List<MavenProject> projects = this.session.getAllProjects();
        for (MavenProject project : projects) {
            File projectBaseDirectory = project.getBasedir();
            File projectNotificationDirectory = new File(projectBaseDirectory, NOTIFICATION_DIRECTORY_PATH);
            if (projectNotificationDirectory.exists()) {
                File[] fileArray = projectNotificationDirectory.listFiles();
                if (fileArray != null) {
                    IOFileFilter txtFileFilter = FileFilterUtils.suffixFileFilter(JSON);
                    Collection<File> files = FileUtils.listFiles(projectNotificationDirectory,
                        txtFileFilter, FileFilterUtils.directoryFileFilter());
                    for (File notificationJsonFile : files) {
                        try {
                            SimpleNotification notification = objectMapper.readValue(notificationJsonFile, SimpleNotification.class);
                            Set<Notification> notifications = resultMap.computeIfAbsent(notification.getKey(), l -> new HashSet<>());
                            notification.setFile(notificationJsonFile);
                            notifications.add(notification);

                        } catch (IOException e) {
                            throw new GenerationException("Could not read notification!", e);
                        }
                    }

                }
            }
        }

        return resultMap;

    }

    private GroupNotificationOutput groupNotifications(Map<String, Set<Notification>> notificationMap) {
        GroupNotificationOutput groupedNotifications = new GroupNotificationOutput();
        for (Map.Entry<String, Set<Notification>> entry : notificationMap.entrySet()) {
            for (Notification notification : entry.getValue()) {
                String key = notification.getKey();
                String group = notification.getGroup();
                if (notification.hasGroup()) {
                    key = GROUP + group;
                    loadGroupVelocityContextProperties(notification, groupedNotifications);

                }
                List<Notification> notificationList = groupedNotifications.groupedNotificationMap.computeIfAbsent(key, l -> new ArrayList<>());
                notificationList.add(notification);
            }
        }

        return groupedNotifications;
    }

    private void loadGroupVelocityContextProperties(Notification notification, GroupNotificationOutput groupedNotifications) {
        String sourceGroupPropertiesName = FilenameUtils.getName(String.format("group-%s.properties", notification.getGroup()));
        File sourceGroupPropertiesFile = new File(notification.getFile().getParentFile(), sourceGroupPropertiesName);
        if (sourceGroupPropertiesFile.exists()) {
            Properties targetGroupProperties =
                groupedNotifications.groupedNotificationVelocityContextMap.computeIfAbsent(notification.getGroup(), p -> new Properties());

            NotificationUtils.mergeExistingAndNewProperties(sourceGroupPropertiesFile, targetGroupProperties);
        }
    }

    private static void outputGroupNotification(Map.Entry<String, List<Notification>> entry, Properties groupProperties, String groupName) throws IOException {
        String templateName = "templates/notifications/group-" + groupName + ".vm";
        Set<String> groupItems = new HashSet<>();
        for (Notification notification : entry.getValue()) {
            groupItems.add(notification.getNotificationAsString());
        }

        VelocityNotification groupNotification = new VelocityNotification(groupName, groupName, groupItems, templateName);
        if (groupProperties != null) {
            for (String groupPropertyKey : groupProperties.stringPropertyNames()) {
                String value = groupProperties.getProperty(groupPropertyKey);
                groupNotification.addToVelocityContext(groupPropertyKey, value);
            }
        }

        logger.warn(groupNotification.getNotificationAsString());
    }

    protected static String getNotificationJson(Notification notification) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(notification);

        } catch (JsonProcessingException e) {
            throw new GenerationException("Could not transform notification to json!", e);
        }
    }

    /**
     * A tuple to make it a bit easier to bundle group notifications with group velocity context properties.
     */
    private static class GroupNotificationOutput {
        Map<String, List<Notification>> groupedNotificationMap = new HashMap<>();
        Map<String, Properties> groupedNotificationVelocityContextMap = new HashMap<>();
    }

}
