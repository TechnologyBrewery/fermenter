package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the ForeignKeyWithColumnDefinition entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ForeignKeyWithColumnDefinitionBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="FK_WITH_COLUMN_DEFINITION")
public class ForeignKeyWithColumnDefinitionBO extends ForeignKeyWithColumnDefinitionBaseBO {
	
	public ForeignKeyWithColumnDefinitionBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ForeignKeyWithColumnDefinitionBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
}