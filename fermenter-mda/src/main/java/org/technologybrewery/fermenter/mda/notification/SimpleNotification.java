package org.technologybrewery.fermenter.mda.notification;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Simple implementation of {@link AbstractNotification} to be used as the target for when a notification is read
 * from the file system.
 */
public class SimpleNotification extends AbstractNotification {

    private String notificationAsString;

    public SimpleNotification() {
        super(null, null);
    }

    @Override
    public String getNotificationAsString() {
        return notificationAsString;
    }

    public void setNotificationAsString(String notificationAsString) {
        this.notificationAsString = notificationAsString;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(19, 23);
        if (hasGroup()) {
            builder.append(group);
        }
        builder.append(key);

        return builder.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        Notification that = (Notification) obj;
        EqualsBuilder builder = new EqualsBuilder();
        if (hasGroup()) {
            builder.append(this.group, that.getGroup());
        }
        builder.append(this.key, that.getKey());

        return builder.isEquals();
    }
}
