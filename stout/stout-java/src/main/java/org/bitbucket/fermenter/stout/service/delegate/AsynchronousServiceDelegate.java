package org.bitbucket.fermenter.stout.service.delegate;

import org.bitbucket.fermenter.stout.service.AsynchronousServiceDescriptor;
import org.bitbucket.fermenter.stout.service.AsynchronousServiceResponse;

/**
 * Provides a generic service to invoke asynchronous services.  
 */
public interface AsynchronousServiceDelegate {
	
	/** Name which uniquely identifies the AsyncTest service. */
	public static final String SERVICE_NAME = "org.bitbucket.fermenter.stout.service.AsynchronousService";	
	
	/**
	 * Puts the passed <tt>AsynchronousServiceDescriptor</tt> onto a queue so it can be invoked 
	 * asynchronously.
	 * @param serviceDescritor Describes the service to execute
	 * @return A response containing any feedback related to adding this operation to the queue
	 */
	public AsynchronousServiceResponse enqueue(AsynchronousServiceDescriptor serviceDescritor);

}
