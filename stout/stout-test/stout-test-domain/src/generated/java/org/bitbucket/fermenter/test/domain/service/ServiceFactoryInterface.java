package org.bitbucket.fermenter.test.domain.service;

import org.bitbucket.fermenter.test.domain.service.ejb.SimpleDomainManagerService;
import org.bitbucket.fermenter.test.domain.service.ejb.ValidationExampleChildMaintenanceService;
import org.bitbucket.fermenter.test.domain.service.ejb.ValidationExampleMaintenanceService;
import org.bitbucket.fermenter.test.domain.service.ejb.ValidationReferencedObjectMaintenanceService;
import org.bitbucket.fermenter.test.domain.service.ejb.ValidationReferenceExampleMaintenanceService;
import org.bitbucket.fermenter.test.domain.service.ejb.SimpleDomainMaintenanceService;

import org.bitbucket.fermenter.factory.Factory;

public interface ServiceFactoryInterface extends Factory {

	/**
	 * Create a(n) SimpleDomainManagerService instance.
	 * 
	 * @return A service to perform operations on SimpleDomainManager
	 */
	SimpleDomainManagerService createSimpleDomainManagerService();

	/**
	 * Create a(n) ValidationExampleChildMaintenanceService instance.
	 * 
	 * @return A service to manage CRUD for {@link ValidationExampleChild}
	 */
	ValidationExampleChildMaintenanceService createValidationExampleChildMaintenanceService();

	/**
	 * Create a(n) ValidationExampleMaintenanceService instance.
	 * 
	 * @return A service to manage CRUD for {@link ValidationExample}
	 */
	ValidationExampleMaintenanceService createValidationExampleMaintenanceService();

	/**
	 * Create a(n) ValidationReferencedObjectMaintenanceService instance.
	 * 
	 * @return A service to manage CRUD for {@link ValidationReferencedObject}
	 */
	ValidationReferencedObjectMaintenanceService createValidationReferencedObjectMaintenanceService();

	/**
	 * Create a(n) ValidationReferenceExampleMaintenanceService instance.
	 * 
	 * @return A service to manage CRUD for {@link ValidationReferenceExample}
	 */
	ValidationReferenceExampleMaintenanceService createValidationReferenceExampleMaintenanceService();

	/**
	 * Create a(n) SimpleDomainMaintenanceService instance.
	 * 
	 * @return A service to manage CRUD for {@link SimpleDomain}
	 */
	SimpleDomainMaintenanceService createSimpleDomainMaintenanceService();

}