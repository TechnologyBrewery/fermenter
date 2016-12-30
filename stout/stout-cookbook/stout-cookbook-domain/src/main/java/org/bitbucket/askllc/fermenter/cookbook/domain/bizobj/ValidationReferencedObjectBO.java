package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the ValidationReferencedObject entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationReferencedObjectBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="VALIDATION_REF_OBJECT")
public class ValidationReferencedObjectBO extends ValidationReferencedObjectBaseBO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationReferencedObjectBO.class);

	public ValidationReferencedObjectBO() {
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