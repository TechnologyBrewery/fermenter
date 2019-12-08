package org.bitbucket.fermenter.stout.messages;

/**
 * Provides a reusable way to access message key names.
 *
 */
public enum CommonSearchMessageKey {

    NO_RESULTS_FOUND("no.results.found"), SEARCH_LIMIT_REACHED("search.limit.reached");

    private String keyName;

    /**
     * Creates new instance of this message.
     * 
     * @param value
     *            the key name for this message
     */
    CommonSearchMessageKey(String value) {
        this.keyName = value;
    }

    /**
     * Gets the underlying key name associated with this MessageKey enum
     * instance.
     * 
     * @return underlying key name associated with this MessageKey enum
     *         instance.
     */
    public String getKeyName() {
        return this.keyName;
    }

}
