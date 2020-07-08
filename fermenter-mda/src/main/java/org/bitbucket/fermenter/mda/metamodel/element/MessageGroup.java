package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.List;

/**
 * Defines the contract for a messages that contains message formulas for returning information back to the user.
 */
public interface MessageGroup extends NamespacedMetamodel {

    /**
     * Returns the {@link Message} instances within this message group.
     * 
     * @return messages
     */
    List<Message> getMessages();

}