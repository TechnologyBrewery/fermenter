package org.tigris.atlas.mda.generator.entity;

import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.element.objectivec.ObjectiveCEntity;
import org.tigris.atlas.mda.generator.AbstractObjectiveCEntityGenerator;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.generator.GenerationException;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Entity;

/**
 * Provides entity generation fields for Objective-C purposes.
 */
public class ObjectiveCEntityGenerator extends AbstractObjectiveCEntityGenerator {

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
			populateVelocityContext(vc, objectiveCEntity, context);
			fileName = replaceEntityName(basefileName, objectiveCEntity.getName());
			context.setOutputFile(fileName);
			generateFile(context, vc);
		}
	}

	protected void populateVelocityContext(VelocityContext vc, ObjectiveCEntity entity, GenerationContext generationContext) {
		vc.put("projectName", OBJECTIVE_C_PROJECT_NAME);
		vc.put("entityName", entity.getName());
		vc.put("idFields", entity.getIdFields().values());
		vc.put("fields", entity.getFields().values());
		vc.put("references", entity.getReferences().values());
		vc.put("imports", entity.getImports());
	}

	@Override
	protected void populateVelocityContext(VelocityContext vc, Entity entity, GenerationContext generationContext) {
	}
}
