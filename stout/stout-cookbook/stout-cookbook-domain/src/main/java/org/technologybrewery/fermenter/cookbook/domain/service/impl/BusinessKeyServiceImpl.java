package org.technologybrewery.fermenter.cookbook.domain.service.impl;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.BusinessKeyedExampleBO;
import org.technologybrewery.fermenter.cookbook.domain.service.rest.BusinessKeyService;
import org.springframework.stereotype.Service;

/**
 * Service implementation class for the BusinessKey service.
 * 
 * @see org.technologybrewery.fermenter.cookbook.domain.service.rest.BusinessKeyService
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY Generated from service.impl.java.vm
 */
@Service
public class BusinessKeyServiceImpl extends BusinessKeyBaseServiceImpl implements BusinessKeyService {

    /**
     * {@inheritDoc}
     */
    @Override
    protected BusinessKeyedExampleBO findByBusinessKeyImpl(String businessKey) {
        return BusinessKeyedExampleBO.findByBusinessKey(businessKey);
    }

}
