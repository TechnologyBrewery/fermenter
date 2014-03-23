package org.tigris.atlas.mda.generator.service;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.tigris.atlas.mda.element.java.JavaOperation;
import org.tigris.atlas.mda.element.java.JavaService;
import org.tigris.atlas.mda.generator.AbstractJavaGenerator;
import org.tigris.atlas.mda.generator.GenerationContext;
import org.tigris.atlas.mda.generator.GenerationException;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Operation;
import org.tigris.atlas.mda.metadata.element.Service;

public abstract class AbstractServiceResponseJavaGenerator extends AbstractJavaGenerator {

	private static final String TAG_RETURN_MANY_TYPE_NAME = "${returnManyTypeName}";
	protected static final String TAG_OPERATION_NAME = "${operationName}";

	/**
	 * @see org.tigris.atlas.mda.codegen.generator.Generator#generate(org.tigris.atlas.mda.codegen.generator.GenerationContext)
	 */
	public void generate(GenerationContext context) throws GenerationException {
		String applicationName = context.getArtifactId();
		Iterator<Service> serviceIterator = MetadataRepository.getInstance().getAllServices(applicationName).values().iterator();
		
		Service service;
		JavaService javaService;
		JavaOperation javaOperation;
		VelocityContext vc;
		String fileName;
		String basefileName = context.getOutputFile();
		Iterator<Operation> operationIterator;
		basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
		
		while (serviceIterator.hasNext()) {
			service = (Service) serviceIterator.next();
			javaService = new JavaService(service);
			
			
			operationIterator = javaService.getOperations().values().iterator();
			while(operationIterator.hasNext()) {
				javaOperation = (JavaOperation)operationIterator.next();
				
				if (shouldGenerate(applicationName, javaOperation)) {
					vc = setupContext(context, javaService, javaOperation);
					
					if (StringUtils.contains(basefileName, TAG_OPERATION_NAME)) {
						fileName = replaceOperationName(basefileName, javaOperation.getName());
					} else {
						fileName = replaceReturnManyTypeName(basefileName, javaOperation.getReturnManyType());
					}
					context.setOutputFile(fileName);
					
					generateFile(context, vc);
					
				}				
			}
		}
	}
	
	protected final String replaceOperationName(String original, String operationName) {
		return StringUtils.replace(original, TAG_OPERATION_NAME, operationName);
	}
	
	protected final String replaceReturnManyTypeName(String original, String returnManyTypeName) {
		return StringUtils.replace(original, TAG_RETURN_MANY_TYPE_NAME, returnManyTypeName);
	}
	
	/**
	 * Determine if this operation should result in the generation of a service response
	 * @param currentApplication The name of the current application.  We don't need to regenerate 
	 * responses that come from other applications
	 * @param operation The operation in question
	 * @return the result
	 */
	protected abstract boolean shouldGenerate(String currentApplication, JavaOperation operation);

	/**
	 * Provides an opportunity to add specific response data into the context
	 * @param context
	 * @param javaService
	 * @param javaOperation
	 * @return
	 */
	protected VelocityContext setupContext(GenerationContext context, JavaService javaService, JavaOperation javaOperation) {
		VelocityContext vc;
		vc = new VelocityContext();
		vc.put("service", javaService);
		vc.put("operation", javaOperation);
		vc.put("basePackage", context.getBasePackage());
		return vc;
	}

	protected boolean isSingleEntityResponse(String currentApplication, JavaOperation operation) {
		return (MetadataRepository.getInstance().getEntity(currentApplication, operation.getReturnType() ) != null);
	}

}
