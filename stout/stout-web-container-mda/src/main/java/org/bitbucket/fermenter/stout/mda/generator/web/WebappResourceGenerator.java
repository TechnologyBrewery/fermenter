package org.bitbucket.fermenter.stout.mda.generator.web;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractResourcesGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;

public class WebappResourceGenerator extends AbstractResourcesGenerator {

	@Override
	public void generate(GenerationContext context) throws GenerationException {
		VelocityContext vc = new VelocityContext();
		vc.put("basePackage", context.getBasePackage());
		generateFile(context, vc);
	}

	@Override
	protected String getOutputSubFolder() {
		return "webapp/";
	}

}
