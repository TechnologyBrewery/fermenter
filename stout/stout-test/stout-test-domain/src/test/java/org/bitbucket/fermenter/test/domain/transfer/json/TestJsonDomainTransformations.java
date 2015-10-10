package org.bitbucket.fermenter.test.domain.transfer.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bitbucket.fermenter.test.domain.enumeration.SimpleDomainEnumeration;
import org.bitbucket.fermenter.test.domain.transfer.SimpleDomain;
import org.bitbucket.fermenter.test.domain.transfer.TransferObjectFactory;
import org.bitbucket.fermenter.test.domain.transfer.ValidationExample;
import org.bitbucket.fermenter.test.domain.transfer.ValidationExampleChild;
import org.bitbucket.fermenter.test.domain.transfer.ValidationReferenceExample;
import org.bitbucket.fermenter.test.domain.transfer.ValidationReferencedObject;
import org.junit.Test;

public class TestJsonDomainTransformations extends AbstractTestTransformations {

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
	public void testTransformationOfEnumerationField() throws Exception {
		SimpleDomain domain = TransferObjectFactory.createSimpleDomain();
		final SimpleDomainEnumeration enumValue = SimpleDomainEnumeration.values()[RandomUtils.nextInt(0, SimpleDomainEnumeration
				.values().length)];
		domain.setAnEnumeratedValue(enumValue);
		
		String json = objectMapper.writeValueAsString(domain);
		assertNotNull(json);
		assertTrue(json.contains(enumValue.toString()));
		
		SimpleDomain rehydratedDomain = objectMapper.readValue(json, SimpleDomain.class);
		assertNotNull(rehydratedDomain);
		assertEquals(enumValue, rehydratedDomain.getAnEnumeratedValue());
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
	
	@Test
	public void testTransformationOfReference() throws Exception {
		ValidationReferenceExample domain = TransferObjectFactory.createValidationReferenceExample();
		ValidationReferencedObject reference = TransferObjectFactory.createValidationReferencedObject();
		final String field = RandomStringUtils.randomAlphanumeric(25);
		reference.setSomeDataField(field);
		domain.setRequiredReference(reference);
		
		String json = objectMapper.writeValueAsString(domain);
		assertNotNull(json);
		assertTrue(json.contains(field));
		
		ValidationReferenceExample rehydratedDomain = objectMapper.readValue(json, ValidationReferenceExample.class);
		assertNotNull(rehydratedDomain);
		ValidationReferencedObject rehydratedReference = rehydratedDomain.getRequiredReference();
		assertNotNull(rehydratedReference);
		assertEquals(field, rehydratedReference.getSomeDataField());
		
	}	
	
}
