package org.bitbucket.fermenter.stout.messages;

/**
 * Meta-message information about core framework related items.
 */
public enum CoreMessages implements MetaMessage {

    INVALID_ONE_TO_ONE_RELATION("invalid.one-to-one.relation",
            "Invalid 1-1 relationship for field ${fieldName} - more than one child object exists in the relation"),
    
    INVALID_REFERENCE("invalid.reference",
            "A non-existent object was referenced in a M-1 relationship for reference ${referenceName}"),

    INVALID_ENTITY_KEY("invalid.entity.key", "An entity with key ${keyName} could not be found"),

    OPLOCK_ERROR("oplock.error", "Another user has modified the data you are trying to save"),

    UNKNOWN_EXCEPTION_OCCURRED("unknown.exception.occurred", "An unknown exception occurred"),

    UNRECOVERABLE_EXCEPTION_OCCURRED("unrecoverable.exception.occurred", "An unrecoverable exception occurred"),

    RECOVERABLE_EXCEPTION_OCCURRED("recoverable.exception.occurred", "A recoverable exception occurred"),

    DATABASE_EXCEPTION_OCCURRED("database.exception.occurred", "An exception occurred while persisting");

    private String name;
    private String text;

    /**
     * Creates new instance of a message.
     * 
     * @param name
     *            the raw name for this message (naturally formatted)
     * @param text
     *            the raw message (with inserts, if desired) to set for this message
     */
    CoreMessages(String name, String text) {
        this.name = name;
        this.text = text;
    }

    /**
     * Gets the underlying raw text associated with this SampleMessages enum instance.
     * 
     * @return underlying raw text associated with this SampleMessages enum instance.
     */
    public String getText() {
        return text;
    }

    /**
     * {@inheritDoc)
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Retrieves the name of the enumerated value whose associated underlying value is equal to the input value.
     * 
     * @param value
     *            The search value that should match an enum instance's associated underlying value
     * @return The name of the matching enum instance
     */
    public static CoreMessages fromTextString(String value) {
        CoreMessages enumInstance;
        switch (value) {
        case "invalid.one-to-one.relation":
            enumInstance = INVALID_ONE_TO_ONE_RELATION;
            break;
        case "invalid.reference":
            enumInstance = INVALID_REFERENCE;
            break;
        case "invalid.entity.key":
            enumInstance = INVALID_ENTITY_KEY;
            break;
        case "oplock.error":
            enumInstance = OPLOCK_ERROR;
            break;
        case "unknown.exception.occurred":
            enumInstance = UNKNOWN_EXCEPTION_OCCURRED;
            break;
        case "unrecoverable.exception.occurred":
            enumInstance = UNRECOVERABLE_EXCEPTION_OCCURRED;
            break;
        case "recoverable.exception.occurred":
            enumInstance = RECOVERABLE_EXCEPTION_OCCURRED;
            break;
        case "database.exception.occurred":
            enumInstance = DATABASE_EXCEPTION_OCCURRED;
            break;   
        default:
            enumInstance = null;
            break;
        }
        return enumInstance;
    }
    
}
