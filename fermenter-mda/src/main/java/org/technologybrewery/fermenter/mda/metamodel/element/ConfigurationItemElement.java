package org.technologybrewery.fermenter.mda.metamodel.element;

import org.apache.commons.lang3.StringUtils;
import org.technologybrewery.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a configuration item.
 */
@JsonPropertyOrder({ "key", "value" })
public class ConfigurationItemElement implements ConfigurationItem {

    protected static MessageTracker messageTracker = MessageTracker.getInstance();

    @JsonInclude(Include.NON_NULL)
    private String key;

    @JsonInclude(Include.NON_NULL)
    private String value;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return key;
    }

    /**
     * Sets the configuration item key.
     * 
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Sets the configuration item value.
     * 
     * @param key
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (StringUtils.isBlank(getKey())) {
            messageTracker.addErrorMessage("A key is required for each configuration item!");
        }

        if (StringUtils.isBlank(getValue())) {
            messageTracker.addErrorMessage("A value is required for each configuration item!");
        }
    }

}
