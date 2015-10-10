package org.bitbucket.fermenter.mda.generator.service;

import org.bitbucket.fermenter.mda.JavaOperation;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;

public class PrimitiveServiceResponseJavaGenerator extends
		AbstractServiceResponseJavaGenerator {

	protected boolean shouldGenerate(String currentApplication, JavaOperation operation) {
		String returnType = operation.getReturnType();
		return (!operation.isReturnTypeCollection() && (!MetadataRepository.getInstance().getAllEntities().containsKey(returnType)));
	}

}
