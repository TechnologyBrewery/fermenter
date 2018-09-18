package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the UuidKeyedEntity entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.UuidKeyedEntityBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="UUID_KEYED_ENTITY")
public class UuidKeyedEntityBO extends UuidKeyedEntityBaseBO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UuidKeyedEntityBO.class);

	public UuidKeyedEntityBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	public static void deleteAllUuidKeyedEntity() {
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