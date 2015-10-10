package org.bitbucket.fermenter.mda.generator.html;

import java.util.Collection;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractResourcesGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;

public class ServiceHtmlGenerator extends AbstractResourcesGenerator {

	public void generate(GenerationContext context) throws GenerationException {
		String applicationName = context.getArtifactId();
		VelocityContext vc = new VelocityContext();
		
		vc.put("name", context.getProjectName());
		
		Collection entityNames = MetadataRepository.getInstance().getAllEntities(applicationName).values();
		vc.put("entities", entityNames);
		
		Collection services = MetadataRepository.getInstance().getAllServices(applicationName).values();
		vc.put("services", services);
		
		String fileName = context.getOutputFile();
		context.setOutputFile(fileName);
		
		generateFile(context, vc);
	}

}
