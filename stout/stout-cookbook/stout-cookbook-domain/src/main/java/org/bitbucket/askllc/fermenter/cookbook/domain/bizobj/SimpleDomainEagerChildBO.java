package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the SimpleDomainEagerChild entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainEagerChildBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="SIMPLE_DOMAIN_EAGER_CHILD")
public class SimpleDomainEagerChildBO extends SimpleDomainEagerChildBaseBO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDomainEagerChildBO.class);

	public SimpleDomainEagerChildBO() {
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