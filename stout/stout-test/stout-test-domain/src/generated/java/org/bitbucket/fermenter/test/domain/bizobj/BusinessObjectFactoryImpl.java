package org.bitbucket.fermenter.test.domain.bizobj;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bitbucket.fermenter.bizobj.BusinessObject;
import org.bitbucket.fermenter.test.domain.transfer.*;

public class BusinessObjectFactoryImpl implements BusinessObjectFactoryInterface {

	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessObjectFactoryImpl.class);
	private static Map<String, String> BUSINESS_OBJECT_TYPES = null;

	private static final String PREFIX = "org.bitbucket.fermenter.test.domain.bizobj.";

	static {
		BUSINESS_OBJECT_TYPES = new HashMap<String, String>();
		BUSINESS_OBJECT_TYPES.put(ValidationExampleChild.ENTITY, PREFIX + "ValidationExampleChildBO");
		BUSINESS_OBJECT_TYPES.put(ValidationExample.ENTITY, PREFIX + "ValidationExampleBO");
		BUSINESS_OBJECT_TYPES.put(ValidationReferencedObject.ENTITY, PREFIX + "ValidationReferencedObjectBO");
		BUSINESS_OBJECT_TYPES.put(ValidationReferenceExample.ENTITY, PREFIX + "ValidationReferenceExampleBO");
		BUSINESS_OBJECT_TYPES.put(SimpleDomain.ENTITY, PREFIX + "SimpleDomainBO");
	}

	public BusinessObject createBusinessObject(String entityName) {
		BusinessObject businessObject = null;
		String type = (String) BUSINESS_OBJECT_TYPES.get(entityName);

		try {
			Class<?> clazz = Class.forName(type);
			businessObject = (BusinessObject) clazz.newInstance();
		} catch (InstantiationException e) {
			LOGGER.error("Unable to instantiate type: " + type);
		} catch (IllegalAccessException e) {
			LOGGER.error("Unable to access type: " + type);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Unable to find class: " + type);
		}

		return businessObject;
	}

	/**
	 * Create a(n) ValidationExampleChild business object instance.
	 */
	public ValidationExampleChildBO createValidationExampleChildBO() {
		return (ValidationExampleChildBO) createBusinessObject(ValidationExampleChild.ENTITY);
	}

	/**
	 * Create a(n) ValidationExample business object instance.
	 */
	public ValidationExampleBO createValidationExampleBO() {
		return (ValidationExampleBO) createBusinessObject(ValidationExample.ENTITY);
	}

	/**
	 * Create a(n) ValidationReferencedObject business object instance.
	 */
	public ValidationReferencedObjectBO createValidationReferencedObjectBO() {
		return (ValidationReferencedObjectBO) createBusinessObject(ValidationReferencedObject.ENTITY);
	}

	/**
	 * Create a(n) ValidationReferenceExample business object instance.
	 */
	public ValidationReferenceExampleBO createValidationReferenceExampleBO() {
		return (ValidationReferenceExampleBO) createBusinessObject(ValidationReferenceExample.ENTITY);
	}

	/**
	 * Create a(n) SimpleDomain business object instance.
	 */
	public SimpleDomainBO createSimpleDomainBO() {
		return (SimpleDomainBO) createBusinessObject(SimpleDomain.ENTITY);
	}

}