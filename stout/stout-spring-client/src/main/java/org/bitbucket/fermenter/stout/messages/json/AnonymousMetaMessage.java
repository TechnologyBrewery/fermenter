package org.bitbucket.fermenter.stout.messages.json;

import org.bitbucket.fermenter.stout.messages.MetaMessage;

/**
 * Package protected generic {@link MetaMessage} to support deserialization of arbitrary messages.
 */
class AnonymousMetaMessage implements MetaMessage {

    private String name;
    private String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    

}
