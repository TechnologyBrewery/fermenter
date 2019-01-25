package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;


import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the ValidationTransientReferencedObject entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationTransientReferencedObjectBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
public class ValidationTransientReferencedObjectBO extends ValidationTransientReferencedObjectBaseBO {
	
	public ValidationTransientReferencedObjectBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationTransientReferencedObjectBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
}