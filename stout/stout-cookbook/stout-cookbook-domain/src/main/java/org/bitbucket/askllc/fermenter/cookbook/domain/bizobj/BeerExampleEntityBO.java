package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the BeerExampleEntity entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.BeerExampleEntityBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 *
 * Originally generated from templates/bo.java.vm
 */
@Entity
@Table(name="BEER_ENTITY")
public class BeerExampleEntityBO extends BeerExampleEntityBaseBO {
	
	private static final Logger logger = LoggerFactory.getLogger(BeerExampleEntityBO.class);

	public BeerExampleEntityBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
    
    /**
    * Lifecycle method that is invoked when saving BeerExampleEntity via a creation or update, 
    * only if the entity's fields were validated successfully. 
    * 
    * If BeerExampleEntity requires additional business logic in order to validate its data prior to saving, 
    * implement that logic here.
    * 
    * @see <a href="https://fermenter.atlassian.net/wiki/spaces/FER/pages/62128129/Stout#Stout-Savelifecycle">Stout: Save Lifecycle</a>
    */
	@Override
	protected void complexValidation() {
        /* add complex validation logic here */
	}
}
