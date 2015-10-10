package org.bitbucket.fermenter.mda.metadata;


import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.metadata.element.OperationMetadata;
import org.bitbucket.fermenter.mda.metadata.element.ParameterMetadata;
import org.bitbucket.fermenter.mda.metadata.element.Service;
import org.bitbucket.fermenter.mda.metadata.element.ServiceMetadata;

/**
 * Responsible for maintaining the list of service metadata in the system.
 */
class ServiceMetadataManager extends MetadataManager {
	
    private static ServiceMetadataManager INSTANCE = new ServiceMetadataManager();
	
	private static Log log = LogFactory.getLog( ServiceMetadataManager.class );
	
	public static ServiceMetadataManager getInstance() {
		return INSTANCE;
	}
	
	protected String getMetadataLocation() {
		return "services";
	}

	private ServiceMetadataManager() {
		super();				
	}
	
	/**
	 * Answer the metadata for a specified service.
	 * 
	 * @param serviceName
	 * @return
	 */
	public static Service getServiceMetadata(String applicationName, String serviceName) {
		return (Service) getInstance().getMetadataMap( applicationName ).get( serviceName );
	}
	
	/**
	 * Returns the metadata element by name from any application that is loaded.
	 * @param name The name by which to retrieve
	 * @return The <tt>Service</tt> instance for <tt>name</tt> or null
	 */
	public static Service getServiceMetadata(String name) {
		Map<String, Service> map = getInstance().getCompleteMetadataMap();
		return (map != null) ? map.get(name) : null;
	}
	
	/**
	 * Answer the full collection of service metadata entries.
	 * 
	 * @return
	 */
	public static Map<String, Service> getServices(String applicationName) {
		return getInstance().getMetadataMap( applicationName );
	}
	
	public void addService(ServiceMetadata s) {
		s.setApplicationName(currentApplication);
		addMetadataElement( s.getName(), s );
	}

	/**
	 * Set up the parsing rules for the disgester.
	 * 
	 * @param digester Used to parse the metadata file into metadata elements
	 */
	protected void initialize(Digester digester) {
		
		digester.addObjectCreate(	"service", 			ServiceMetadata.class.getName()		);
		digester.addCallMethod( 	"service/name", 	"setName", 							0 	);
		digester.addCallMethod( 	"service/documentation", 	"setDocumentation", 			0 	);
		
		digester.addObjectCreate( 	"service/operations/operation", 							OperationMetadata.class.getName() );
		digester.addCallMethod( 	"service/operations/operation/name", 						"setName", 				0 );
		digester.addCallMethod( 	"service/operations/operation/documentation", 				"setDocumentation", 	0 );
		digester.addCallMethod( 	"service/operations/operation/return", 						"setReturnType", 		0 );
		digester.addCallMethod( 	"service/operations/operation/return-many", 				"setReturnManyType", 	0 );
		digester.addCallMethod( 	"service/operations/operation/view-type", 					"setViewType", 	0 );
		digester.addCallMethod( 	"service/operations/operation/transaction-attribute", 		"setTransactionAttribute", 	0 );
		digester.addCallMethod( 	"service/operations/operation/transmission-method", 		"setTransmissionMethod", 	0 );
		digester.addObjectCreate( 	"service/operations/operation/parameters/parameter", 		ParameterMetadata.class.getName() );
		digester.addCallMethod( 	"service/operations/operation/parameters/parameter/name", 	"setName", 				0 );
		digester.addCallMethod( 	"service/operations/operation/parameters/parameter/documentation", 	"setDocumentation", 				0 );
		digester.addCallMethod( 	"service/operations/operation/parameters/parameter/type",	"setType", 				0 );
		digester.addCallMethod( 	"service/operations/operation/parameters/parameter/many",	"setMany", 				0 );
		digester.addCallMethod( 	"service/operations/operation/parameters/parameter/project","setProject", 			0 );
        digester.addSetNext( 		"service/operations/operation/parameters/parameter", 		"addParameter", 		ParameterMetadata.class.getName() );
		digester.addSetNext( 		"service/operations/operation", 							"addOperation", 		OperationMetadata.class.getName() );
		
		digester.addSetNext( 		"service", "addService", ServiceMetadata.class.getName() );
		
		if (log.isDebugEnabled()) {
			log.debug("Initialization complete");
		}

	}

}
