package org.technologybrewery.fermenter.mda.notification;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * Provides common implementation of basic Notification.
 */
public abstract class AbstractNotification implements Notification {

    protected String key;

    protected String group;

    protected Set<String> items;

    /**
     * New instance.
     *
     * @param key   notification identifier
     * @param items set of strings to use in the notification
     */
    protected AbstractNotification(String key, Set<String> items) {
        this.key = key;
        this.items = items;
    }

    protected AbstractNotification(String key, String group, Set<String> items) {
        this(key, items);
        this.group = group;
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
    public String getGroup() {
        return group;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasGroup() {
        return StringUtils.isNotBlank(group);
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
