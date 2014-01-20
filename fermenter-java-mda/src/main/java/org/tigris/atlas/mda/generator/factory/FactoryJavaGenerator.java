package org.tigris.atlas.mda.generator.factory;

import org.tigris.atlas.mda.generator.AbstractJavaGenerator;

public class FactoryJavaGenerator extends AbstractFactoryGenerator {

	protected String getOutputSubFolder() {
		return AbstractJavaGenerator.OUTPUT_SUB_FOLDER_JAVA;
	}

}
