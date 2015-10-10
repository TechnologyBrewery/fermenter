package org.bitbucket.fermenter.test.domain.transfer;

import org.bitbucket.fermenter.transfer.AbstractTransferObjectFactory;
import org.bitbucket.fermenter.transfer.PrimaryKey;
import org.bitbucket.fermenter.transfer.TransferObject;

public class TransferObjectFactory extends AbstractTransferObjectFactory {

	private static TransferObjectFactoryInterface getTransferObjectFactory() {
		return (TransferObjectFactoryInterface) AbstractTransferObjectFactory
				.getAbstractTransferObjectFactory(TransferObjectFactory.class);
	}

	public static TransferObject createTransferObject(String type) {
		return getTransferObjectFactory().createTransferObject(type);
	}

	public static PrimaryKey createPrimaryKey(String type) {
		return getTransferObjectFactory().createPrimaryKey(type);
	}

	/**
	 * Create a(n) ValidationExampleChild transfer object instance
	 */
	public static ValidationExampleChild createValidationExampleChild() {
		return getTransferObjectFactory().createValidationExampleChild();
	}

	/**
	 * Create a(n) ValidationExampleChild primary key instance
	 */
	public static ValidationExampleChildPK createValidationExampleChildPK() {
		return getTransferObjectFactory().createValidationExampleChildPK();
	}

	/**
	 * Create a(n) ValidationExample transfer object instance
	 */
	public static ValidationExample createValidationExample() {
		return getTransferObjectFactory().createValidationExample();
	}

	/**
	 * Create a(n) ValidationExample primary key instance
	 */
	public static ValidationExamplePK createValidationExamplePK() {
		return getTransferObjectFactory().createValidationExamplePK();
	}

	/**
	 * Create a(n) ValidationReferencedObject transfer object instance
	 */
	public static ValidationReferencedObject createValidationReferencedObject() {
		return getTransferObjectFactory().createValidationReferencedObject();
	}

	/**
	 * Create a(n) ValidationReferencedObject primary key instance
	 */
	public static ValidationReferencedObjectPK createValidationReferencedObjectPK() {
		return getTransferObjectFactory().createValidationReferencedObjectPK();
	}

	/**
	 * Create a(n) ValidationReferenceExample transfer object instance
	 */
	public static ValidationReferenceExample createValidationReferenceExample() {
		return getTransferObjectFactory().createValidationReferenceExample();
	}

	/**
	 * Create a(n) ValidationReferenceExample primary key instance
	 */
	public static ValidationReferenceExamplePK createValidationReferenceExamplePK() {
		return getTransferObjectFactory().createValidationReferenceExamplePK();
	}

	/**
	 * Create a(n) SimpleDomain transfer object instance
	 */
	public static SimpleDomain createSimpleDomain() {
		return getTransferObjectFactory().createSimpleDomain();
	}

	/**
	 * Create a(n) SimpleDomain primary key instance
	 */
	public static SimpleDomainPK createSimpleDomainPK() {
		return getTransferObjectFactory().createSimpleDomainPK();
	}

}