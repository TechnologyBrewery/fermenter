package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the MappedSubclassA entity.
 * 
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.MappedSubclassABaseBO
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name = "MAPPED_SUBCLASS_A")
public class MappedSubclassABO extends MappedSubclassABaseBO {

    public MappedSubclassABO() {
        super();
        SpringAutowiringUtil.autowireBizObj(this);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MappedSubclassABO.class);

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void complexValidation() {

    }

    public static List<MappedSubclassABO> findAllSubclassAs() {
        return getDefaultRepository().findAll();
    }
}