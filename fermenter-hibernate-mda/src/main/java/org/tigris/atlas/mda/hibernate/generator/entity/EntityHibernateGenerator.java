package org.tigris.atlas.mda.hibernate.generator.entity;

import org.tigris.atlas.mda.generator.AbstractResourcesGenerator;
import org.tigris.atlas.mda.generator.entity.AbstractJavaEntityGenerator;

public class EntityHibernateGenerator extends AbstractJavaEntityGenerator {

	protected String getOutputSubFolder() {
		return AbstractResourcesGenerator.OUTPUT_SUB_FOLDER_RESOURCES;
	}

}
