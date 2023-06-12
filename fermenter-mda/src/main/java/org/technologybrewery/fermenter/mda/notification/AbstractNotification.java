package org.technologybrewery.fermenter.mda.notification;

import java.util.Set;

/**
 * Provides common implementation of basic Notification.
 */
public abstract class AbstractNotification implements Notification {

    protected String key;

    protected Set<String> items;

    /**
     * New instance.
     *
     * @param key   notification identifier
     * @param items set of strings to use in the notification
     */
    public AbstractNotification(String key, Set<String> items) {
        this.key = key;
        this.items = items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getItems() {
        return items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addItems(Set<String> newItems) {
        items.addAll(newItems);
    }

}
