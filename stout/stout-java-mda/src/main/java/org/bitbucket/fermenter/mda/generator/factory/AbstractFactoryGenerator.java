package org.bitbucket.fermenter.mda.generator.factory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.JavaElementUtils;
import org.bitbucket.fermenter.mda.JavaService;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Service;

public abstract class AbstractFactoryGenerator extends AbstractGenerator {

	public void generate(GenerationContext context) throws GenerationException {
		String currentApplication = context.getArtifactId();
		
		VelocityContext vc = new VelocityContext();
		vc.put("prefix", context.getBasePackage());
		vc.put("basePackage", context.getBasePackage());
		
		Collection<Entity> entities = MetadataRepository.getInstance().getAllEntities(currentApplication).values();
		vc.put("entities", entities);

		Collection<String> serviceNames = MetadataRepository.getInstance().getAllServices(currentApplication).keySet();
		vc.put("serviceNames", serviceNames);
		
		Collection<Service> services = MetadataRepository.getInstance().getAllServices(currentApplication).values();
		Collection<Service> javaServices = new HashSet<Service>();
		for (Iterator<Service> i = services.iterator(); i.hasNext();) {
			Service service = (Service) i.next();
			javaServices.add(new JavaService(service));
		}
		vc.put("services", javaServices);
		vc.put("baseJndiName", JavaElementUtils.getBaseJndiName(context.getBasePackage()));
		
		vc.put("pathPrefix", context.getBasePackageAsPath());
		
		String fileName = context.getOutputFile();
		fileName = replaceBasePackage(fileName, context.getBasePackageAsPath());
		fileName = replaceProjectName(fileName, context.getProjectName());
		context.setOutputFile(fileName);
		
		generateFile(context, vc);
	}

}