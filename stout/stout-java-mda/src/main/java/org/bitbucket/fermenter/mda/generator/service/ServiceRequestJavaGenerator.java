package org.bitbucket.fermenter.mda.generator.service;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.JavaOperation;
import org.bitbucket.fermenter.mda.JavaService;
import org.bitbucket.fermenter.mda.generator.AbstractJavaGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Service;

public class ServiceRequestJavaGenerator extends AbstractJavaGenerator {

	private static final String TAG_OPERATION_NAME = "${operationName}";

	/**
	 * @see org.bitbucket.fermenter.mda.codegen.generator.Generator#generate(org.bitbucket.fermenter.mda.codegen.generator.GenerationContext)
	 */
	public void generate(GenerationContext context) throws GenerationException {
		String applicationName = context.getArtifactId();
		Iterator serviceIterator = MetadataRepository.getInstance().getAllServices(applicationName).values().iterator();
		
		Service service;
		JavaService javaService;
		JavaOperation javaOperation;
		VelocityContext vc;
		String basefileName = context.getOutputFile();
		Iterator operationIterator;
		basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
		
		while (serviceIterator.hasNext()) {
			service = (Service) serviceIterator.next();
			javaService = new JavaService(service);
			
			operationIterator = javaService.getOperations().values().iterator();
			while(operationIterator.hasNext()) {
				javaOperation = (JavaOperation)operationIterator.next();
				
				if ( shouldGenerate( javaService, javaOperation) ) {
					vc = setupContext(context, javaOperation);
					context.setOutputFile( replaceOperationName(basefileName, javaOperation.getName()) );
					
					generateFile(context, vc);
					
				}				
			}
		}
	}
	
	protected final String replaceOperationName(String original, String operationName) {
		return StringUtils.replace(original, TAG_OPERATION_NAME, operationName);
	}
	
	/**
	 * Determine if this operation should result in the generation of a service response
	 * @param operation The operation in question
	 * @return the result
	 */
	private boolean shouldGenerate(JavaService service, JavaOperation operation) {
		return ( operation.getParameters().size() > 0 ) ;
		
	}

	/**
	 * Provides an opportunity to add specific response data into the context
	 * @param context
	 * @param javaService
	 * @param javaOperation
	 * @return
	 */
	protected VelocityContext setupContext(GenerationContext context, JavaOperation javaOperation) {
		VelocityContext vc;
		vc = new VelocityContext();
		vc.put("operation", javaOperation);
		vc.put("basePackage", context.getBasePackage());
		return vc;
	}

}