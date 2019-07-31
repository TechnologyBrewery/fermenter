package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the CachedEntityExample entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.CachedEntityExampleBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="CACHED_ENTITY_EXAMPLE")
public class CachedEntityExampleBO extends CachedEntityExampleBaseBO {
	
	public CachedEntityExampleBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CachedEntityExampleBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
}