package org.bitbucket.fermenter.test.domain.bizobj;

import org.bitbucket.fermenter.bizobj.AbstractBusinessObjectFactory;
import org.bitbucket.fermenter.bizobj.BusinessObject;

public class BusinessObjectFactory extends AbstractBusinessObjectFactory {

	private static BusinessObjectFactoryInterface getBusinessObjectFactory() {
		return (BusinessObjectFactoryInterface) AbstractBusinessObjectFactory
				.getAbstractBusinessObjectFactory(BusinessObjectFactory.class);
	}

	public static BusinessObject createBusinessObject(String type) {
		return getBusinessObjectFactory().createBusinessObject(type);
	}

	/**
	 * Create a(n) ValidationExampleChild business object instance
	 */
	public static ValidationExampleChildBO createValidationExampleChildBO() {
		return getBusinessObjectFactory().createValidationExampleChildBO();
	}

	/**
	 * Create a(n) ValidationExample business object instance
	 */
	public static ValidationExampleBO createValidationExampleBO() {
		return getBusinessObjectFactory().createValidationExampleBO();
	}

	/**
	 * Create a(n) ValidationReferencedObject business object instance
	 */
	public static ValidationReferencedObjectBO createValidationReferencedObjectBO() {
		return getBusinessObjectFactory().createValidationReferencedObjectBO();
	}

	/**
	 * Create a(n) ValidationReferenceExample business object instance
	 */
	public static ValidationReferenceExampleBO createValidationReferenceExampleBO() {
		return getBusinessObjectFactory().createValidationReferenceExampleBO();
	}

	/**
	 * Create a(n) SimpleDomain business object instance
	 */
	public static SimpleDomainBO createSimpleDomainBO() {
		return getBusinessObjectFactory().createSimpleDomainBO();
	}

}