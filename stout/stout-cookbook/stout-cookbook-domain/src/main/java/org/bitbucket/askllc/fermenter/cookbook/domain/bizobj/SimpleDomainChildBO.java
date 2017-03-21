package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the SimpleDomainChild entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainChildBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="SIMPLE_DOMAIN_CHILD")
public class SimpleDomainChildBO extends SimpleDomainChildBaseBO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDomainChildBO.class);

	public SimpleDomainChildBO() {
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