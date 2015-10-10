package org.bitbucket.fermenter.mda.generator.entity;

import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.element.objectivec.ObjectiveCEntity;
import org.bitbucket.fermenter.mda.generator.AbstractObjectiveCGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;

/**
 * Provides entity generation support for Objective-C.
 *
 * This generator does not currently support:
 *     -tables
 *     -composites
 *     -inverse relations
 *     -queries
 */
public class ObjectiveCEntityGenerator extends AbstractObjectiveCGenerator {

	@Override
	public void generate(GenerationContext context) throws GenerationException {
		@SuppressWarnings("unchecked")
		Iterator<Entity> entities = MetadataRepository.getInstance().getAllEntities().values().iterator();

		String fileName;
		String basefileName = context.getOutputFile();
		basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
		while (entities.hasNext()) {
			ObjectiveCEntity objectiveCEntity = new ObjectiveCEntity(entities.next());
			VelocityContext vc = new VelocityContext();
			vc.put("lineSeparator", System.getProperty("line.separator"));
			vc.put("projectName", OBJECTIVE_C_PROJECT_NAME);
			vc.put("entityName", objectiveCEntity.getName());
			vc.put("imports", objectiveCEntity.getImports());
			vc.put("idFields", objectiveCEntity.getIdFields().values());
			vc.put("fields", objectiveCEntity.getFields().values());
			vc.put("references", objectiveCEntity.getReferences().values());
			vc.put("relations", objectiveCEntity.getRelations().values());
			fileName = replaceEntityName(basefileName, objectiveCEntity.getName());
			context.setOutputFile(fileName);
			generateFile(context, vc);
		}
	}

	@Override
	protected String getOutputSubFolder() {
		return super.getOutputSubFolder() + "entities/";
	}
}
