package org.bitbucket.fermenter.mda.generator.factory;

import org.bitbucket.fermenter.mda.generator.AbstractJavaGenerator;

public class FactoryJavaGenerator extends AbstractFactoryGenerator {

	protected String getOutputSubFolder() {
		return AbstractJavaGenerator.OUTPUT_SUB_FOLDER_JAVA;
	}

}
