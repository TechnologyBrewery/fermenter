package org.bitbucket.fermenter.stout.mda.generator.service;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metamodel.element.Service;
import org.bitbucket.fermenter.stout.mda.JavaService;
import org.bitbucket.fermenter.stout.mda.RemoteJavaService;

/**
 * Iterates through service instances, passing {@link JavaRemoteService}s instance to the templates.
 *
 */
public class ServiceDelegateJavaGenerator extends ServiceJavaGenerator {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populateVelocityContext(VelocityContext vc, Service service, GenerationContext generationContext) {
        super.populateVelocityContext(vc, service, generationContext);
        JavaService javaService = (JavaService) vc.get(SERVICE);
        RemoteJavaService remoteJavaService = new RemoteJavaService(javaService);
        vc.put(SERVICE, remoteJavaService);

    }

}
