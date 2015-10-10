package org.bitbucket.fermenter.test.domain.service.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * This class is a marker that informs the EE contain to activate an JAX-RS bindings.
 */
@ApplicationPath("/rest")
public class JaxRsActivator extends Application {

	// class body intentionally left blank

}