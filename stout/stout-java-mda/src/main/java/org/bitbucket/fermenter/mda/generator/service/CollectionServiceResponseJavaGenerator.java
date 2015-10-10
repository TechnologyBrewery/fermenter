package org.bitbucket.fermenter.mda.generator.service;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.JavaEntity;
import org.bitbucket.fermenter.mda.JavaOperation;
import org.bitbucket.fermenter.mda.JavaService;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;

public class CollectionServiceResponseJavaGenerator extends
		AbstractServiceResponseJavaGenerator {

	protected boolean shouldGenerate(String currentApplication, JavaOperation operation) {
		boolean shouldGenerate = false;
		if (operation.isReturnTypeCollection()) {
			String returnType = operation.getReturnManyType();
			shouldGenerate = MetadataRepository.getInstance().getAllEntities(currentApplication).containsKey(returnType);
		}
		return shouldGenerate;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.codegen.generator.service.AbstractServiceResponseJavaGenerator#setupContext(org.bitbucket.fermenter.mda.codegen.generator.GenerationContext, org.bitbucket.fermenter.mda.codegen.element.java.JavaService, org.bitbucket.fermenter.mda.codegen.element.java.JavaOperation)
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
		 Entity entity = (Entity)MetadataRepository.getInstance().getAllEntities().get(entityType);
		 JavaEntity javaEntity = new JavaEntity(entity);
		 vc.put("entity", javaEntity);
		 
		 return vc;
	}
	
}
