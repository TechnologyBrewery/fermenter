package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;


import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the TransientChildEntity entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.TransientChildEntityBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 * Generated from bo.java.vm
 */
public class TransientChildEntityBO extends TransientChildEntityBaseBO {
	
	public TransientChildEntityBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransientChildEntityBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
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
