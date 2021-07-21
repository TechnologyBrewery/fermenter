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
 *
 * Originally generated from templates/bo.java.vm
 */
@MappedSuperclass
public abstract class MappedSuperclassParentBO<PK extends Serializable, BO, JPA extends JpaRepository<BO, PK>> 
		extends MappedSuperclassParentBaseBO<PK, BO, JPA> {
	
	private static final Logger logger = LoggerFactory.getLogger(MappedSuperclassParentBO.class);

	
	@Override
	protected Logger getLogger() {
		return logger;
	}
    
    /**
    * Lifecycle method that is invoked when saving MappedSuperclassParent via a creation or update, 
    * only if the entity's fields were validated successfully. 
    * 
    * If MappedSuperclassParent requires additional business logic in order to validate its data prior to saving, 
    * implement that logic here.
    * 
    * @see <a href="https://fermenter.atlassian.net/wiki/spaces/FER/pages/62128129/Stout#Stout-Savelifecycle">Stout: Save Lifecycle</a>
    */
	@Override
	protected void complexValidation() {
        /* add complex validation logic here */
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
