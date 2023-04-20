package org.technologybrewery.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.technologybrewery.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the BusinessKeyedExample entity.
 * @see org.technologybrewery.fermenter.cookbook.domain.bizobj.BusinessKeyedExampleBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 *
 * Originally generated from templates/bo.java.vm
 */
@Entity
@Table(name="BUSINESS_KEYED_EXAMPLE")
public class BusinessKeyedExampleBO extends BusinessKeyedExampleBaseBO {
	
	private static final Logger logger = LoggerFactory.getLogger(BusinessKeyedExampleBO.class);

	public BusinessKeyedExampleBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
    
    /**
    * Lifecycle method that is invoked when saving BusinessKeyedExample via a creation or update, 
    * only if the entity's fields were validated successfully. 
    * 
    * If BusinessKeyedExample requires additional business logic in order to validate its data prior to saving, 
    * implement that logic here.
    * 
    * @see <a href="https://fermenter.atlassian.net/wiki/spaces/FER/pages/62128129/Stout#Stout-Savelifecycle">Stout: Save Lifecycle</a>
    */
	@Override
	protected void complexValidation() {
        /* add complex validation logic here */
	}

    public static BusinessKeyedExampleBO findByBusinessKey(String businessKey) {
	    return getDefaultRepository().findByBusinessKey(businessKey);
	}
}
