package org.bitbucket.fermenter.test.domain.persist;

import org.bitbucket.fermenter.persist.AbstractDaoFactory;

public class DaoFactory extends AbstractDaoFactory {

	private static DaoFactoryInterface getDaoFactory() {
		return (DaoFactoryInterface) AbstractDaoFactory.getAbstractDaoFactory(DaoFactory.class);
	}

	/**
	 * Create a(n) ValidationExampleChild data access object instance
	 */
	public static ValidationExampleChildDao createValidationExampleChildDao() {
		return getDaoFactory().createValidationExampleChildDao();
	}

	/**
	 * Create a(n) ValidationExample data access object instance
	 */
	public static ValidationExampleDao createValidationExampleDao() {
		return getDaoFactory().createValidationExampleDao();
	}

	/**
	 * Create a(n) ValidationReferencedObject data access object instance
	 */
	public static ValidationReferencedObjectDao createValidationReferencedObjectDao() {
		return getDaoFactory().createValidationReferencedObjectDao();
	}

	/**
	 * Create a(n) ValidationReferenceExample data access object instance
	 */
	public static ValidationReferenceExampleDao createValidationReferenceExampleDao() {
		return getDaoFactory().createValidationReferenceExampleDao();
	}

	/**
	 * Create a(n) SimpleDomain data access object instance
	 */
	public static SimpleDomainDao createSimpleDomainDao() {
		return getDaoFactory().createSimpleDomainDao();
	}

}