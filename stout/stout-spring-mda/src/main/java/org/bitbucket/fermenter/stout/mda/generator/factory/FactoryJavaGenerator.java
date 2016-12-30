package org.bitbucket.fermenter.stout.mda.generator.factory;

import org.bitbucket.fermenter.stout.mda.generator.AbstractJavaGenerator;

public class FactoryJavaGenerator extends AbstractFactoryGenerator {

	protected String getOutputSubFolder() {
		return AbstractJavaGenerator.OUTPUT_SUB_FOLDER_JAVA;
	}

}
