package org.bitbucket.fermenter.stout.util;

import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.bitbucket.fermenter.stout.messages.json.MessageDeserializer;
import org.bitbucket.fermenter.stout.messages.json.MessageSerializer;
import org.bitbucket.fermenter.stout.messages.json.MessagesMixIn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Manages and customizes the Jackson {@link ObjectMapper} instance for this project, containing just the configuration
 * needed to handle messages.
 */
public final class ObjectMapperManager {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        configureObjectMapper();
    }

    private ObjectMapperManager() {
        // prevent instantiation
    }

    /**
     * Adds in any custom modules desired for this project's {@link ObjectMapper}.
     */
    private static void configureObjectMapper() {
        SimpleModule module = new SimpleModule();

        module.setMixInAnnotation(Messages.class, MessagesMixIn.class);
        module.addSerializer(Message.class, new MessageSerializer());
        module.addDeserializer(Message.class, new MessageDeserializer());

        module.addDeserializer(String.class, new TrimmingStringDeserializer(String.class));

        objectMapper.registerModule(module);
    }

    /**
     * Returns the thread-safe, singleton instance of {@link ObjectMapper} for this project.
     * 
     * @return object mapper
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}
