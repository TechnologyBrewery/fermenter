package org.technologybrewery.fermenter.mda.notification;

import org.apache.commons.collections4.MapUtils;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.MavenSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Listens for the end of the Maven Session and then outputs any accumulated manual action notifications.
 */
@Named
@Singleton
public class NotificationService extends AbstractMavenLifecycleParticipant {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final String NOTIFICATIONS = "notifications";

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

        displayNotifications();
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
     * Merges any notifications from the current plugin execution into the session for storage across plugin invocations.
     */
    public synchronized void mergeNotificationsIntoSessionForCrossProjectStorage() {
        Map<String, Map<String, Notification>> collectorNotifications = NotificationCollector.getNotifications();
        Map<String, Map<String, Notification>> sessionNotifications = getNotificationsFromSession();

        synchronized (this) {
            if (sessionNotifications == null) {
                sessionNotifications = collectorNotifications;
                Properties userProperties = session.getUserProperties();
                userProperties.put(NOTIFICATIONS, sessionNotifications);
            } else {
                for (Map.Entry<String, Map<String, Notification>> entry : collectorNotifications.entrySet()) {
                    String fileName = entry.getKey();
                    Map<String, Notification> notificationsForFile = entry.getValue();
                    for (Map.Entry<String, Notification> subMapEntry : notificationsForFile.entrySet()) {
                        addNotificationToPassedMap(fileName, subMapEntry.getValue(), sessionNotifications);
                    }
                }
            }
        }
    }

    private Map<String, Map<String, Notification>> getNotificationsFromSession() {
        Properties userProperties = session.getUserProperties();
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Notification>> stringMapMap = (Map<String, Map<String, Notification>>) userProperties.get(NOTIFICATIONS);
        return stringMapMap;
    }

    void displayNotifications() {
        if (this.hideManualActions) {
            logger.debug("Hiding manual actions");

        } else if (logger.isWarnEnabled()) {
            // Get all notifications, then display them by file being modified
            Map<String, Map<String, Notification>> sessionNotifications = getNotificationsFromSession();

            if (MapUtils.isNotEmpty(sessionNotifications)) {
                int i = 1;
                logger.warn("Manual action steps were detected by fermenter-mda in {} place(s)", sessionNotifications.size());
                logger.warn("");
                for (Map.Entry<String, Map<String, Notification>> entry : sessionNotifications.entrySet()) {
                    logger.debug("Notifications for: {}", entry.getKey());

                    for (Object notification : entry.getValue().values()) {
                        logger.warn("------------------------------------------------------------------------");
                        logger.warn("Manual Action #{}", i++);
                        logger.warn("------------------------------------------------------------------------");
                        logger.warn(notification.toString());
                    }
                }

                logger.warn("To disable these messages, please use -Dfermenter.hide.manual.actions=true");

            }
        }
    }

}
