package org.bitbucket.fermenter.stout.mda.generator.entity;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.entity.AbstractEntityGenerator;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.stout.mda.JavaEntity;

public abstract class AbstractJavaEntityGenerator extends AbstractEntityGenerator {

	protected void populateVelocityContext(VelocityContext vc, Entity entity, GenerationContext generationContext) {
		JavaEntity javaEntity = new JavaEntity(entity);
		vc.put("entity", javaEntity);
		vc.put("basePackage", generationContext.getBasePackage());

		// the attributes below should be indexed via the objects above in the future:
		vc.put("prefix", generationContext.getBasePackage());
		vc.put("entityName", javaEntity.getName());
		vc.put("useOptimisticLocking", new Boolean(javaEntity.useOptimisticLocking()));
		vc.put("imports", javaEntity.getImports());
		vc.put("table", javaEntity.getTable());
		vc.put("idFields", javaEntity.getIdFields().values());
		vc.put("fields", javaEntity.getFields().values());
		vc.put("composites", javaEntity.getComposites().values());
		vc.put("relations", javaEntity.getRelations().values());
		vc.put("inverseRelations", javaEntity.getInverseRelations().values());
		vc.put("references", javaEntity.getReferences().values());
		vc.put("parent", javaEntity.getParent());
		vc.put("extends", javaEntity.getSuperclass());
		vc.put("queries", javaEntity.getQueries().values());
		vc.put("uid", RandomUtils.nextLong());

	}

}