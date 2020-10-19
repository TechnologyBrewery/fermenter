package org.bitbucket.fermenter.stout.messages.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MetaMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;

/**
 * Serializes {@link Message} objects by marshalling all attributes of {@link Message} objects (i.e. key, severity,
 * inserts, and properties), as well as the appropriately populated summary and detail message content in order to
 * prevent clients, which may reside in different application tiers, from having to determine the actual message text
 * from the {@link Message} object attributes.
 */
public class MessageSerializer extends JsonSerializer<Message> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Message> handledType() {
        return Message.class;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(Message message, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        
        MetaMessage metaMessage = message.getMetaMessage();
        jgen.writeObjectField("name", metaMessage.toString());        
        
        jgen.writeStringField("severity", message.getSeverity() != null ? message.getSeverity().toString() : null);

        jgen.writeArrayFieldStart("inserts");
        Collection<Entry<String, String>> inserts = message.getAllInserts();
        for (Entry<String, String> insert : inserts) {
            jgen.writeStartObject();            
            String name = insert.getKey();
            String value = insert.getValue();
            jgen.writeStringField(name, value);            
            jgen.writeEndObject();
            
        }
        jgen.writeEndArray();
       
        jgen.writeObjectField("rawText", metaMessage.getText());    
        
        jgen.writeStringField("displayText", message.getDisplayText());
        jgen.writeEndObject();
    }

}
