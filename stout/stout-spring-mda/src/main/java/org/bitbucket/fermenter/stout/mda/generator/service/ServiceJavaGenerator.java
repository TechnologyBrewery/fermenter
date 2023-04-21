package org.technologybrewery.fermenter.stout.mda.generator.service;

import org.apache.velocity.VelocityContext;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;
import org.technologybrewery.fermenter.mda.generator.service.AbstractServiceGenerator;
import org.technologybrewery.fermenter.mda.metamodel.element.Service;
import org.technologybrewery.fermenter.stout.mda.JavaService;
import org.technologybrewery.fermenter.stout.mda.java.JavaGeneratorUtil;

/**
 * Iterates through service instances, passing {@link JavaService}s instance to the templates.
 *
 */
public class ServiceJavaGenerator extends AbstractServiceGenerator {

    private static final String BASE_PACKAGE = "basePackage";
    protected static final String SERVICE = "service";

    @Override
    protected void populateVelocityContext(VelocityContext vc, Service service, GenerationContext generationContext) {
        JavaService javaService = new JavaService(service);
        vc.put(SERVICE, javaService);
        vc.put(BASE_PACKAGE, generationContext.getBasePackage());
    }

    @Override
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
    }

}
