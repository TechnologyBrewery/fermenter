package org.bitbucket.fermenter.test.domain.transfer;

import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.bitbucket.fermenter.transfer.TransferObject;
import org.bitbucket.fermenter.transfer.PrimaryKey;

public class TransferObjectFactoryImpl implements TransferObjectFactoryInterface {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransferObjectFactoryImpl.class);
	private static Map TRANSFER_OBJECT_TYPES = null;
	private static Map PRIMARY_KEY_TYPES = null;

	private static final String PREFIX = "org.bitbucket.fermenter.test.domain.transfer.";
	private static final String PK = "PK";

	static {
		TRANSFER_OBJECT_TYPES = new HashMap(5);
		PRIMARY_KEY_TYPES = new HashMap(5);

		TRANSFER_OBJECT_TYPES.put(ValidationExampleChild.ENTITY, PREFIX + "ValidationExampleChild");
		PRIMARY_KEY_TYPES.put(ValidationExampleChild.ENTITY, PREFIX + "ValidationExampleChild" + PK);

		TRANSFER_OBJECT_TYPES.put(ValidationExample.ENTITY, PREFIX + "ValidationExample");
		PRIMARY_KEY_TYPES.put(ValidationExample.ENTITY, PREFIX + "ValidationExample" + PK);

		TRANSFER_OBJECT_TYPES.put(ValidationReferencedObject.ENTITY, PREFIX + "ValidationReferencedObject");
		PRIMARY_KEY_TYPES.put(ValidationReferencedObject.ENTITY, PREFIX + "ValidationReferencedObject" + PK);

		TRANSFER_OBJECT_TYPES.put(ValidationReferenceExample.ENTITY, PREFIX + "ValidationReferenceExample");
		PRIMARY_KEY_TYPES.put(ValidationReferenceExample.ENTITY, PREFIX + "ValidationReferenceExample" + PK);

		TRANSFER_OBJECT_TYPES.put(SimpleDomain.ENTITY, PREFIX + "SimpleDomain");
		PRIMARY_KEY_TYPES.put(SimpleDomain.ENTITY, PREFIX + "SimpleDomain" + PK);
	}

	public TransferObject createTransferObject(String entityName) {
		TransferObject transferObject = null;
		String type = (String) TRANSFER_OBJECT_TYPES.get(entityName);

		try {
			Class clazz = Class.forName(type);
			transferObject = (TransferObject) clazz.newInstance();
		} catch (InstantiationException e) {
			LOGGER.error("Unable to instantiate type: " + type);
		} catch (IllegalAccessException e) {
			LOGGER.error("Unable to access type: " + type);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Unable to find class: " + type);
		}

		return transferObject;
	}

	public PrimaryKey createPrimaryKey(String entityName) {
		PrimaryKey primaryKey = null;
		String type = (String) PRIMARY_KEY_TYPES.get(entityName);

		try {
			Class clazz = Class.forName(type);
			primaryKey = (PrimaryKey) clazz.newInstance();
		} catch (InstantiationException e) {
			LOGGER.error("Unable to instantiate type: " + type);
		} catch (IllegalAccessException e) {
			LOGGER.error("Unable to access type: " + type);
		} catch (ClassNotFoundException e) {
			LOGGER.error("Unable to find class: " + type);
		}

		return primaryKey;
	}

	/**
	 * Create a(n) ValidationExampleChild transfer object instance
	 */
	public ValidationExampleChild createValidationExampleChild() {
		return (ValidationExampleChild) createTransferObject(ValidationExampleChild.ENTITY);
	}

	/**
	 * Create a(n) ValidationExampleChild primary key instance
	 */
	public ValidationExampleChildPK createValidationExampleChildPK() {
		return (ValidationExampleChildPK) createPrimaryKey(ValidationExampleChild.ENTITY);
	}

	/**
	 * Create a(n) ValidationExample transfer object instance
	 */
	public ValidationExample createValidationExample() {
		return (ValidationExample) createTransferObject(ValidationExample.ENTITY);
	}

	/**
	 * Create a(n) ValidationExample primary key instance
	 */
	public ValidationExamplePK createValidationExamplePK() {
		return (ValidationExamplePK) createPrimaryKey(ValidationExample.ENTITY);
	}

	/**
	 * Create a(n) ValidationReferencedObject transfer object instance
	 */
	public ValidationReferencedObject createValidationReferencedObject() {
		return (ValidationReferencedObject) createTransferObject(ValidationReferencedObject.ENTITY);
	}

	/**
	 * Create a(n) ValidationReferencedObject primary key instance
	 */
	public ValidationReferencedObjectPK createValidationReferencedObjectPK() {
		return (ValidationReferencedObjectPK) createPrimaryKey(ValidationReferencedObject.ENTITY);
	}

	/**
	 * Create a(n) ValidationReferenceExample transfer object instance
	 */
	public ValidationReferenceExample createValidationReferenceExample() {
		return (ValidationReferenceExample) createTransferObject(ValidationReferenceExample.ENTITY);
	}

	/**
	 * Create a(n) ValidationReferenceExample primary key instance
	 */
	public ValidationReferenceExamplePK createValidationReferenceExamplePK() {
		return (ValidationReferenceExamplePK) createPrimaryKey(ValidationReferenceExample.ENTITY);
	}

	/**
	 * Create a(n) SimpleDomain transfer object instance
	 */
	public SimpleDomain createSimpleDomain() {
		return (SimpleDomain) createTransferObject(SimpleDomain.ENTITY);
	}

	/**
	 * Create a(n) SimpleDomain primary key instance
	 */
	public SimpleDomainPK createSimpleDomainPK() {
		return (SimpleDomainPK) createPrimaryKey(SimpleDomain.ENTITY);
	}

}