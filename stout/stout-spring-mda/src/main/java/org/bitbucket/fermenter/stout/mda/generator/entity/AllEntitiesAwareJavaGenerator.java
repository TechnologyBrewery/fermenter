package org.bitbucket.fermenter.stout.mda.generator.entity;

import org.bitbucket.fermenter.mda.generator.entity.AbstractAllEntitiesAwareGenerator;
import org.bitbucket.fermenter.stout.mda.java.JavaGeneratorUtil;

public class AllEntitiesAwareJavaGenerator extends AbstractAllEntitiesAwareGenerator {

	protected String getOutputSubFolder() {
		return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
	}

}
