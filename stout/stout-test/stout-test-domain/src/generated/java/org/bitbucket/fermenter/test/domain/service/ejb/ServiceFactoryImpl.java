package org.bitbucket.fermenter.test.domain.service.ejb;

import org.bitbucket.fermenter.test.domain.service.ServiceFactoryInterface;
import org.bitbucket.fermenter.test.domain.service.ejb.SimpleDomainManagerService;
import org.bitbucket.fermenter.test.domain.service.ejb.ValidationExampleChildMaintenanceService;
import org.bitbucket.fermenter.test.domain.service.ejb.ValidationExampleMaintenanceService;
import org.bitbucket.fermenter.test.domain.service.ejb.ValidationReferencedObjectMaintenanceService;
import org.bitbucket.fermenter.test.domain.service.ejb.ValidationReferenceExampleMaintenanceService;
import org.bitbucket.fermenter.test.domain.service.ejb.SimpleDomainMaintenanceService;

import org.bitbucket.fermenter.service.ejb.AbstractEjbServiceFactory;

/**
 * Implementation of the service factory for ejbs.
 */
public class ServiceFactoryImpl extends AbstractEjbServiceFactory implements ServiceFactoryInterface {

	/**
	 * @{inheritDoc
	 */
	public SimpleDomainManagerService createSimpleDomainManagerService() {
		return (SimpleDomainManagerService) performJndiLookup("java:module/SimpleDomainManagerService");
	}

	/**
	 * @{inheritDoc
	 */
	public ValidationExampleChildMaintenanceService createValidationExampleChildMaintenanceService() {
		return (ValidationExampleChildMaintenanceService) performJndiLookup("java:module/ValidationExampleChildMaintenanceService");
	}

	/**
	 * @{inheritDoc
	 */
	public ValidationExampleMaintenanceService createValidationExampleMaintenanceService() {
		return (ValidationExampleMaintenanceService) performJndiLookup("java:module/ValidationExampleMaintenanceService");
	}

	/**
	 * @{inheritDoc
	 */
	public ValidationReferencedObjectMaintenanceService createValidationReferencedObjectMaintenanceService() {
		return (ValidationReferencedObjectMaintenanceService) performJndiLookup("java:module/ValidationReferencedObjectMaintenanceService");
	}

	/**
	 * @{inheritDoc
	 */
	public ValidationReferenceExampleMaintenanceService createValidationReferenceExampleMaintenanceService() {
		return (ValidationReferenceExampleMaintenanceService) performJndiLookup("java:module/ValidationReferenceExampleMaintenanceService");
	}

	/**
	 * @{inheritDoc
	 */
	public SimpleDomainMaintenanceService createSimpleDomainMaintenanceService() {
		return (SimpleDomainMaintenanceService) performJndiLookup("java:module/SimpleDomainMaintenanceService");
	}

}