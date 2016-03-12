package org.bitbucket.fermenter.cider.mda.generator.enumeration;

import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.cider.mda.element.objectivec.ObjectiveCEnumeration;
import org.bitbucket.fermenter.cider.mda.generator.AbstractObjectiveCGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.mda.metadata.element.Enumeration;

/**
 * Provides enumeration generation support for Objective-C.
 */
public class ObjectiveCEnumerationGenerator extends AbstractObjectiveCGenerator {

	@Override
	public void generate(GenerationContext context) throws GenerationException {
		@SuppressWarnings("unchecked")
		MetadataRepository repo = MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		Iterator<Enumeration> enumerations = repo.getAllEnumerations().values().iterator();

		String fileName;
		String basefileName = context.getOutputFile();
		basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
		while (enumerations.hasNext()) {
			Enumeration enumeration = enumerations.next();
			ObjectiveCEnumeration objectiveCEnumeration = new ObjectiveCEnumeration(enumeration);
			VelocityContext vc = new VelocityContext();
			vc.put("projectName", OBJECTIVE_C_PROJECT_NAME);
			vc.put("enumeration", objectiveCEnumeration);
			fileName = replaceEnumerationName(basefileName, objectiveCEnumeration.getName());
			context.setOutputFile(fileName);
			generateFile(context, vc);
		}
	}

	@Override
	protected String getOutputSubFolder() {
		return super.getOutputSubFolder() + "enumerations/";
	}
}
