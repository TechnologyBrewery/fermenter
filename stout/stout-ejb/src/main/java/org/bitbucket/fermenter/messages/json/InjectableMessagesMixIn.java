package org.bitbucket.fermenter.messages.json;

import javassist.util.proxy.MethodHandler;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Utilized to instruct Jackson on which properties of {@link InjectableMessages} objects should be ignored during JSON
 * serialization. This class needs all of the mixin annotations specified by {@link MessagesMixIn} in addition to
 * several annotations that exclude the proxy related properties of {@link InjectableMessages}, as
 * {@link InjectableMessages} is serialized as a CDI request scoped CDI bean.
 */
public abstract class InjectableMessagesMixIn extends MessagesMixIn {

    @JsonIgnore
    abstract MethodHandler getHandler();

    @JsonIgnore
    abstract Class<?> getTargetClass();

    @JsonIgnore
    abstract Object getTargetInstance();

    @JsonIgnore
    abstract String getTargetType();
}
