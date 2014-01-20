package org.tigris.atlas.mda.generator.html;

import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.generator.AbstractResourcesGenerator;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.generator.GenerationException;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Enumeration;

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
