package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the IntegerKeyedEntity entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.IntegerKeyedEntityBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="INT_KEYED_ENTITY")
public class IntegerKeyedEntityBO extends IntegerKeyedEntityBaseBO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IntegerKeyedEntityBO.class);

	public IntegerKeyedEntityBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
}