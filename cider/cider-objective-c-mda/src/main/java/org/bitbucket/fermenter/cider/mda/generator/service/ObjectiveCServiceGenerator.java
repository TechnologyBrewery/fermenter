package org.bitbucket.fermenter.cider.mda.generator.service;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.cider.mda.element.objectivec.ObjectiveCService;
import org.bitbucket.fermenter.cider.mda.generator.AbstractObjectiveCGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Service;

/**
 *  Iterates through service instances, passing a {@link ObjectiveCService} instance to the templates.
 */
public class ObjectiveCServiceGenerator extends AbstractObjectiveCGenerator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generate(GenerationContext context) throws GenerationException {
		Collection<Service> services = MetadataRepository.getInstance().getAllServices().values();

		String originalFileName = context.getOutputFile();

		for (Service service : services) {
			ObjectiveCService objectiveCService = new ObjectiveCService(service);

			context.setOutputFile(StringUtils.replace(originalFileName, "${serviceName}", objectiveCService.getName()));

			VelocityContext vc = new VelocityContext();
			vc.put("projectName", OBJECTIVE_C_PROJECT_NAME);
			vc.put("service", objectiveCService);
			vc.put("basePackage", context.getBasePackage());
			vc.put("artifactId", context.getArtifactId());
			vc.put("version", context.getVersion());

			generateFile(context, vc);
		}
	}

	@Override
	protected String getOutputSubFolder() {
		return super.getOutputSubFolder() + "services/";
	}

}
