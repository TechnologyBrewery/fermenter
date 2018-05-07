package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.MappedSuperclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Business object for the MappedSuperclassParent entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.MappedSuperclassParentBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@MappedSuperclass
public abstract class MappedSuperclassParentBO<PK extends Serializable, BO, JPA extends JpaRepository<BO, PK>> 
		extends MappedSuperclassParentBaseBO<PK, BO, JPA> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MappedSuperclassParentBO.class);
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
    /**
     * Simulates a polymorphic query that isn't natively supported by JPA's mapped superclass functionality.
     * 
     * @return
     */
    public static List<MappedSuperclassParentBO> findAll() {
        List<MappedSuperclassParentBO> allChildBOs = new ArrayList<>();
        allChildBOs.addAll(MappedSubclassABO.findAllSubclassAs());
        allChildBOs.addAll(MappedSubclassBBO.findAllSubclassBs());
        return allChildBOs;
    }
}