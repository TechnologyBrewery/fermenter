package org.tigris.atlas.mda.objectivec.generator.entity;

import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.generator.entity.AbstractEntityGenerator;
import org.tigris.atlas.mda.metadata.element.Entity;

/**
 * Provides entity generation fields for Objective-C purposes.
 */
public class EntityObjectiveCGenerator extends AbstractEntityGenerator {
	
	protected void populateVelocityContext(VelocityContext vc, Entity entity, GenerationContext generationContext) {
		
		//NOTE: In java, we wrap the entity with a decorator class and then pass that in everywhere rather than the 
		//base entity.  It helps simplify dealing with things like simple types - e.g., MDA "string" -> Objective-C 
		//"NSString" so your templates can stay pretty clean.  Probably a good idea here too, but certainly your call.
		//You'll end up having to wrap a number of classes, but you can probably copy over the Java hierarchy and just 
		//tweak it from there.  See the fermenter-java-mda project's org.tigris.atlas.mda.element.java for that route.
		
		vc.put("entity", entity);
		vc.put("basePackage", generationContext.getBasePackage());

		// the attributes below should be indexed via the objects above in the future:
		vc.put("prefix", generationContext.getBasePackage());
		vc.put("entityName", entity.getName());
		vc.put("useOptimisticLocking", new Boolean(entity.useOptimisticLocking()));
		vc.put("table", entity.getTable());
		vc.put("idFields", entity.getIdFields().values());
		vc.put("fields", entity.getFields().values());
		vc.put("composites", entity.getComposites().values());
		vc.put("relations", entity.getRelations().values());
		vc.put("inverseRelations", entity.getInverseRelations().values());
		vc.put("references", entity.getReferences().values());
		vc.put("parent", entity.getParent());
		vc.put("extends", entity.getSuperclass());
		vc.put("queries", entity.getQueries().values());

	}
	
	/**
	 * Controls where in the "src/main|generated|test/<outputSubFolder>" will exist.  No idea what this should 
	 * be for objective-c.
	 * 
	 * {@inheritDoc}
	 */
	protected String getOutputSubFolder() {
		return "update-me/";
	}	
	

}
