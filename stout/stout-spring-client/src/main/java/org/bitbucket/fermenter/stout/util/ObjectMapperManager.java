package org.bitbucket.fermenter.stout.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.stout.messages.DefaultMessages;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.bitbucket.fermenter.stout.messages.json.MessageDeserializer;
import org.bitbucket.fermenter.stout.messages.json.MessageSerializer;
import org.bitbucket.fermenter.stout.messages.json.MessagesMixIn;
import org.bitbucket.fermenter.stout.page.json.SortDeserializer;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
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

        module.addDeserializer(Sort.class, new SortDeserializer());
        
        module.addAbstractTypeMapping(Messages.class, DefaultMessages.class);
        module.setMixInAnnotation(Messages.class, MessagesMixIn.class);
        module.setMixInAnnotation(DefaultMessages.class, MessagesMixIn.class);
        module.addSerializer(Message.class, new MessageSerializer(ObjectMapperManager.class));
        module.addDeserializer(Message.class, new MessageDeserializer());

        module.addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            private static final long serialVersionUID = 8721520299501142938L;

            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return StringUtils.trim(p.getValueAsString());
            }
        });

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
