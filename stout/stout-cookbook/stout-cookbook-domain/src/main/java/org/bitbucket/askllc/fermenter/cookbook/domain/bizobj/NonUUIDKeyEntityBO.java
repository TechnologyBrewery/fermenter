package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the NonUUIDKeyEntity entity.
 * 
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.NonUUIDKeyEntityBaseBO
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name = "NON_UUID_KEY_ENTITY")
public class NonUUIDKeyEntityBO extends NonUUIDKeyEntityBaseBO {

    public NonUUIDKeyEntityBO() {
        super();
        SpringAutowiringUtil.autowireBizObj(this);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(NonUUIDKeyEntityBO.class);

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void complexValidation() {
        setCalculatedField(getCalculatedField() + 1);
    }

    public static void deleteAllNonUUIDKeyEntities() {
        getDefaultRepository().deleteAllInBatch();
    }

}