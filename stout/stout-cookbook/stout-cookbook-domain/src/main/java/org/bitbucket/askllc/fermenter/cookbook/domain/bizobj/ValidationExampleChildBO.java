package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the ValidationExampleChild entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleChildBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="VALIDATION_EXAMPLE_CHILD")
public class ValidationExampleChildBO extends ValidationExampleChildBaseBO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExampleChildBO.class);

	public ValidationExampleChildBO() {
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