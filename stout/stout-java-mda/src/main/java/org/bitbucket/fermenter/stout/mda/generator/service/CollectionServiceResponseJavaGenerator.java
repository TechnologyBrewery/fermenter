package org.bitbucket.fermenter.stout.mda.generator.service;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.stout.mda.JavaEntity;
import org.bitbucket.fermenter.stout.mda.JavaOperation;
import org.bitbucket.fermenter.stout.mda.JavaService;

public class CollectionServiceResponseJavaGenerator extends
		AbstractServiceResponseJavaGenerator {

	protected boolean shouldGenerate(String currentApplication, JavaOperation operation) {
		boolean shouldGenerate = false;
		if (operation.isReturnTypeCollection()) {
			String returnType = operation.getReturnManyType();
			MetadataRepository metadataRepository = 
	                MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
			shouldGenerate = metadataRepository.getAllEntities(currentApplication).containsKey(returnType);
		}
		return shouldGenerate;
	}

	/**
	 * @see org.bitbucket.fermenter.stout.mda.codegen.generator.service.AbstractServiceResponseJavaGenerator#setupContext(org.bitbucket.fermenter.stout.mda.codegen.generator.GenerationContext, org.bitbucket.fermenter.stout.mda.codegen.element.java.JavaService, org.bitbucket.fermenter.stout.mda.codegen.element.java.JavaOperation)
	 */
	protected VelocityContext setupContext(GenerationContext context, JavaService javaService, JavaOperation javaOperation) {		
		 VelocityContext vc = super.setupContext(context, javaService, javaOperation);		 
		 
		 //this template is also used to generate responses for entities from the 
		 //entity maintenance server, so the properties are a little more abstract:
		 String responseName = javaOperation.getName();
		 vc.put("responseName", responseName);
		 vc.put("uncapitalizedResponseName", StringUtils.uncapitalize(responseName));
		 String entityType = javaOperation.getReturnManyType();
		 vc.put("entityType", entityType);
		 MetadataRepository metadataRepository = 
	                MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		 Entity entity = (Entity)metadataRepository.getAllEntities().get(entityType);
		 JavaEntity javaEntity = new JavaEntity(entity);
		 vc.put("entity", javaEntity);
		 
		 return vc;
	}
	
}
