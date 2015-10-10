package org.bitbucket.fermenter.mda.generator.html;

import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractResourcesGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Enumeration;

public class EnumerationHtmlGenerator extends AbstractResourcesGenerator {

	public void generate(GenerationContext context) throws GenerationException {
		String applicationName = context.getArtifactId();
		Iterator enumerationIterator = MetadataRepository.getInstance().getAllEnumerations(applicationName).values().iterator();
		
		Enumeration enumeration;
		VelocityContext vc;
		String fileName;
		String basefileName = context.getOutputFile();
		
		while (enumerationIterator.hasNext()) {
			enumeration = (Enumeration) enumerationIterator.next();
			
			vc = new VelocityContext();
			vc.put("enumeration", enumeration);
						
			fileName = replaceEnumerationName(basefileName, enumeration.getName());
			context.setOutputFile(fileName);
			
			generateFile(context, vc);
		}
	}

}
