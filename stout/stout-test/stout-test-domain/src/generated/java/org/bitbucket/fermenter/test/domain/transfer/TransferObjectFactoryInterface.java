package org.bitbucket.fermenter.test.domain.transfer;

import org.bitbucket.fermenter.transfer.AbstractTransferObjectFactoryInterface;

public interface TransferObjectFactoryInterface extends AbstractTransferObjectFactoryInterface {

	/**
	 * Create a(n) ValidationExampleChild transfer object instance
	 */
	public ValidationExampleChild createValidationExampleChild();

	/**
	 * Create a(n) ValidationExampleChild primary key instance
	 */
	public ValidationExampleChildPK createValidationExampleChildPK();

	/**
	 * Create a(n) ValidationExample transfer object instance
	 */
	public ValidationExample createValidationExample();

	/**
	 * Create a(n) ValidationExample primary key instance
	 */
	public ValidationExamplePK createValidationExamplePK();

	/**
	 * Create a(n) ValidationReferencedObject transfer object instance
	 */
	public ValidationReferencedObject createValidationReferencedObject();

	/**
	 * Create a(n) ValidationReferencedObject primary key instance
	 */
	public ValidationReferencedObjectPK createValidationReferencedObjectPK();

	/**
	 * Create a(n) ValidationReferenceExample transfer object instance
	 */
	public ValidationReferenceExample createValidationReferenceExample();

	/**
	 * Create a(n) ValidationReferenceExample primary key instance
	 */
	public ValidationReferenceExamplePK createValidationReferenceExamplePK();

	/**
	 * Create a(n) SimpleDomain transfer object instance
	 */
	public SimpleDomain createSimpleDomain();

	/**
	 * Create a(n) SimpleDomain primary key instance
	 */
	public SimpleDomainPK createSimpleDomainPK();

}