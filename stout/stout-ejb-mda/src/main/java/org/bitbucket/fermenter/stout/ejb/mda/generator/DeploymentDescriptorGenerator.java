package org.bitbucket.fermenter.stout.ejb.mda.generator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractResourcesGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Service;
import org.bitbucket.fermenter.stout.mda.JavaElementUtils;
import org.bitbucket.fermenter.stout.mda.JavaOperation;
import org.bitbucket.fermenter.stout.mda.JavaService;

public class DeploymentDescriptorGenerator extends AbstractResourcesGenerator {

	public void generate(GenerationContext context) throws GenerationException {
		String applicationName = context.getArtifactId();
		VelocityContext vc = getNewVelocityContext(context);
		vc.put("prefix", context.getBasePackage());
		vc.put("basePackagePath", context.getBasePackage().replace('.','/'));

		Service service;
		JavaService javaService;
		Boolean j2eeBeansExist = Boolean.FALSE;
		Boolean asyncOperationsExist = Boolean.FALSE;
		
		//get the correct set of services to introspect:
		Collection services = Collections.EMPTY_LIST; 		
		Map allServices = MetadataRepository.getInstance().getAllServices(applicationName);
		services = (allServices != null) ? allServices.values() :  Collections.EMPTY_LIST;
		
		Collection javaServices = new HashSet();
		for (Iterator i = services.iterator(); i.hasNext();) {			
			service = (Service) i.next();			
			javaService = new JavaService(service);
			javaServices.add(javaService);
			if (j2eeBeansExist.booleanValue()) { 
				asyncOperationsExist = hasAsyncOperations(javaService);
				if (asyncOperationsExist.booleanValue()) {
					j2eeBeansExist = Boolean.TRUE;
				}
			}
		}
		vc.put("services", javaServices);
		vc.put("baseJndiName", JavaElementUtils.getBaseJndiName(context.getBasePackage()));		
		vc.put("asyncOperationsExist", asyncOperationsExist);
		
		//this is a bit of a work-around for now:
		String ejbJarName = getEjbJarName((String)vc.get(ARTIFACT_ID), (String)vc.get(VERSION));
		vc.put("ejb-jar-name", ejbJarName);
		
		String fileName = context.getOutputFile();
		fileName = replaceBasePackage(fileName, context.getBasePackageAsPath());
		fileName = replaceProjectName(fileName, context.getProjectName());
		context.setOutputFile(fileName);
		
		generateFile(context, vc);
	}

	/**
	 * Determines if any operation on a servicer have asynchronous operations
	 * @param javaService The service to introspect
	 * @return Boolean
	 */
	private Boolean hasAsyncOperations(JavaService javaService) {
		Boolean asyncOperationsExist = Boolean.FALSE;
		
		JavaOperation op;
		Map opMap;
		Iterator opIterator;
		//check operations for async behavior:
		opMap = javaService.getOperations();			
		if (opMap != null) {
			opIterator = opMap.values().iterator();
			while (opIterator.hasNext()) {
				op = (JavaOperation)opIterator.next();
				if (op.isAsynchronous().booleanValue()) {
					
					asyncOperationsExist = Boolean.TRUE;
					break;
				}
			}
		}
		
		return asyncOperationsExist;
	}
	
	private String getEjbJarName(String artifactId, String version) {
		String ejbJarName = null;
		String baseName = null;
		
		baseName = artifactId;
		
		ejbJarName = baseName + "-ejb-" + version;	
		
		return ejbJarName;
	}

}
