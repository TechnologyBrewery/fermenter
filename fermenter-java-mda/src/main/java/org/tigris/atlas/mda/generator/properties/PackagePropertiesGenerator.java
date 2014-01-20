package org.tigris.atlas.mda.generator.properties;

import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.generator.AbstractResourcesGenerator;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.generator.GenerationException;


public class PackagePropertiesGenerator extends AbstractResourcesGenerator {

	public void generate(GenerationContext context) throws GenerationException {
		VelocityContext vc = new VelocityContext();
		vc.put("basePackage", context.getBasePackage());	
		
		generateFile(context, vc);
	}

}
