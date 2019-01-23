package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the ForeignKeyColumnCustomName entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ForeignKeyColumnCustomNameBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="FK_CUSTOM_NAME")
public class ForeignKeyColumnCustomNameBO extends ForeignKeyColumnCustomNameBaseBO {
	
	public ForeignKeyColumnCustomNameBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ForeignKeyColumnCustomNameBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
}