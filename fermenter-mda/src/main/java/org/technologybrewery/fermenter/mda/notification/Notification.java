package org.technologybrewery.fermenter.mda.notification;

import java.util.Set;

/**
 * A notification of a required action to present to the user during project generation.
 */
public interface Notification {

    /**
     * Returns the unique key representing the type of action indicated by this notification.
     *
     * @return the notification key
     */
    String getKey();

    /**
     * Returns a grouping that this notification should be displayed in conjunction with.
     *
     * @return group
     */
    String getGroup();

    /**
     * Determines if a group is set for this notification.
     *
     * @return true if set, false if not
     */
    boolean hasGroup();

    /**
     * Returns the list of action items to request from the user.
     *
     * @return the actions required
     */
    Set<String> getItems();

    /**
     * Add multiple action items to the notification.
     *
     * @param items tne set of action items to add
     */
    void addItems(Set<String> items);

    /**
     * Returns the notification as string that can be output to the console.
     *
     * @return notification content
     */
    String getNotificationAsString();

}
