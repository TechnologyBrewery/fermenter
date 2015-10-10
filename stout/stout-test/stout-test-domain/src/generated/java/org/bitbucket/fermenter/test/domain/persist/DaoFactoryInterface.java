package org.bitbucket.fermenter.test.domain.persist;

import org.bitbucket.fermenter.persist.AbstractDaoFactoryInterface;

public interface DaoFactoryInterface extends AbstractDaoFactoryInterface {

	/**
	 * Create a(n) ValidationExampleChild data access object instance
	 */
	public ValidationExampleChildDao createValidationExampleChildDao();

	/**
	 * Create a(n) ValidationExample data access object instance
	 */
	public ValidationExampleDao createValidationExampleDao();

	/**
	 * Create a(n) ValidationReferencedObject data access object instance
	 */
	public ValidationReferencedObjectDao createValidationReferencedObjectDao();

	/**
	 * Create a(n) ValidationReferenceExample data access object instance
	 */
	public ValidationReferenceExampleDao createValidationReferenceExampleDao();

	/**
	 * Create a(n) SimpleDomain data access object instance
	 */
	public SimpleDomainDao createSimpleDomainDao();

}