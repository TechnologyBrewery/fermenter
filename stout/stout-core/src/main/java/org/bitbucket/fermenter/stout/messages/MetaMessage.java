package org.bitbucket.fermenter.stout.messages;

/**
 * Met information about a message to allow the common definition of a message to live outside a message instance.
 */
public interface MetaMessage {

    String getText();

    String toString();

}
