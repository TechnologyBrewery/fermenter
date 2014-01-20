package org.tigris.atlas.mda.generator.service;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.element.java.JavaEntity;
import org.tigris.atlas.mda.element.java.JavaOperation;
import org.tigris.atlas.mda.element.java.JavaService;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Entity;

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
	 * @see org.tigris.atlas.mda.codegen.generator.service.AbstractServiceResponseJavaGenerator#setupContext(org.tigris.atlas.mda.codegen.generator.GenerationContext, org.tigris.atlas.mda.codegen.element.java.JavaService, org.tigris.atlas.mda.codegen.element.java.JavaOperation)
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
