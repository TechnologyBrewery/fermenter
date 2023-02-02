package org.bitbucket.fermenter.mda.util;

/**
 * An important message to be replayed to the user after the build.
 */
public class PriorityMessage {
    private String message;
    private String filePath;
    private static final String EMPTY_LINE = "\n";

    /**
     * Default constructor for {@link PriorityMessage} with no arguments needed for serialization/deserialization.
     */
    public PriorityMessage() { this(null, null); }

    /**
     * Custom constructor for {@link PriorityMessage} that takes the message as an argument
     * @param message
     *          {@link String} message
     */
    public PriorityMessage(String message) { this(message, null); }

    /**
     * Custom constructor for {@link PriorityMessage} that takes the message and file path as arguments
     * @param message
     *          {@link String} message
     * @param filePath
     *          {@link String} associated file path
     */
    public PriorityMessage(String message, String filePath) {
        this.message = message;
        this.filePath = filePath;
    }

    /**
     * Creates a string representation of the {@link PriorityMessage} including the optional metadata when applicable.
     * @return
     *          {@link String} representation of the message.
     */
    @Override
    public String toString() {
        final StringBuilder finalMessage = new StringBuilder();

        //adds the optional file path to the message if there is one
        if (filePath != null && !filePath.isBlank()) {
            finalMessage.append(filePath + ":" + EMPTY_LINE);
        }

        finalMessage.append(this.message);
        finalMessage.append(EMPTY_LINE);

        return finalMessage.toString();
    }

    /**
     * Sets the message to be replayed to the user.
     * @param message
     *          {@link String} message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the message to be replayed to the user.
     * @return
     *          {@link String} message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the optional file path associated with the message.
     * @param
     *         {@link String} filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns the optional file path associated with the message.
     * @return
     *          {@link String} filepath
     */
    public String getFilePath() {
        return filePath;
    }
}
