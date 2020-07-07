package org.bitbucket.fermenter.stout.messages;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A message is an object that consists of a name (used to somehow identify the message's text) and a severity (e.g.
 * 'Error', 'Info'). Messages can have properties in the text to make them more meaningful, but cannot be associated
 * with a specific field without extending the class.
 */
public class Message {

    private static final Logger logger = LoggerFactory.getLogger(Message.class);

    private MetaMessage metaMessage;
    private Severity severity;
    private Map<String, String> inserts = new HashMap<>();

    public Message(MetaMessage metaMessage) {
        this.metaMessage = metaMessage;
    }

    public Message(MetaMessage metaMessage, Severity severity) {
        this.metaMessage = metaMessage;
        this.severity = severity;
    }

    public Message(MetaMessage metaMessage, Severity severity, Map<String, Object> inserts) {
        this.metaMessage = metaMessage;
        this.severity = severity;
        if (inserts != null) {
            for (Map.Entry<String, Object> insert : inserts.entrySet()) {
                addInsert(insert.getKey(), insert.getValue());
            }
        }
    }

    /**
     * Get the meta information for this message as represented by an instance of {@link MetaMessage}.
     *
     * @return The message's meta information - the value used to identify the text for this message
     */
    public MetaMessage getMetaMessage() {
        return metaMessage;
    }

    /**
     * Get the severity for this message.
     *
     * @return The {@link Severity} enumeration value for this message
     * @see Severity
     */
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Get the *formatted* insert for the passed insert name
     *
     * @return formatted insert
     */
    public String getInsert(String insertName) {
        return inserts.get(insertName);
    }

    /**
     * Returns all inserts. This can be useful when you have many similar messages and want to differentiate them based
     * on which objects / values they are representing.
     * 
     * @return all inserts or an empty collection if none exist
     */
    public Collection<Entry<String, String>> getAllInserts() {
        return inserts.entrySet();
    }

    /**
     * Update the value of the message's severity.
     *
     * @param severity
     *            The severity (enumerated type) value
     * @see Severity
     */
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    /**
     * Adds a insert value for a given insert name. For instance name: 'today', value: new Date().
     * 
     * @param insertName
     *            the named parameter in a message's text (e.g., 'Something went wrong on ${today}')
     * @param insertValue
     *            the value to insert
     */
    public void addInsert(String insertName, Object insertValue) {
        if (insertName == null) {
            // We could fail hard here and throw an exception, but that seems disproportionate for inserts in a message:
            logger.error("Adding an insert without a name means it cannot be retrieved!");
        }

        if (insertValue != null) {
            this.inserts.put(insertName, insertValue.toString());
        }

    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, true);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, true);
    }

    /**
     * Return a more friendly representation of the message.
     * 
     * @return The string representation of this message
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Returns a "displayable" version of the message with inserts formatted and included, where appropriate.
     */
    public String getDisplayText() {
        String displayText = metaMessage.getText();
        for (String insertName : inserts.keySet()) {
            String searchString = "${" + insertName + "}";
            if (displayText.contains(searchString)) {
                String formattedInsert = getInsert(insertName);
                displayText = StringUtils.replace(displayText, searchString, formattedInsert);

            } else {
                logger.debug("Trying no insert value for {} exists in MetaMessage {}!", searchString, metaMessage);

            }
        }

        return displayText;

    }

    // TODO: allow formats to also be passed for a given insert name - until then,
    // we'll go with some defaults

}
