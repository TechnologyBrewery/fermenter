package org.technologybrewery.fermenter.mda.notification;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.technologybrewery.fermenter.mda.generator.GenerationException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Listens for the end of the Maven Session and then outputs any accumulated manual action notifications.
 */
@Named
@Singleton
public class NotificationService extends AbstractMavenLifecycleParticipant {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final String NOTIFICATION_DIRECTORY_PATH = "target/manual-action-notifications/";

    private MavenSession session;

    private boolean hideManualActions = false;

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
    public void recordNotifications() {
        int manualActionCount = 0;
        File projectParentFile = new File(NOTIFICATION_DIRECTORY_PATH);
        Map<String, Map<String, Notification>> collectorNotifications = NotificationCollector.getNotifications();
        for (Map.Entry<String, Map<String, Notification>> entry : collectorNotifications.entrySet()) {
            String fileName = entry.getKey();
            Map<String, Notification> notificationsForFile = entry.getValue();
            int i = 0;
            for (Map.Entry<String, Notification> subMapEntry : notificationsForFile.entrySet()) {
                File outputFile = new File(projectParentFile, FilenameUtils.getName(fileName + "-" + i++ + ".txt"));
                try {
                    FileUtils.forceMkdir(projectParentFile);
                    FileUtils.write(outputFile, subMapEntry.getValue().getNotificationAsString(), Charset.defaultCharset());
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

    private void displayNotifications() throws IOException {
        if (this.hideManualActions) {
            logger.debug("Hiding manual actions");

        } else if (logger.isWarnEnabled()) {
            // Get all notifications, then display them by file being modified
            Map<String, List<File>> notificationMap = findNotificationForDisplay();

            if (MapUtils.isNotEmpty(notificationMap)) {
                int i = 1;
                logger.warn("Manual action steps were detected by fermenter-mda in {} module(s)", notificationMap.size());
                logger.warn("");
                for (Map.Entry<String, List<File>> entry : notificationMap.entrySet()) {
                    logger.debug("Notifications for artifactId: {}", entry.getKey());

                    for (File notification : entry.getValue()) {
                        logger.warn("------------------------------------------------------------------------");
                        logger.warn("Manual Action #{}", i++);
                        logger.warn("------------------------------------------------------------------------");
                        logger.warn(FileUtils.readFileToString(notification, Charset.defaultCharset()));
                    }
                }

                logger.warn("To disable these messages, please use -Dfermenter.hide.manual.actions=true");

            }
        }
    }

    private Map<String, List<File>> findNotificationForDisplay() {
        Map<String, List<File>> resultMap = new HashMap<>();
        List<MavenProject> projects = this.session.getAllProjects();
        for (MavenProject project : projects) {
            File projectBaseDirectory = project.getBasedir();
            File projectNotificationDirectory = new File(projectBaseDirectory, NOTIFICATION_DIRECTORY_PATH);
            if (projectNotificationDirectory.exists()) {
                File[] fileArray = projectNotificationDirectory.listFiles();
                if (fileArray != null) {
                    resultMap.put(project.getArtifactId(), Arrays.stream(fileArray).collect(Collectors.toList()));
                }
            }
        }

        return resultMap;

    }

}
