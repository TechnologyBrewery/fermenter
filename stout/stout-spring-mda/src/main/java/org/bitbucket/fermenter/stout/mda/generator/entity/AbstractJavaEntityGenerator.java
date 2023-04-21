package org.technologybrewery.fermenter.stout.mda.generator.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;
import org.technologybrewery.fermenter.mda.generator.entity.AbstractEntityGenerator;
import org.technologybrewery.fermenter.mda.metamodel.element.Entity;
import org.technologybrewery.fermenter.stout.mda.JavaEntity;
import org.technologybrewery.fermenter.stout.mda.java.JavaGeneratorUtil;

public abstract class AbstractJavaEntityGenerator extends AbstractEntityGenerator {

	protected void populateVelocityContext(VelocityContext vc, Entity entity, GenerationContext generationContext) {
		JavaEntity javaEntity = new JavaEntity(entity);
		vc.put("entity", javaEntity);
		vc.put("basePackage", generationContext.getBasePackage());
		vc.put("StringUtils", StringUtils.class);
		vc.put("templateName", generationContext.getTemplateName());
	}
	
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
    }

}
