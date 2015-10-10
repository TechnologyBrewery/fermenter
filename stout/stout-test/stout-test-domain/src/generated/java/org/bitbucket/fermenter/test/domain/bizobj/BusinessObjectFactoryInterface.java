package org.bitbucket.fermenter.test.domain.bizobj;

import org.bitbucket.fermenter.bizobj.AbstractBusinessObjectFactoryInterface;

public interface BusinessObjectFactoryInterface extends AbstractBusinessObjectFactoryInterface {

	/**
	 * Create a(n) ValidationExampleChild business object instance
	 */
	public ValidationExampleChildBO createValidationExampleChildBO();

	/**
	 * Create a(n) ValidationExample business object instance
	 */
	public ValidationExampleBO createValidationExampleBO();

	/**
	 * Create a(n) ValidationReferencedObject business object instance
	 */
	public ValidationReferencedObjectBO createValidationReferencedObjectBO();

	/**
	 * Create a(n) ValidationReferenceExample business object instance
	 */
	public ValidationReferenceExampleBO createValidationReferenceExampleBO();

	/**
	 * Create a(n) SimpleDomain business object instance
	 */
	public SimpleDomainBO createSimpleDomainBO();

}