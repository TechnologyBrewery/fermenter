package org.bitbucket.fermenter.mda.generator.entity;

import org.bitbucket.fermenter.mda.generator.AbstractResourcesGenerator;

public class EntityDdlGenerator extends AbstractJavaEntityGenerator {

	protected String getOutputSubFolder() {
		return AbstractResourcesGenerator.OUTPUT_SUB_FOLDER_RESOURCES;
	}

}
