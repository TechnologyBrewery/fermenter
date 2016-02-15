package org.bitbucket.fermenter.stout.mda.hibernate.generator.entity;

import org.bitbucket.fermenter.mda.generator.AbstractResourcesGenerator;
import org.bitbucket.fermenter.stout.mda.generator.entity.AbstractJavaEntityGenerator;

/**
 * Provides Hibernate-specific information to standard Java entity generation.
 */
public class EntityHibernateGenerator extends AbstractJavaEntityGenerator {

	/**
	 * {@inheritDoc}
	 */
	protected String getOutputSubFolder() {
		return AbstractResourcesGenerator.OUTPUT_SUB_FOLDER_RESOURCES;
	}
	
}
