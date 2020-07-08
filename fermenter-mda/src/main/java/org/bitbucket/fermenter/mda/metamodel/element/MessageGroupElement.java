package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a group of messages.
 */
@JsonPropertyOrder({ "package", "name", "messages"})
public class MessageGroupElement extends NamespacedMetamodelElement implements MessageGroup {

    @JsonProperty(required = true)
    protected List<Message> messages = new ArrayList<>();

    /**
     * Return the list of messages for this group.
     * 
     * @return constants
     */
    @Override
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Adds a message to this group.
     * 
     * @param message
     *            the message to add
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSchemaFileName() {
        return "fermenter-2-message-group-schema.json";
    }

}
