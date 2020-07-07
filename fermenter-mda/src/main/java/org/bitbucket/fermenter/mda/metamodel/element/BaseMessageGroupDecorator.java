package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Provides baseline decorator functionality for {@link MessageGroup}.
 * 
 * The goal is to make it easier to apply the decorator pattern in various implementations of generators (e.g., Java,
 * Typescript, Dart) so that each concrete decorate does only has to decorate those aspects of the class that are
 * needed, not all the pass-through methods that each decorate requires by default.
 */
public class BaseMessageGroupDecorator implements MessageGroup {

    protected MessageGroup wrapped;

    /**
     * New decorator for {@link MessageGroup}.
     * 
     * @param messageGroupToDecorate
     *            instance to decorate
     */
    public BaseMessageGroupDecorator(MessageGroup messageGroupToDecorate) {
        MetamodelUtils.validateWrappedInstanceIsNonNull(getClass(), messageGroupToDecorate);
        wrapped = messageGroupToDecorate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return wrapped.getName();
    }

    /**
     * Returns the Capitalized name.
     * 
     * @return capitalized name
     */
    public String getCapitalizedName() {
        return StringUtils.capitalize(getName());
    }

    @Override
    public String getFileName() {
        return wrapped.getFileName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPackage() {
        return wrapped.getPackage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> getMessages() {
        List<Message> wrappedMessages = new ArrayList<>();
        for (Message message : wrapped.getMessages()) {
            Message wrappedMessage = new BaseMessageDecorator(message);
            wrappedMessages.add(wrappedMessage);
        }

        return wrappedMessages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        wrapped.validate();

    }

}
