package org.technologybrewery.fermenter.stout.mda.generator.entity;

import org.technologybrewery.fermenter.mda.generator.entity.AbstractAllOrderedEntitiesAwareGenerator;
import org.technologybrewery.fermenter.stout.mda.java.JavaGeneratorUtil;

public class AllOrderedEntitiesAwareJavaGenerator extends AbstractAllOrderedEntitiesAwareGenerator {

	protected String getOutputSubFolder() {
		return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
	}

}
