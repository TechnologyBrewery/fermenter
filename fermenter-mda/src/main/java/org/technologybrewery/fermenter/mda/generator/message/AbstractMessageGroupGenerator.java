package org.technologybrewery.fermenter.mda.generator.message;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.technologybrewery.fermenter.mda.generator.AbstractGenerator;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;
import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.technologybrewery.fermenter.mda.metamodel.element.MessageGroup;

/**
 * Iterates through each message group in the meta-model and enables the
 * generation of a single file for each.
 */
public abstract class AbstractMessageGroupGenerator extends AbstractGenerator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generate(GenerationContext context) {
		DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
				.getMetamodelRepository(DefaultModelInstanceRepository.class);

		Map<String, MessageGroup> messageGroups = metadataRepository.getMessageGroupsByContext(metadataContext);

		String fileName;
		String baseFileName = context.getOutputFile();
		baseFileName = replaceBasePackage(baseFileName, context.getBasePackageAsPath());

		for (MessageGroup messageGroup : messageGroups.values()) {
			VelocityContext vc = new VelocityContext();
			populateVelocityContext(vc, messageGroup, context);

			fileName = replaceMessageGroupName(baseFileName, messageGroup.getName());
			context.setOutputFile(fileName);

			generateFile(context, vc);
		}
	}

	/**
	 * Enables subclasses to add any additional metadata to the provided
	 * {@link VelocityContext} to facilitate the generation of the provided
	 * {@link MessageGroup}.
	 * 
	 * @param vc                context
	 * @param messageGroup      message group
	 * @param generationContext generation context
	 */
	protected abstract void populateVelocityContext(VelocityContext vc, MessageGroup messageGroup,
			GenerationContext generationContext);

}
