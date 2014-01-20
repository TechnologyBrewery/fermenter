package org.tigris.atlas.mda.generator.form;


import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.element.java.JavaForm;
import org.tigris.atlas.mda.generator.AbstractJavaGenerator;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.generator.GenerationException;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Form;

public class FormJavaGenerator extends AbstractJavaGenerator {

	public void generate(GenerationContext context) throws GenerationException {
		// Iterate over this application's forms only
		//String applicationName = context.getArtifactId();
		Iterator i = MetadataRepository.getInstance().getAllForms().values().iterator();
		
		Form metadata = null;
		JavaForm form = null;
		
		String basefileName = context.getOutputFile();		
		basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
		while (i.hasNext()) {
			metadata = (Form) i.next();
			form = new JavaForm(metadata);
			
			VelocityContext vc = new VelocityContext();
			vc.put("form", form);
			vc.put("basePackage", context.getBasePackage());
			
			String fileName = context.getOutputFile();
			fileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
			fileName = replaceFormName(basefileName, form.getName());
			context.setOutputFile(fileName);
			
			generateFile(context, vc);
		}
	}

}
