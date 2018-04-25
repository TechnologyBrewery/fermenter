package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the MappedSuperclassChildB entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.MappedSuperclassChildBBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="MAPPED_SUPERCLASS_CHILD_B")
public class MappedSuperclassChildBBO extends MappedSuperclassChildBBaseBO {
	
	public MappedSuperclassChildBBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MappedSuperclassChildBBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
    public static List<MappedSuperclassChildBBO> findAllChildBBOs() {
        return getDefaultRepository().findAll();
    }
}