package org.bitbucket.fermenter.stout.mda.generator.entity;

import org.bitbucket.fermenter.mda.generator.entity.AbstractAllOrderedEntitiesAwareGenerator;
import org.bitbucket.fermenter.stout.mda.java.JavaGeneratorUtil;

public class AllOrderedEntitiesAwareJavaGenerator extends AbstractAllOrderedEntitiesAwareGenerator {

	protected String getOutputSubFolder() {
		return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
	}

}
