package org.bitbucket.fermenter.mda.generator.service;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.JavaEntity;
import org.bitbucket.fermenter.mda.JavaOperation;
import org.bitbucket.fermenter.mda.JavaService;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;

public class EntityServiceResponseJavaGenerator extends AbstractServiceResponseJavaGenerator {

	protected boolean shouldGenerate(String currentApplication, JavaOperation operation) {
		return  isSingleEntityResponse(currentApplication, operation);
	}

	/**
	 * @see org.bitbucket.fermenter.mda.codegen.generator.service.AbstractServiceResponseJavaGenerator#setupContext(org.bitbucket.fermenter.mda.codegen.generator.GenerationContext, org.bitbucket.fermenter.mda.codegen.element.java.JavaService, org.bitbucket.fermenter.mda.codegen.element.java.JavaOperation)
	 */
	protected VelocityContext setupContext(GenerationContext context, JavaService javaService, JavaOperation javaOperation) {	
		String applicationName = context.getArtifactId();
		 VelocityContext vc = super.setupContext(context, javaService, javaOperation);		 
		 
		 //this template is also used to generate responses for entities from the 
		 //entity maintenance server, so the properties are a little more abstract:
		 String responseName = javaOperation.getName();
		 vc.put("responseName", responseName);
		 vc.put("uncapitalizedResponseName", StringUtils.uncapitalize(responseName));
		 String entityType = javaOperation.getReturnType();
		 vc.put("entityType", entityType);
		 Entity entity = (Entity)MetadataRepository.getInstance().getAllEntities(applicationName).get(entityType);
		 JavaEntity javaEntity = new JavaEntity(entity);
		 vc.put("entity", javaEntity);
		 vc.put(EntityMaintenanceServiceResponseJavaGenerator.SERVICE_RESPONSE_QUALIFIER, "");
		 
		 return vc;
	}
	
	

}
