package org.technologybrewery.fermenter.cookbook.local.referencing.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.technologybrewery.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the Placeholder entity.
 * 
 * @see org.technologybrewery.fermenter.cookbook.local.referencing.domain.bizobj.PlaceholderBaseBO
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name = "PLACEHOLDER")
public class PlaceholderBO extends PlaceholderBaseBO {

    public PlaceholderBO() {
        super();
        SpringAutowiringUtil.autowireBizObj(this);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceholderBO.class);

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void complexValidation() {
        // placeholder for complex validation
    }
}
