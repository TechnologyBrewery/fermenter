package org.bitbucket.askllc.fermenter.cookbook.domain.service.impl;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.CachedEntityExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.CachedEntityExampleBusinessService;
import org.springframework.stereotype.Service;

/**
 * Service implementation class for the CachedEntityExampleBusiness service.
 * 
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.CachedEntityExampleBusinessService
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Service
public class CachedEntityExampleBusinessServiceImpl extends CachedEntityExampleBusinessBaseServiceImpl
        implements CachedEntityExampleBusinessService {

    /**
     * {@inheritDoc}
     */
    @Override
    protected CachedEntityExampleBO findByNameImpl(String name) {
        return CachedEntityExampleBO.findByName(name);
    }

}