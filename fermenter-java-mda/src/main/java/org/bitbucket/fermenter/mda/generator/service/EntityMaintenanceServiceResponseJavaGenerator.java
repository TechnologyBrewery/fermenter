package org.bitbucket.fermenter.mda.generator.service;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.JavaEntity;
import org.bitbucket.fermenter.mda.generator.AbstractJavaGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;

public class EntityMaintenanceServiceResponseJavaGenerator extends AbstractJavaGenerator {

	/**
	 * This constant represents extra test to differentiate between entity maintenance
	 * service response types and entity service response types.
	 */
	public static final String SERVICE_RESPONSE_QUALIFIER = "serviceResponseQualifier";

	public void generate(GenerationContext context) throws GenerationException {
		String applicationName = context.getArtifactId();
		Iterator entities = MetadataRepository.getInstance().getAllEntities(applicationName).values().iterator();
		
		String fileName;
		String basefileName = context.getOutputFile();		
		basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
		while (entities.hasNext()) {
			Entity entity = (Entity) entities.next();
			JavaEntity javaEntity = new JavaEntity(entity);
			
			 //this template is also used to generate responses for entities from the 
			 //entity maintenance server, so the properties are a little more abstract:
			 VelocityContext vc = new VelocityContext();
			 String responseName = javaEntity.getName();
			 vc.put("responseName", responseName);
			 vc.put("entityType", responseName);
			 vc.put("basePackage", context.getBasePackage());			 			
			 vc.put("uncapitalizedResponseName", StringUtils.uncapitalize(responseName));
			 vc.put("entity", javaEntity);			 
			 vc.put(SERVICE_RESPONSE_QUALIFIER, "ServiceResponse");
			
			
			fileName = replaceEntityName(basefileName, entity.getName());
			context.setOutputFile(fileName);
			
			generateFile(context, vc);
		}
	}

}
