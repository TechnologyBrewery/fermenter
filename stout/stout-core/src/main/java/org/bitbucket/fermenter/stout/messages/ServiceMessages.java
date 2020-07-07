package org.bitbucket.fermenter.stout.messages;

/**
 * Meta-message information about service related items.
 */
public enum ServiceMessages implements MetaMessage {

    FIND_BY_EXAMPLE_REQUIRES_SORT("find.by.example.requires.sort",
            "When searching by example, a valid sort is required"),

    FIND_BY_EXAMPLE_REQUIRES_START_PAGE("find.by.example.requires.start.page",
            "When searching by example, a valid startPage is required"),

    FIND_BY_EXAMPLE_REQUIRES_COUNT("find.by.example.requires.count",
            "When searching by example, a valid count is required"),

    FIND_BY_EXAMPLE_COUNT_OVERRIDE("find.by.example.count.override",
            "When searching by example, the count has been overridden to use the base count of ${baseCount}"),

    PAGING_REQUIRES_START_PAGE("service.paged.requires.start.page",
            "When using a paged service, startPage must be a positive integer"),

    PAGING_REQUIRES_COUNT("service.paged.requires.count",
            "When using a paged service, count must be a positive integer"),

    BULK_OPERATION_FAILED("bulk.operation.failed", "The ${operation} operation failed for the following ids ${keyList}");

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
    ServiceMessages(String name, String text) {
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
    public static ServiceMessages fromTextString(String value) {
        ServiceMessages enumInstance;
        switch (value) {
        case "find.by.example.requires.sort":
            enumInstance = FIND_BY_EXAMPLE_REQUIRES_SORT;
            break;
        case "find.by.example.requires.start.page":
            enumInstance = FIND_BY_EXAMPLE_REQUIRES_START_PAGE;
            break;
        case "find.by.example.requires.count":
            enumInstance = FIND_BY_EXAMPLE_REQUIRES_COUNT;
            break;
        case "find.by.example.count.override":
            enumInstance = FIND_BY_EXAMPLE_COUNT_OVERRIDE;
            break;
        case "service.paged.requires.start.page":
            enumInstance = PAGING_REQUIRES_START_PAGE;
            break;
        case "service.paged.requires.count":
            enumInstance = PAGING_REQUIRES_COUNT;
            break;
        case "bulk.operation.failed":
            enumInstance = BULK_OPERATION_FAILED;
            break;
        default:
            enumInstance = null;
            break;
        }
        return enumInstance;
    }

}
