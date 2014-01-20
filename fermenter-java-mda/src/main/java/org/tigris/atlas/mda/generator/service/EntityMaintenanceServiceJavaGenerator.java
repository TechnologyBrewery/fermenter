package org.tigris.atlas.mda.generator.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.element.java.JavaElementUtils;
import org.tigris.atlas.mda.element.java.JavaEntity;
import org.tigris.atlas.mda.generator.AbstractJavaGenerator;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.generator.GenerationException;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Entity;

public class EntityMaintenanceServiceJavaGenerator extends AbstractJavaGenerator {

	private static final String ENTITY_MAINTENANCE = "EntityMaintenance";

	public void generate(GenerationContext context) throws GenerationException {
		String applicationName = context.getArtifactId();
		Iterator entityIterator = MetadataRepository.getInstance().getAllEntities(applicationName).values().iterator();
		
		JavaEntity javaEntity;
		Collection javaEntities = new ArrayList();
		while (entityIterator.hasNext()) {
			javaEntity = new JavaEntity((Entity)entityIterator.next());
			javaEntities.add(javaEntity);
		}
		
		VelocityContext vc = new VelocityContext();
		vc.put("serviceName", ENTITY_MAINTENANCE);
		vc.put("service.name", ENTITY_MAINTENANCE);
		vc.put("basePackage", context.getBasePackage());
		vc.put("entities", javaEntities);
		vc.put("baseJndiName", JavaElementUtils.getBaseJndiName(context.getBasePackage()));
		vc.put("artifactId", context.getArtifactId());
		vc.put("version", context.getVersion());
					
		String basefileName = context.getOutputFile();
		basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
		String fileName = replaceServiceName(basefileName, ENTITY_MAINTENANCE);
		context.setOutputFile(fileName);
		
		generateFile(context, vc);
	}

}
