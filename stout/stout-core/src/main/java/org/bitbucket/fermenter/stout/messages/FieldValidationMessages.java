package org.bitbucket.fermenter.stout.messages;

/**
 * Meta-message information about field validation related items.
 */
public enum FieldValidationMessages implements MetaMessage {


    INVALID_FIELD("invalid.field", "Field ${fieldName} is invalid: ${constraintViolation}"),
    
    INVALID_SCALE("invalid.scale", "The ${fieldName} value must contain at most ${scale} numbers after the decimal"),

    MUST_BE_GREATER_THAN("value.must.be.gt.min", "The ${fieldName} value must be greater than or equal to ${minValue}"),

    MUST_BE_LESS_THAN("value.must.be.lt.max", "The ${fieldName} value must be less than or equal to ${maxValue}"),

    NULL_NOT_ALLOWED("null.not.allowed", "${fieldName} is a required field"),

    MUST_BE_SHORTER_THAN("value.must.be.shorter.than",
            "The ${fieldName} value cannot be more than ${maxCharacters} characters long"),

    MUST_BE_LONGER_THAN("value.must.be.longer.than",
            "The ${fieldName} value must be at least ${maxCharacters} characters long"),

    INVALID_FORMAT("invalid.format", "The value '${fieldValue}' does not match the format for field ${fieldName}");

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
    FieldValidationMessages(String name, String text) {
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
    public static FieldValidationMessages fromTextString(String value) {
        FieldValidationMessages enumInstance;
        switch (value) {
        case "invalid.field":
            enumInstance = INVALID_FIELD;
            break;        
        case "invalid.scale":
            enumInstance = INVALID_SCALE;
            break;
        case "value.must.be.gt.min":
            enumInstance = MUST_BE_GREATER_THAN;
            break;
        case "value.must.be.lt.max":
            enumInstance = MUST_BE_LESS_THAN;
            break;
        case "null.not.allowed":
            enumInstance = NULL_NOT_ALLOWED;
            break;
        case "value.must.be.shorter.than":
            enumInstance = MUST_BE_SHORTER_THAN;
            break;
        case "value.must.be.longer.than":
            enumInstance = MUST_BE_LONGER_THAN;
            break;
        case "invalid.format":
            enumInstance = INVALID_FORMAT;
            break;
        default:
            enumInstance = null;
            break;
        }
        return enumInstance;
    }

}
