package org.bitbucket.fermenter.stout.mda.generator.properties;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractResourcesGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MessagesMetadataManager;
import org.bitbucket.fermenter.mda.metadata.element.Message;

public class MessageResourceGenerator extends AbstractResourcesGenerator {

	private static final Log LOG = LogFactory.getLog(MessageResourceGenerator.class);
	
	public void generate(GenerationContext context) throws GenerationException {
		
		Collection<String> locales = MessagesMetadataManager.getInstance().getAllLocales();
		String baseFileName = context.getOutputFile();
		for (String locale : locales) {
			String fileName = null;
			if (locale.equals(Message.DEFAULT_LOCALE)) {
				fileName = StringUtils.replace(baseFileName, "${locale}", "");
			} else {
				fileName = StringUtils.replace(baseFileName, "${locale}", "_" + locale);
			}
			fileName = replaceBasePackage(fileName, context.getBasePackageAsPath());
			
			LOG.debug("Writing properties for locale " + locale + " to file " + fileName);
			
			context.setOutputFile(fileName);

			VelocityContext vc = new VelocityContext();
			vc.put("locale", locale);
			vc.put("messages", MessagesMetadataManager.getInstance().getAllMessages());
			generateFile(context, vc);
		}
	}

}
