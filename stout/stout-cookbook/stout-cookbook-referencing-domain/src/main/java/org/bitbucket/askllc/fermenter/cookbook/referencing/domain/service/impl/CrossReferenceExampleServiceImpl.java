package org.bitbucket.askllc.fermenter.cookbook.referencing.domain.service.impl;

import java.util.Collection;

import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.SimpleDomain;
import org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.referencing.domain.service.rest.CrossReferenceExampleService;
import org.springframework.stereotype.Service;

/**
 * Service implementation class for the CrossReferenceExample service.
 * @see org.bitbucket.askllc.fermenter.cookbook.referencing.domain.service.rest.CrossReferenceExampleService
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Service
public class CrossReferenceExampleServiceImpl extends CrossReferenceExampleBaseServiceImpl implements CrossReferenceExampleService {

	/**
	 * {@inheritDoc}
	 */
	@Override
		protected Collection<SimpleDomain> doSomethingAndReturnARemoteEntityImpl() {
		//TODO: Add Business Logic Here
	
			return null;
		}


	/**
	 * {@inheritDoc}
	 */
	@Override
		protected Collection<LocalDomainBO> doSomethingWithARemoteEntityParamImpl(SimpleDomain remoteEntity) {
		//TODO: Add Business Logic Here
	
			return null;
		}

}