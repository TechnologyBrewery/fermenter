package org.bitbucket.fermenter.mda.metamodel;

import org.bitbucket.fermenter.mda.metamodel.element.MessageGroup;
import org.bitbucket.fermenter.mda.metamodel.element.MessageGroupElement;

/**
 * Responsible for maintaining the list of message group model instances elements in the system.
 */
class MessageGroupModelInstanceManager extends AbstractMetamodelManager<MessageGroup> {

    private static final MessageGroupModelInstanceManager instance = new MessageGroupModelInstanceManager();

    /**
     * Returns the singleton instance of this class.
     * 
     * @return singleton
     */
    public static MessageGroupModelInstanceManager getInstance() {
        return instance;
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
