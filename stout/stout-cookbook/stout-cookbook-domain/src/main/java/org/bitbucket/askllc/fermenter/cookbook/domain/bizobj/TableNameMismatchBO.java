package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the TableNameMismatch entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.TableNameMismatchBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="NEW_TABLE_NAME")
public class TableNameMismatchBO extends TableNameMismatchBaseBO {
	
	public TableNameMismatchBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	public static void deleteAllTableNameMismatch() {
		getDefaultRepository().deleteAllInBatch();
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TableNameMismatchBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
}