package org.tigris.atlas.mda.generator.service;

import org.tigris.atlas.mda.element.java.JavaOperation;
import org.tigris.atlas.mda.metadata.MetadataRepository;

public class PrimitiveServiceResponseJavaGenerator extends
		AbstractServiceResponseJavaGenerator {

	protected boolean shouldGenerate(String currentApplication, JavaOperation operation) {
		String returnType = operation.getReturnType();
		return (!operation.isReturnTypeCollection() && (!MetadataRepository.getInstance().getAllEntities().containsKey(returnType)));
	}

}
