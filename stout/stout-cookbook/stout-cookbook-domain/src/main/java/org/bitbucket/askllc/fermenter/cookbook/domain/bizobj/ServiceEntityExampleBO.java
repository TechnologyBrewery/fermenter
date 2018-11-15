package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the ServiceEntityExample entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ServiceEntityExampleBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="SERVICE_ENTITY_EXAMPLE")
public class ServiceEntityExampleBO extends ServiceEntityExampleBaseBO {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceEntityExampleBO.class);
	
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	public ServiceEntityExampleBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}

	@Override
	protected void validateRelations() {
		logger.debug("\tServiceEntityExampleBO: validateRelations() - START");
		super.validateRelations();
		logger.debug("\tServiceEntityExampleBO: validateRelations() - END");
	}

	@Override
	protected void defaultValues() {
		logger.debug("\tServiceEntityExampleBO: defaultValues() - START");
		super.defaultValues();
		logger.debug("\tServiceEntityExampleBO: defaultValues() - END");
		
	}

	@Override
	protected void postSave() {
		logger.debug("\t\t\tServiceEntityExampleBO: postSave() - START");
		super.postSave();
		logger.debug("\t\t\tServiceEntityExampleBO: postSave() - END");
	}

	@Override
	public void validate() {
		logger.debug("\t\tServiceEntityExampleBO: validate() - START");
		super.validate();
		logger.debug("\t\tServiceEntityExampleBO: validate() - END");
	}

	@Override
	protected void preValidate() {
		logger.debug("\tServiceEntityExampleBO: preValidate() - START");
		super.preValidate();
		logger.debug("\tServiceEntityExampleBO: preValidate() - END");
	}

	@Override
	protected void postValidate() {
		logger.debug("\t\tServiceEntityExampleBO: postValidate() - START");
		super.postValidate();
		logger.debug("\t\tServiceEntityExampleBO: postValidate() - END");
	}

	@Override
	public void validateFields() {
		logger.debug("\t\tServiceEntityExampleBO: validateFields() - START");
		super.validateFields();
		logger.debug("\t\tServiceEntityExampleBO: validateFields() - END");
	}
	

	@Override
	protected void complexValidation() {
		logger.debug("\t\tServiceEntityExampleBO: complexValidation() - START");
		logger.debug("\t\tServiceEntityExampleBO: complexValidation() - END");

	}

	@Override
	protected void complexValidationOnRelations() {
		logger.debug("\tServiceEntityExampleBO: complexValidationOnRelations() - START");
		super.complexValidationOnRelations();
		logger.debug("\tServiceEntityExampleBO: complexValidationOnRelations() - END");
	}
	
}