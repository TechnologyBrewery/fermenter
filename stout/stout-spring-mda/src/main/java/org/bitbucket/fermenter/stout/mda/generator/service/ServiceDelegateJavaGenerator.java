package org.bitbucket.fermenter.stout.mda.generator.service;

import java.util.Set;
import java.util.TreeSet;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metadata.element.Service;
import org.bitbucket.fermenter.stout.mda.JavaService;

/**
 * Iterates through service instances, passing {@link JavaService}s instance to the templates.
 *
 */
public class ServiceDelegateJavaGenerator extends ServiceJavaGenerator {

    private static final String BIZOBJ_SUBPACKAGE = ".bizobj.";

    @Override
    protected void populateVelocityContext(VelocityContext vc, Service service, GenerationContext generationContext) {
        super.populateVelocityContext(vc, service, generationContext);
        JavaService javaService = (JavaService) vc.get(SERVICE);

        // migrate BO references to TO references - fix in FER-94:
        Set<String> delegateImports = new TreeSet<>();
        Set<String> serviceImports = javaService.getImports();
        for (String serviceImport : serviceImports) {
            if (serviceImport.contains(BIZOBJ_SUBPACKAGE)) {
                if (serviceImport.startsWith(generationContext.getBasePackage())) {
                    serviceImport = serviceImport.replace(BIZOBJ_SUBPACKAGE, ".transfer.");
                    serviceImport = serviceImport.substring(0, serviceImport.length() - "BO".length());
                    delegateImports.add(serviceImport);
                } else {
                    // bad import - do nothing
                }
            } else {
                delegateImports.add(serviceImport);
            }
        }
        vc.put("delegateImports", delegateImports);
    }

}
