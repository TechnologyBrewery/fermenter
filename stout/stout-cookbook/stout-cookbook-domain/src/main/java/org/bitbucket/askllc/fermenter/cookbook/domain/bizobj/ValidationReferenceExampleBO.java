package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the ValidationReferenceExample entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationReferenceExampleBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="VALIDATION_REF_EXAMPLE")
public class ValidationReferenceExampleBO extends ValidationReferenceExampleBaseBO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationReferenceExampleBO.class);

	public ValidationReferenceExampleBO() {
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