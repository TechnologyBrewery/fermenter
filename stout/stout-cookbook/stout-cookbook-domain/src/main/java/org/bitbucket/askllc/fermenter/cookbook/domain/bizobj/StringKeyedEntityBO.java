package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the StringKeyedEntity entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.StringKeyedEntityBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="STRING_KEYED_ENTITY")
public class StringKeyedEntityBO extends StringKeyedEntityBaseBO {
	
	public StringKeyedEntityBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StringKeyedEntityBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
    
    /**
    * Lifecycle method that is invoked when saving StringKeyedEntity via a creation or update, 
    * only if the entity's fields were validated successfully. 
    * 
    * If StringKeyedEntity requires additional business logic in order to validate its data prior to saving, 
    * implement that logic here.
    * 
    * @see <a href="https://fermenter.atlassian.net/wiki/spaces/FER/pages/62128129/Stout#Stout-Savelifecycle">Stout: Save Lifecycle</a>
    */
	@Override
	protected void complexValidation() {
        /* add complex validation logic here */
	}
}