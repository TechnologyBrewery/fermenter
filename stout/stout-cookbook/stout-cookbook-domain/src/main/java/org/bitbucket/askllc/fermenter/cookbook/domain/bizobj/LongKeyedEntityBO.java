package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the LongKeyedEntity entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.LongKeyedEntityBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="LONG_KEYED_ENTITY")
public class LongKeyedEntityBO extends LongKeyedEntityBaseBO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LongKeyedEntityBO.class);

	public LongKeyedEntityBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	public static void deleteAllLongKeyedEntity() {
		getDefaultRepository().deleteAllInBatch();
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
}