package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Business object for the MappedSubclassB entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.MappedSubclassBBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name="MAPPED_SUBCLASS_B")
public class MappedSubclassBBO extends MappedSubclassBBaseBO {
	
	public MappedSubclassBBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MappedSubclassBBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
    public static List<MappedSubclassBBO> findAllSubclassBs() {
        return getDefaultRepository().findAll();
    }	
	
}