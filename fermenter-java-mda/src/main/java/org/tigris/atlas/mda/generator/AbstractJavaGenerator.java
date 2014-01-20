package org.tigris.atlas.mda.generator;

public abstract class AbstractJavaGenerator extends AbstractGenerator {

	public static final String OUTPUT_SUB_FOLDER_JAVA = "java/";
	
	protected String getOutputSubFolder() {
		return OUTPUT_SUB_FOLDER_JAVA;
	}

}
