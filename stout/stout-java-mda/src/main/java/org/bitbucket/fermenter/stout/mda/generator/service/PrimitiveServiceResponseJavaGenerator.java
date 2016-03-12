package org.bitbucket.fermenter.stout.mda.generator.service;

import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.stout.mda.JavaOperation;

public class PrimitiveServiceResponseJavaGenerator extends
		AbstractServiceResponseJavaGenerator {

	protected boolean shouldGenerate(String currentApplication, JavaOperation operation) {
		String returnType = operation.getReturnType();
		MetadataRepository metadataRepository = 
                MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		return (!operation.isReturnTypeCollection() && (!metadataRepository.getAllEntities().containsKey(returnType)));
	}

}
