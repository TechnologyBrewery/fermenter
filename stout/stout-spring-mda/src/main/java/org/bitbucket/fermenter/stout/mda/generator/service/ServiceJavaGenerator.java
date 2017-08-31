package org.bitbucket.fermenter.stout.mda.generator.service;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.service.AbstractServiceGenerator;
import org.bitbucket.fermenter.mda.metadata.element.Service;
import org.bitbucket.fermenter.stout.mda.JavaService;
import org.bitbucket.fermenter.stout.mda.java.JavaGeneratorUtil;

/**
 * Iterates through service instances, passing {@link JavaService}s instance to the templates.
 *
 */
public class ServiceJavaGenerator extends AbstractServiceGenerator {

    @Override
    protected void populateVelocityContext(VelocityContext vc, Service service, GenerationContext generationContext) {
        JavaService javaService = new JavaService(service);
        vc.put("service", javaService);
        vc.put("basePackage", generationContext.getBasePackage());
    }

    @Override
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
    }

}
