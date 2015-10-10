package org.bitbucket.fermenter.messages.json;

import java.io.IOException;
import java.util.Collection;

import org.bitbucket.fermenter.messages.Message;
import org.bitbucket.fermenter.messages.MessageUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * Serializes {@link Message} objects by marshalling all attributes of {@link Message} objects (i.e. key, severity,
 * inserts, and properties), as well as the appropriately populated summary and detail message content in order to
 * prevent clients, which may reside in different application tiers, from having to determine the actual message text
 * from the {@link Message} object attributes.
 */
public class MessageSerializer extends JsonSerializer<Message> {

	private Class<?> messageResourceBundleClazz;

	public MessageSerializer(Class<?> messageResourceBundleClazz) {
		this.messageResourceBundleClazz = messageResourceBundleClazz;
	}

	@Override
	public Class<Message> handledType() {
		return Message.class;
	}

	@Override
	public void serialize(Message value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
			JsonGenerationException {
		jgen.writeStartObject();
		jgen.writeStringField("key", value.getKey());
		jgen.writeStringField("severity", value.getSeverity() != null ? value.getSeverity().toString() : null);

		jgen.writeArrayFieldStart("properties");
		Collection<String> properties = value.getProperties();
		if (properties != null) {
			for (String property : properties) {
				jgen.writeString(property);
			}
		}
		jgen.writeEndArray();

		jgen.writeArrayFieldStart("inserts");
		Collection<Object> inserts = value.getInserts();
		if (inserts != null) {
			for (Object insert : inserts) {
				jgen.writeObject(insert);
			}
		}
		jgen.writeEndArray();

		jgen.writeStringField("summaryMessage",
				MessageUtils.getSummaryMessage(value.getKey(), value.getInserts(), this.messageResourceBundleClazz));
		jgen.writeStringField("detailMessage",
				MessageUtils.getDetailMessage(value.getKey(), value.getInserts(), this.messageResourceBundleClazz));
		jgen.writeEndObject();
	}

}
