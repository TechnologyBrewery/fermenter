package org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj;


import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the LocalTransientDomain entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.referencing.domain.bizobj.LocalTransientDomainBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
public class LocalTransientDomainBO extends LocalTransientDomainBaseBO {
	
	public LocalTransientDomainBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalTransientDomainBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
}