package org.bitbucket.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.bitbucket.fermenter.mda.metamodel.element.BaseMessageGroupDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Message;
import org.bitbucket.fermenter.mda.metamodel.element.MessageGroup;

/**
 * Decorates a message group for easier Java rendering.
 */
public class JavaMessageGroup extends BaseMessageGroupDecorator implements MessageGroup, JavaPackagedElement {

    private List<Message> decoratedMessages;

    /**
     * {@inheritDoc}
     */
    public JavaMessageGroup(MessageGroup messageGroupToDecorate) {
        super(messageGroupToDecorate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> getMessages() {
        if (decoratedMessages == null) {
            List<Message> messages = wrapped.getMessages();
            if (CollectionUtils.isEmpty(messages)) {
                decoratedMessages = Collections.emptyList();

            } else {
                decoratedMessages = new ArrayList<>();
                for (Message message : messages) {
                    decoratedMessages.add(new JavaMessage(message));

                }

            }
        }

        return decoratedMessages;
    }

}
