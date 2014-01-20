package org.tigris.atlas.mda.generator.enumeration;

import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.element.java.JavaEnumeration;
import org.tigris.atlas.mda.generator.AbstractJavaGenerator;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.generator.GenerationException;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Enumeration;

public class EnumerationJavaGenerator extends AbstractJavaGenerator {

	public void generate(GenerationContext context) throws GenerationException {
		String applicationName = context.getArtifactId();
		Iterator enumerationIterator = MetadataRepository.getInstance().getAllEnumerations(applicationName).values().iterator();
		
		Enumeration enumeration;
		JavaEnumeration javaEnumeration;
		VelocityContext vc;
		String fileName;
		String basefileName = context.getOutputFile();
		basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
		
		while (enumerationIterator.hasNext()) {
			enumeration = (Enumeration) enumerationIterator.next();
			javaEnumeration = new JavaEnumeration(enumeration);
			
			vc = new VelocityContext();
			vc.put("enumeration", javaEnumeration);
			vc.put("basePackage", context.getBasePackage());
						
			fileName = replaceEnumerationName(basefileName, enumeration.getName());
			context.setOutputFile(fileName);
			
			generateFile(context, vc);
		}
	}

}
