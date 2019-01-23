package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the ForeignKeyColumnConventions entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ForeignKeyColumnConventionsBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="FK_COLUMN_CONVENTIONS")
public class ForeignKeyColumnConventionsBO extends ForeignKeyColumnConventionsBaseBO {
	
	public ForeignKeyColumnConventionsBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ForeignKeyColumnConventionsBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
}