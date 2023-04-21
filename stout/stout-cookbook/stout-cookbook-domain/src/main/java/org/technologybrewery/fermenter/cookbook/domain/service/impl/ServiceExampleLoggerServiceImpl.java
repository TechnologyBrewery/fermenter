package org.technologybrewery.fermenter.cookbook.domain.service.impl;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.ServiceEntityExampleBO;
import org.technologybrewery.fermenter.cookbook.domain.service.rest.ServiceExampleLoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



/**
 * Service implementation class for the ServiceExampleLogger service.
 * @see org.technologybrewery.fermenter.cookbook.domain.service.rest.ServiceExampleLoggerService
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */

@Service
public class ServiceExampleLoggerServiceImpl extends ServiceExampleLoggerBaseServiceImpl implements ServiceExampleLoggerService {

	private static final Logger logger = LoggerFactory.getLogger(ServiceExampleLoggerServiceImpl.class);
	
	/**
	 * {@inheritDoc}
	 */
	public void logServiceEntityExampleImpl() {
		
		ServiceEntityExampleBO bo = new ServiceEntityExampleBO();
		
		logger.debug("ServiceExampleLogger: ServiceEntityExampleBO() - START\n");
		bo.save();
		logger.debug("ServiceExampleLogger: ServiceEntityExampleBO() - END");
		
	}


}
