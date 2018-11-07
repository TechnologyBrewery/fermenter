package org.bitbucket.askllc.fermenter.cookbook.domain.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bitbucket.askllc.fermenter.cookbook.domain.enumeration.SimpleDomainEnumeration;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.EnumerationBasedService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Service implementation class for the EnumerationBased service.
 * 
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.EnumerationBasedService
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Service
public class EnumerationBasedServiceImpl extends EnumerationBasedBaseServiceImpl implements EnumerationBasedService {
    
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EnumerationBasedServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    protected SimpleDomainEnumeration returnSingleEnumeratedValueImpl() {
        return SimpleDomainEnumeration.FIRST;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<SimpleDomainEnumeration> returnMultipleEnumeratedValueImpl() {
        Collection<SimpleDomainEnumeration> values = new ArrayList<>();
        values.add(SimpleDomainEnumeration.SECOND);
        values.add(SimpleDomainEnumeration.THIRD);
        values.add(SimpleDomainEnumeration.FOURTH);
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void passInSingleEnumeratedValueImpl(SimpleDomainEnumeration enumeratedValue) {
        logger.debug("The following enumerated value was passed into this operation: {}", enumeratedValue);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void passInMultipleEnumeratedValuesImpl(List<SimpleDomainEnumeration> enumeratedValues) {
        logger.debug("The following enumerated values were passed into this operation:");
        for (SimpleDomainEnumeration enumeratedValue : enumeratedValues) {
            logger.debug("\t{}", enumeratedValue);
        }

    }

//	@Override
//	protected void passInSingleEnumeratedValueImpl(SimpleDomainEnumeration enumeratedValue) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	protected void passInMultipleEnumeratedValuesImpl(List<SimpleDomainEnumeration> enumeratedValue) {
//		// TODO Auto-generated method stub
//		
//	}

}