package org.technologybrewery.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.technologybrewery.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the SimpleDomainEagerChild entity.
 * @see org.technologybrewery.fermenter.cookbook.domain.bizobj.SimpleDomainEagerChildBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 *
 * Originally generated from templates/bo.java.vm
 */
@Entity
@Table(name="SIMPLE_DOMAIN_EAGER_CHILD")
public class SimpleDomainEagerChildBO extends SimpleDomainEagerChildBaseBO {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleDomainEagerChildBO.class);

	public SimpleDomainEagerChildBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
    
    /**
    * Lifecycle method that is invoked when saving SimpleDomainEagerChild via a creation or update, 
    * only if the entity's fields were validated successfully. 
    * 
    * If SimpleDomainEagerChild requires additional business logic in order to validate its data prior to saving, 
    * implement that logic here.
    * 
    * @see <a href="https://fermenter.atlassian.net/wiki/spaces/FER/pages/62128129/Stout#Stout-Savelifecycle">Stout: Save Lifecycle</a>
    */
	@Override
	protected void complexValidation() {
        /* add complex validation logic here */
	}
}
