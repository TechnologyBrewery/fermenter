package com.ask.test.domain.transfer.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ask.test.domain.transfer.SimpleDomain;
import com.ask.test.domain.transfer.TransferObjectFactory;
import com.ask.test.domain.transfer.ValidationExample;
import com.ask.test.domain.transfer.ValidationExampleChild;

public class TestJsonDomainTransformations {

	private static ObjectMapper objectMapper;
	
	@BeforeClass
	public static void init() {
		Weld weld = new Weld();
		WeldContainer container = weld.initialize();
		objectMapper = container.instance().select(ObjectMapper.class).get();
	}
	
	@Test
	public void testObjectMapperExists() {
		assertNotNull(objectMapper);
	}
	
	@Test
	public void testTransformationOfIdField() throws Exception {
		SimpleDomain domain = TransferObjectFactory.createSimpleDomain();
		final String id = UUID.randomUUID().toString();
		domain.setId(id);
		
		String json = objectMapper.writeValueAsString(domain);
		assertNotNull(json);
		assertTrue(json.contains(id));
		
		SimpleDomain rehydratedDomain = objectMapper.readValue(json, SimpleDomain.class);
		assertNotNull(rehydratedDomain);
		assertEquals(id, rehydratedDomain.getId());

	}
	
	@Test
	public void testTransformationOfField() throws Exception {
		SimpleDomain domain = TransferObjectFactory.createSimpleDomain();
		final String name = RandomStringUtils.randomAlphanumeric(25);
		domain.setName(name);
		
		String json = objectMapper.writeValueAsString(domain);
		assertNotNull(json);
		assertTrue(json.contains(name));
		
		SimpleDomain rehydratedDomain = objectMapper.readValue(json, SimpleDomain.class);
		assertNotNull(rehydratedDomain);
		assertEquals(name, rehydratedDomain.getName());
	}
	
	@Test
	public void testTransformationOfChildren() throws Exception {
		ValidationExample domain = TransferObjectFactory.createValidationExample();
		
		ValidationExampleChild child = TransferObjectFactory.createValidationExampleChild();
		final String childId = UUID.randomUUID().toString();
		child.setId(childId);
		domain.addValidationExampleChild(child);
		
		String json = objectMapper.writeValueAsString(domain);
		assertNotNull(json);
		assertTrue(json.contains(childId));
		
		ValidationExample rehydratedDomain = objectMapper.readValue(json, ValidationExample.class);
		assertNotNull(rehydratedDomain);
		Set<ValidationExampleChild> rehydratedChildren = rehydratedDomain.getValidationExampleChilds();
		assertNotNull(rehydratedChildren);
		ValidationExampleChild rehydratedChild = rehydratedChildren.iterator().next();
		assertNotNull(rehydratedChild);
		assertEquals(childId, rehydratedChild.getId());
				
	}	
	
	
}
