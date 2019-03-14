package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the DefaultValueExample entity.
 * 
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.DefaultValueExampleBaseBO
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name = "DEFAULT_VALUE")
public class DefaultValueExampleBO extends DefaultValueExampleBaseBO {

    public DefaultValueExampleBO() {
        super();
        SpringAutowiringUtil.autowireBizObj(this);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultValueExampleBO.class);

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void complexValidation() {

    }

    @Override
    protected void defaultComplexValues() {
    }
}