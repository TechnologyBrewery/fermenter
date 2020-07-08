package org.bitbucket.fermenter.mda.metamodel.element;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a constant that is defined within a specific {@link MessageGroupElement}.
 */
@JsonPropertyOrder({ "name", "text"})
public class MessageElement extends MetamodelElement implements Message {

    @JsonProperty(required = true)
    private String text;

    /**
     * Returns the message text.
     * 
     * @return message text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the message text.
     * 
     * @param text
     *            message text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        // nothing specific to validate that isn't handled at JSON load time already
    }

}
