package org.tigris.atlas.mda.generator.entity;

import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.element.java.JavaEntity;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.metadata.element.Entity;

public abstract class AbstractJavaEntityGenerator extends AbstractEntityGenerator {
	
	protected void populateVelocityContext(VelocityContext vc, Entity entity, GenerationContext generationContext) {
		JavaEntity javaEntity = new JavaEntity(entity);
		vc.put("entity", javaEntity);
		vc.put("basePackage", generationContext.getBasePackage());
		
		//the attributes below should be indexed via the objects above in the future:
		vc.put( "prefix", generationContext.getBasePackage() );
		vc.put( "entityName", javaEntity.getName() );
		vc.put( "useOptimisticLocking", new Boolean(javaEntity.useOptimisticLocking()));
		vc.put( "imports", javaEntity.getImports());
		vc.put( "table", javaEntity.getTable() );
		vc.put( "idFields", javaEntity.getIdFields().values() );
		vc.put( "fields", javaEntity.getFields().values() );
		vc.put( "composites", javaEntity.getComposites().values() );
		vc.put( "relations", javaEntity.getRelations().values() );
		vc.put( "inverseRelations", javaEntity.getInverseRelations().values() );
		vc.put( "references", javaEntity.getReferences().values() );
		vc.put( "parent", javaEntity.getParent() );
		vc.put( "extends", javaEntity.getSuperclass() );
        vc.put( "queries", javaEntity.getQueries().values() );
    
	}

}