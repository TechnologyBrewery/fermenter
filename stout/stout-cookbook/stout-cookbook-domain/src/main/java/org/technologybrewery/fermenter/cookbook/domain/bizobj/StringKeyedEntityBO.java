package org.technologybrewery.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.technologybrewery.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the StringKeyedEntity entity.
 * @see org.technologybrewery.fermenter.cookbook.domain.bizobj.StringKeyedEntityBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 *
 * Originally generated from templates/bo.java.vm
 */
@Entity
@Table(name="STRING_KEYED_ENTITY")
public class StringKeyedEntityBO extends StringKeyedEntityBaseBO {
	
	private static final Logger logger = LoggerFactory.getLogger(StringKeyedEntityBO.class);

	public StringKeyedEntityBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	@Override
	protected Logger getLogger() {
		return logger;
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
