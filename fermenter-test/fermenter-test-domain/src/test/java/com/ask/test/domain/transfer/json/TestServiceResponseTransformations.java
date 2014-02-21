package com.ask.test.domain.transfer.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.tigris.atlas.messages.DefaultMessages;
import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.Messages;
import org.tigris.atlas.messages.Severity;
import org.tigris.atlas.service.ValueServiceResponse;
import org.tigris.atlas.service.VoidServiceResponse;

import com.ask.test.domain.transfer.SimpleDomain;
import com.ask.test.domain.transfer.TransferObjectFactory;

public class TestServiceResponseTransformations extends AbstractTestTransformations {

	@Test
	public void testVoidServiceResponse() throws Exception {
		Messages messages = new DefaultMessages();
		Message info = createRandomMessage(Severity.INFO);
		messages.addMessage(info);
		
		VoidServiceResponse response = new VoidServiceResponse(messages);
		
		String json = objectMapper.writeValueAsString(response);
		assertNotNull(json);
		
		VoidServiceResponse rehydratedResponse = objectMapper.readValue(json, VoidServiceResponse.class);
		assertNotNull(rehydratedResponse);
		assertEquals(1, rehydratedResponse.getMessages().getAllMessages().size());
		assertEquals(info, rehydratedResponse.getMessages().getAllMessages().iterator().next());
		
	}
	
	@Test
	public void testValueServiceResponse() throws Exception {
		Messages messages = new DefaultMessages();
		Message info = createRandomMessage(Severity.INFO);
		messages.addMessage(info);
		
		SimpleDomain domain = TransferObjectFactory.createSimpleDomain();
		final String id = UUID.randomUUID().toString();
		domain.setId(id);
		
		ValueServiceResponse<SimpleDomain> response = new ValueServiceResponse<SimpleDomain>(domain, messages);
		
		String json = objectMapper.writeValueAsString(response);
		assertNotNull(json);
		
		TypeReference<ValueServiceResponse<SimpleDomain>> simpleDomainWrapper = new TypeReference<ValueServiceResponse<SimpleDomain>>() {};
		ValueServiceResponse<SimpleDomain> rehydratedResponse = objectMapper.readValue(json, simpleDomainWrapper);
		assertNotNull(rehydratedResponse);
		assertEquals(1, rehydratedResponse.getMessages().getAllMessages().size());
		assertEquals(info, rehydratedResponse.getMessages().getAllMessages().iterator().next());
		
		SimpleDomain rehydratedDomain = rehydratedResponse.getValue();
		assertNotNull(rehydratedDomain);
		assertEquals(domain, rehydratedDomain);
		
	}	
	
}
