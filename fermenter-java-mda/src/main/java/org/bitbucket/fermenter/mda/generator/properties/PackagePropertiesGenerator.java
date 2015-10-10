package org.bitbucket.fermenter.mda.generator.properties;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractResourcesGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;


public class PackagePropertiesGenerator extends AbstractResourcesGenerator {

	public void generate(GenerationContext context) throws GenerationException {
		VelocityContext vc = new VelocityContext();
		vc.put("basePackage", context.getBasePackage());	
		
		generateFile(context, vc);
	}

}
