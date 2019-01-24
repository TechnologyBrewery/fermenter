package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the LocalTransientReferenceExample entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.LocalTransientReferenceExampleBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="LOCAL_VALIDATION_REF_EXAMPLE")
public class LocalTransientReferenceExampleBO extends LocalTransientReferenceExampleBaseBO {
	
	public LocalTransientReferenceExampleBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalTransientReferenceExampleBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
}