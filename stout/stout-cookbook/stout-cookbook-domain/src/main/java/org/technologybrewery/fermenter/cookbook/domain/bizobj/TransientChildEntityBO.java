package org.technologybrewery.fermenter.cookbook.domain.bizobj;


import org.technologybrewery.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the TransientChildEntity entity.
 * @see org.technologybrewery.fermenter.cookbook.domain.bizobj.TransientChildEntityBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 *
 * Originally generated from templates/bo.java.vm
 */
public class TransientChildEntityBO extends TransientChildEntityBaseBO {
	
	private static final Logger logger = LoggerFactory.getLogger(TransientChildEntityBO.class);

	public TransientChildEntityBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
    
    /**
    * Lifecycle method that is invoked when saving TransientChildEntity via a creation or update, 
    * only if the entity's fields were validated successfully. 
    * 
    * If TransientChildEntity requires additional business logic in order to validate its data prior to saving, 
    * implement that logic here.
    * 
    * @see <a href="https://fermenter.atlassian.net/wiki/spaces/FER/pages/62128129/Stout#Stout-Savelifecycle">Stout: Save Lifecycle</a>
    */
	@Override
	protected void complexValidation() {
        /* add complex validation logic here */
	}
}
