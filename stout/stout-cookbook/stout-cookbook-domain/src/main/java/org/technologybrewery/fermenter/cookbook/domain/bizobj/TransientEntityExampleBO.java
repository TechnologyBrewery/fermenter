package org.technologybrewery.fermenter.cookbook.domain.bizobj;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the TransientEntityExample entity.
 * @see org.technologybrewery.fermenter.cookbook.domain.bizobj.TransientEntityExampleBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 *
 * Originally generated from templates/bo.java.vm
 */
public abstract class TransientEntityExampleBO extends TransientEntityExampleBaseBO {
	
	private static final Logger logger = LoggerFactory.getLogger(TransientEntityExampleBO.class);

	
	@Override
	protected Logger getLogger() {
		return logger;
	}
    
    /**
    * Lifecycle method that is invoked when saving TransientEntityExample via a creation or update, 
    * only if the entity's fields were validated successfully. 
    * 
    * If TransientEntityExample requires additional business logic in order to validate its data prior to saving, 
    * implement that logic here.
    * 
    * @see <a href="https://fermenter.atlassian.net/wiki/spaces/FER/pages/62128129/Stout#Stout-Savelifecycle">Stout: Save Lifecycle</a>
    */
	@Override
	protected void complexValidation() {
        /* add complex validation logic here */
	}
}
