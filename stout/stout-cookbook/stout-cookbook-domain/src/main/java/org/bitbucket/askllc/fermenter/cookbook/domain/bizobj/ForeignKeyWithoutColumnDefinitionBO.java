package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the ForeignKeyWithoutColumnDefinition entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ForeignKeyWithoutColumnDefinitionBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="FK_WITHOUT_COLUMN_DEFINITION")
public class ForeignKeyWithoutColumnDefinitionBO extends ForeignKeyWithoutColumnDefinitionBaseBO {
	
	public ForeignKeyWithoutColumnDefinitionBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ForeignKeyWithoutColumnDefinitionBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
}