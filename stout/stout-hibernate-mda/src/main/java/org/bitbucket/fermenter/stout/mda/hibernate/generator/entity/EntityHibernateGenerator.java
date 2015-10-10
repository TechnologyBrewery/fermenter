package org.bitbucket.fermenter.stout.mda.hibernate.generator.entity;

import org.bitbucket.fermenter.mda.generator.AbstractResourcesGenerator;
import org.bitbucket.fermenter.stout.mda.generator.entity.AbstractJavaEntityGenerator;

public class EntityHibernateGenerator extends AbstractJavaEntityGenerator {

	protected String getOutputSubFolder() {
		return AbstractResourcesGenerator.OUTPUT_SUB_FOLDER_RESOURCES;
	}

}
