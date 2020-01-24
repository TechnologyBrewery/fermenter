package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the ValidationExample entity.
 * 
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationExampleBaseBO
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name = "VALIDATION_EXAMPLE")
public class ValidationExampleBO extends ValidationExampleBaseBO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExampleBO.class);

    public ValidationExampleBO() {
        super();
        SpringAutowiringUtil.autowireBizObj(this);
    }

    public static void deleteAllValidationExamples() {
        getDefaultRepository().deleteAllInBatch();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void complexValidation() {

    }

    public static List<ValidationExampleBO> getAllValidationExamples() {
        return getDefaultRepository().findAll();
    }

    public static List<ValidationExampleBO> grabAllWithRequiredField() {
        return getDefaultRepository().findByRequiredFieldNotNull();
    }

}