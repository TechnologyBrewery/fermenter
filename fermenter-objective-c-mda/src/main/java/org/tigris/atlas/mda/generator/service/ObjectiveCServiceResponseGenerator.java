package org.tigris.atlas.mda.generator.service;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.element.objectivec.ObjectiveCOperation;
import org.tigris.atlas.mda.element.objectivec.ObjectiveCService;
import org.tigris.atlas.mda.generator.AbstractObjectiveCGenerator;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.generator.GenerationException;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Operation;
import org.tigris.atlas.mda.metadata.element.Service;

/**
 *  Iterates through service instances, passing a {@link Operation} instance to the templates.
 */
public class ObjectiveCServiceResponseGenerator extends AbstractObjectiveCGenerator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generate(GenerationContext context) throws GenerationException {
		Collection<Service> services = MetadataRepository.getInstance().getAllServices().values();

		String originalFileName = context.getOutputFile();

		for (Service service : services) {
			ObjectiveCService objectiveCService = new ObjectiveCService(service);
			for (Operation op : objectiveCService.getOperations().values()) {
				ObjectiveCOperation operation = (ObjectiveCOperation)op;
				context.setOutputFile(StringUtils.replace(originalFileName, "${operationName}", operation.getName()));
				VelocityContext vc = new VelocityContext();
				vc.put("projectName", OBJECTIVE_C_PROJECT_NAME);
				vc.put("operation", operation);
				generateFile(context, vc);
			}
		}
	}

	@Override
	protected String getOutputSubFolder() {
		return super.getOutputSubFolder() + "service-responses/";
	}

}
