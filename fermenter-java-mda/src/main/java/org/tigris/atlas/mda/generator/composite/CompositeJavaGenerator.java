package org.tigris.atlas.mda.generator.composite;

import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.element.java.JavaComposite;
import org.tigris.atlas.mda.generator.AbstractJavaGenerator;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.generator.GenerationException;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Composite;

public class CompositeJavaGenerator extends AbstractJavaGenerator {

	public void generate(GenerationContext context) throws GenerationException {
		String currentApplication = context.getArtifactId();
		Iterator i = MetadataRepository.getInstance().getAllComposites(currentApplication).values().iterator();
		
		Composite metadata = null;
		JavaComposite composite = null;
		
		String basefileName = context.getOutputFile();		
		basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
		while (i.hasNext()) {
			metadata = (Composite) i.next();
			composite = new JavaComposite(metadata);
			
			VelocityContext vc = new VelocityContext();
			vc.put("composite", composite);
			vc.put("basePackage", context.getBasePackage());
			
			String fileName = context.getOutputFile();
			fileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
			fileName = replaceCompositeName(basefileName, composite.getType());
			context.setOutputFile(fileName);
			
			generateFile(context, vc);
		}
	}

}
