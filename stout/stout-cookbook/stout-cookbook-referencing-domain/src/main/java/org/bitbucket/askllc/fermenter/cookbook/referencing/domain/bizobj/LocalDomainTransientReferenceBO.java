package org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the LocalDomainTransientReference entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalDomainTransientReferenceBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="LOCAL_DOMAIN_TWO")
public class LocalDomainTransientReferenceBO extends LocalDomainTransientReferenceBaseBO {
	
	public LocalDomainTransientReferenceBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalDomainTransientReferenceBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
}