package org.bitbucket.fermenter.stout.mda.generator.factory;

import org.bitbucket.fermenter.mda.generator.AbstractResourcesGenerator;

public class FactoryResourceGenerator extends AbstractFactoryGenerator {

	protected String getOutputSubFolder() {
		return AbstractResourcesGenerator.OUTPUT_SUB_FOLDER_RESOURCES;
	}

}
