package org.bitbucket.fermenter.stout.mda.generator.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.entity.AbstractEntityGenerator;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.stout.mda.JavaEntity;
import org.bitbucket.fermenter.stout.mda.java.JavaGeneratorUtil;

public abstract class AbstractJavaEntityGenerator extends AbstractEntityGenerator {

	protected void populateVelocityContext(VelocityContext vc, Entity entity, GenerationContext generationContext) {
		JavaEntity javaEntity = new JavaEntity(entity);
		vc.put("entity", javaEntity);
		vc.put("basePackage", generationContext.getBasePackage());
		vc.put("StringUtils", StringUtils.class);
	}
	
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
    }

}