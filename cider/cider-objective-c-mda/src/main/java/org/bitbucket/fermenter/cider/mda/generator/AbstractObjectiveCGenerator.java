package org.bitbucket.fermenter.cider.mda.generator;

import org.bitbucket.fermenter.mda.generator.AbstractGenerator;

/**
 * Abstract Objective-C generator to hold common methods and properties
 */
public abstract class AbstractObjectiveCGenerator extends AbstractGenerator {

	protected static final String OUTPUT_SUB_FOLDER_OBJECTIVE_C = "objectivec/";
	protected static final String OBJECTIVE_C_PROJECT_NAME = "Wino"; // TODO: load this from metadata

	@Override
	protected String getOutputSubFolder() {
		return OUTPUT_SUB_FOLDER_OBJECTIVE_C;
	}
}
