package org.bitbucket.fermenter.mda.metamodel.element;

/**
 * Contract for a user-facing message defined within a {@link MessageGroup}.
 */
public interface Message {

    /**
     * The name of the message.
     * 
     * @return message name
     */
    public String getName();

    /**
     * The text of the message.
     * 
     * @return Value of the text
     */
    public String getText();

}