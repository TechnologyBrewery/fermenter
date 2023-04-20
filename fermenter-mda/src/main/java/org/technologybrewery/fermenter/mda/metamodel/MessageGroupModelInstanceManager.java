package org.technologybrewery.fermenter.mda.metamodel;

import org.technologybrewery.fermenter.mda.metamodel.element.MessageGroup;
import org.technologybrewery.fermenter.mda.metamodel.element.MessageGroupElement;

/**
 * Responsible for maintaining the list of message group model instances elements in the system.
 */
class MessageGroupModelInstanceManager extends AbstractMetamodelManager<MessageGroup> {

    private static ThreadLocal<MessageGroupModelInstanceManager> threadBoundInstance = ThreadLocal
            .withInitial(MessageGroupModelInstanceManager::new);

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static MessageGroupModelInstanceManager getInstance() {
        return threadBoundInstance.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        super.reset();
        threadBoundInstance.remove();
    }

    /**
     * Prevent instantiation of this singleton from outside this class.
     */
    private MessageGroupModelInstanceManager() {
        super();
    }

    @Override
    protected String getMetadataLocation() {
        return config.getMessageGroupsRelativePath();
    }

    @Override
    protected Class<MessageGroupElement> getMetamodelClass() {
        return MessageGroupElement.class;
    }

    @Override
    protected String getMetamodelDescription() {
        return MessageGroup.class.getSimpleName();
    }

}
