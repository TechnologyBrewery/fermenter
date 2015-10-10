package org.bitbucket.fermenter.stout.mda.generator.service;

import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.stout.mda.JavaOperation;

public class PrimitiveServiceResponseJavaGenerator extends
		AbstractServiceResponseJavaGenerator {

	protected boolean shouldGenerate(String currentApplication, JavaOperation operation) {
		String returnType = operation.getReturnType();
		return (!operation.isReturnTypeCollection() && (!MetadataRepository.getInstance().getAllEntities().containsKey(returnType)));
	}

}
