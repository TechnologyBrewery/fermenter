package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the BusinessKeyedExample entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.BusinessKeyedExampleBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="BUSINESS_KEYED_EXAMPLE")
public class BusinessKeyedExampleBO extends BusinessKeyedExampleBaseBO {
	
	public BusinessKeyedExampleBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessKeyedExampleBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}

    public static BusinessKeyedExampleBO findByBusinessKey(String businessKey) {
	    return getDefaultRepository().findByBusinessKey(businessKey);
	}

}