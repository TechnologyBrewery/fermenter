package org.technologybrewery.fermenter.mda.notification;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An accessible class to make it easy to add notification from anywhere within the fermenter-mda classpath.
 * <p>
 * Theis collector support multi-threading and with notifications gathered and stored automatically across
 * multi-module builds.
 */
public final class NotificationCollector {
    private static final ThreadLocal<Map<String, Map<String, Notification>>> NOTIFICATIONS = ThreadLocal.withInitial(ConcurrentHashMap::new);

    private NotificationCollector() {
        // prevent private instationation of all static class
    }

    /**
     * Resets the notifications.  This can be useful between fermenter-mda-plugin invocations.
     */
    public static void cleanup() {
        NOTIFICATIONS.remove();
    }

    /**
     * Returns all notifications within the collector.
     *
     * @return notifications
     */
    public static Map<String, Map<String, Notification>> getNotifications() {
        return NOTIFICATIONS.get();
    }

    /**
     * Adds a notification to the collector.
     *
     * @param targetFile   file in which the notification is grouped
     * @param notification the notification to add
     */
    public static void addNotification(String targetFile, Notification notification) {
        Map<String, Map<String, Notification>> notificationsByFilenameMap = NotificationCollector.getNotifications();
        NotificationService.addNotificationToPassedMap(targetFile, notification, notificationsByFilenameMap);

    }

}
