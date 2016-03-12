package org.bitbucket.fermenter.stout.mda.generator.form;


import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.mda.metadata.element.Form;
import org.bitbucket.fermenter.stout.mda.JavaForm;
import org.bitbucket.fermenter.stout.mda.generator.AbstractJavaGenerator;

public class FormJavaGenerator extends AbstractJavaGenerator {

	public void generate(GenerationContext context) throws GenerationException {
		// Iterate over this application's forms only
		MetadataRepository metadataRepository = 
                MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		Iterator i = metadataRepository.getAllForms().values().iterator();
		
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
