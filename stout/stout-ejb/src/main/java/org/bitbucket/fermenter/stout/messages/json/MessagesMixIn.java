package org.bitbucket.fermenter.stout.messages.json;

import java.util.Collection;

import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Defines the json contract for {@link Messages}.
 */
public abstract class MessagesMixIn {

    @JsonProperty("messages")
    abstract Collection<Message> getAllMessages();

    @JsonProperty("messages")
    abstract void setAllMessages(Collection<Message> messages);

    @JsonIgnore
    abstract int getErrorMessageCount();

    @JsonIgnore
    abstract int getErrorMessageCount(String property);

    @JsonIgnore
    abstract Collection<Message> getErrorMessages();

    @JsonIgnore
    abstract Collection<Message> getErrorMessages(String property);

    @JsonIgnore
    abstract int getInformationalMessageCount();

    @JsonIgnore
    abstract int getInformationalMessageCount(String property);

    @JsonIgnore
    abstract Collection<Message> getInformationalMessages();

    @JsonIgnore
    abstract Collection<Message> getInformationalMessages(String property);

}
