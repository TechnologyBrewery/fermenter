package org.bitbucket.fermenter.test.domain.persist.hibernate;

import org.bitbucket.fermenter.test.domain.persist.DaoFactoryInterface;

import org.bitbucket.fermenter.test.domain.persist.ValidationExampleChildDao;
import org.bitbucket.fermenter.test.domain.persist.ValidationExampleDao;
import org.bitbucket.fermenter.test.domain.persist.ValidationReferencedObjectDao;
import org.bitbucket.fermenter.test.domain.persist.ValidationReferenceExampleDao;
import org.bitbucket.fermenter.test.domain.persist.SimpleDomainDao;
import org.bitbucket.fermenter.test.domain.persist.hibernate.ValidationExampleChildDaoImpl;
import org.bitbucket.fermenter.test.domain.persist.hibernate.ValidationExampleDaoImpl;
import org.bitbucket.fermenter.test.domain.persist.hibernate.ValidationReferencedObjectDaoImpl;
import org.bitbucket.fermenter.test.domain.persist.hibernate.ValidationReferenceExampleDaoImpl;
import org.bitbucket.fermenter.test.domain.persist.hibernate.SimpleDomainDaoImpl;

public class DaoFactoryImpl implements DaoFactoryInterface {

	/**
	 * Create a(n) ValidationExampleChild data access object instance.
	 */
	public ValidationExampleChildDao createValidationExampleChildDao() {
		return new ValidationExampleChildDaoImpl();
	}

	/**
	 * Create a(n) ValidationExample data access object instance.
	 */
	public ValidationExampleDao createValidationExampleDao() {
		return new ValidationExampleDaoImpl();
	}

	/**
	 * Create a(n) ValidationReferencedObject data access object instance.
	 */
	public ValidationReferencedObjectDao createValidationReferencedObjectDao() {
		return new ValidationReferencedObjectDaoImpl();
	}

	/**
	 * Create a(n) ValidationReferenceExample data access object instance.
	 */
	public ValidationReferenceExampleDao createValidationReferenceExampleDao() {
		return new ValidationReferenceExampleDaoImpl();
	}

	/**
	 * Create a(n) SimpleDomain data access object instance.
	 */
	public SimpleDomainDao createSimpleDomainDao() {
		return new SimpleDomainDaoImpl();
	}

}