package org.tigris.atlas.mda.generator;

public abstract class AbstractResourcesGenerator extends AbstractGenerator {

	public static final String OUTPUT_SUB_FOLDER_RESOURCES = "resources/";	
	
	protected String getOutputSubFolder() {
		return OUTPUT_SUB_FOLDER_RESOURCES;
	}

}
