package org.bitbucket.fermenter.mda.metadata;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.element.Composite;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Enumeration;
import org.bitbucket.fermenter.mda.metadata.element.Service;

public class MetadataRepository extends AbstractMetadataRepository {
	
	private static Log LOG = LogFactory.getLog(MetadataRepository.class);
	
	public MetadataRepository(Properties properties) {
		super(properties);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void load(Properties properties) {
		try {
            loadAllMetadata(properties);
            
		} catch (Exception ex) {
            throw new GenerationException("Unable to load metadata for application " + applicationName, ex);
        }

	}
	
	/**
	 * {@inheritDoc}
	 */
	public void validate(Properties properties) {		
		CompositeMetadataManager.getInstance().validate();
        EntityMetadataManager.getInstance().validate();
        ServiceMetadataManager.getInstance().validate();
        EnumerationMetadataManager.getInstance().validate();        
	}
	
	public Entity getEntity(String entityName) {
		Map entityMap = getAllEntities();
		return (Entity)entityMap.get(entityName);
	}
	
	public Entity getEntity(String applicationName, String entityName) {
		Map entityMap = getAllEntities(applicationName);
		return (Entity)entityMap.get(entityName);
	}
	
	public Map getAllEntities() {
		return EntityMetadataManager.getInstance().getCompleteMetadataMap();
	}
	
	public Map getAllEntities(String applicationName) {
		return EntityMetadataManager.getEntities(applicationName);
	}
	
	public Service getService(String serviceName) {
		Map serviceMap = getAllServices();
		return (Service)serviceMap.get(serviceName);
	}
	
	public Service getService(String applicationName, String serviceName) {
		Map serviceMap = getAllServices(applicationName);
		return (Service)serviceMap.get(serviceName);
	}

	private void loadAllMetadata(Properties props) throws Exception {
		if (props != null) {
    		String metadataLoaderClass = props.getProperty("metadata.loader");
    		Class clazz = Class.forName(metadataLoaderClass);
    		MetadataURLResolver loader = (MetadataURLResolver) clazz.newInstance();
            CompositeMetadataManager compositeManager = CompositeMetadataManager.getInstance();
            compositeManager.reset();
            EntityMetadataManager entityManager =  EntityMetadataManager.getInstance();
            entityManager.reset();
            ServiceMetadataManager serviceManager =  ServiceMetadataManager.getInstance();
            serviceManager.reset();
            EnumerationMetadataManager enumerationManager = EnumerationMetadataManager.getInstance();
            enumerationManager.reset();
            List urls = loader.getMetadataURLs(props);
    		for (Iterator i = urls.iterator(); i.hasNext();) {
    			long start = System.currentTimeMillis();
    			MetadataURL url = (MetadataURL) i.next();
    			compositeManager.loadMetadata(url.getApplicationName(), url.getUrl());
    			entityManager.loadMetadata(url.getApplicationName(), url.getUrl());
    			serviceManager.loadMetadata(url.getApplicationName(), url.getUrl());
    			enumerationManager.loadMetadata(url.getApplicationName(), url.getUrl());			
    
    			if (applicationName.equals(url.getApplicationName())) {
                    // Messages metadata only needs to be loaded for the current project
    				MessagesMetadataManager.getInstance().loadMetadata(url.getUrl());	
    				
    				// Load format information for the current project only
    				FormatMetadataManager.getInstance().loadMetadata(url.getUrl());
    			}
    			if (LOG.isInfoEnabled()) {
    				long stop = System.currentTimeMillis();
    				LOG.info("Metadata for application '" + applicationName + "' has been loaded - " + (stop - start) + "ms");
    			}
    			
    		}
		} else {
			LOG.warn("No properties provided, unable to load any metadata!");
		}
	}

	public Map getAllEnumerations() {
		return EnumerationMetadataManager.getInstance().getCompleteMetadataMap();
	}
	
	public Map getAllEnumerations(String applicationName) {
		return EnumerationMetadataManager.getEnumerations(applicationName);
	}

	public Enumeration getEnumeration(String type) {
		Map enumMap = getAllEnumerations();
		return (Enumeration)enumMap.get(type);
	}
	
	public Enumeration getEnumeration(String applicationName, String type) {
		Map enumMap = getAllEnumerations(applicationName);
		return (Enumeration)enumMap.get(type);
	}
	
	public Map<String, Service> getAllServices() {
		return ServiceMetadataManager.getInstance().getCompleteMetadataMap();
	}
	
	public Map<String, Service> getAllServices(String applicationName) {
		return ServiceMetadataManager.getServices(applicationName);
	}	
	
	public Map getAllComposites() {
		return CompositeMetadataManager.getInstance().getCompleteMetadataMap();
	}
	
	public Map getAllComposites(String applicationName) {
		return CompositeMetadataManager.getComposites(applicationName);
	}
	
	public Composite getComposite(String applicationName, String compositeType) {
		Map composites = getAllComposites(applicationName);
		return (Composite) composites.get(compositeType);
	}
	
	public Composite getComposite(String compositeType) {
		Map composites = getAllComposites();
		return (Composite) composites.get(compositeType);
	}	

}
