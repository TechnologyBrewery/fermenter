package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;


import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the TransientEntityExample entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.TransientEntityExampleBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
public class TransientEntityExampleBO extends TransientEntityExampleBaseBO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransientEntityExampleBO.class);

	public TransientEntityExampleBO() {
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