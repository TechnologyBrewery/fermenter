package org.bitbucket.fermenter.service;

/**
 * Provides a generic service to invoke asynchronous services.  
 */
public interface AsynchronousService {
	
	/**
	 * Puts the passed <tt>AsynchronousServiceDescriptor</tt> onto a queue so it can be invoked 
	 * asynchronously.
	 * @param serviceDescritor Describes the service to execute
	 * @return A response containing any feedback related to adding this operation to the queue
	 */
	public AsynchronousServiceResponse enqueue(AsynchronousServiceDescriptor serviceDescritor);

}
