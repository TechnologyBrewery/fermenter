package org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the LocalDomain entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalDomainBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="LOCAL_DOMAIN")
public class LocalDomainBO extends LocalDomainBaseBO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalDomainBO.class);

	public LocalDomainBO() {
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