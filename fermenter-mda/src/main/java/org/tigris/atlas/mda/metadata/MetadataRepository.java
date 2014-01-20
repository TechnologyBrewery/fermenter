package org.tigris.atlas.mda.metadata;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tigris.atlas.mda.metadata.element.Composite;
import org.tigris.atlas.mda.metadata.element.Entity;
import org.tigris.atlas.mda.metadata.element.Enumeration;
import org.tigris.atlas.mda.metadata.element.Form;
import org.tigris.atlas.mda.metadata.element.Service;

public class MetadataRepository {
	
	private static Log log = LogFactory.getLog(MetadataRepository.class);
	private static MetadataRepository REPOSITORY;
	
	public static void initialize(Properties props) {
		REPOSITORY = new MetadataRepository(props);
		CompositeMetadataManager.getInstance().validate();
		EntityMetadataManager.getInstance().validate();
		ServiceMetadataManager.getInstance().validate();
		EnumerationMetadataManager.getInstance().validate();		
		FormMetadataManager.getInstance().validate();
	}
	
	public static MetadataRepository getInstance() {
		if (REPOSITORY == null) {
			throw new IllegalStateException("The metadata repository has not been initialized");
		}
		
		return REPOSITORY;
	}
	
	private String applicationName;

	private MetadataRepository(Properties props) {
		super();
		
		try {
			applicationName = props.getProperty("application.name");			
			loadAllMetadata(props);	
		} catch (Exception ex) {
			throw new RuntimeException("Unable to load metadata for application " + applicationName, ex);
		}
	}
	
	public String getApplicationName() {
		return applicationName;
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
		String metadataLoaderClass = props.getProperty("metadata.loader");
		Class clazz = Class.forName(metadataLoaderClass);
		MetadataURLResolver loader = (MetadataURLResolver) clazz.newInstance();
		List urls = loader.getMetadataURLs(props);
		for (Iterator i = urls.iterator(); i.hasNext();) {
			long start = System.currentTimeMillis();
			MetadataURL url = (MetadataURL) i.next();
			CompositeMetadataManager.getInstance().loadMetadata(url.getApplicationName(), url.getUrl());
			EntityMetadataManager.getInstance().loadMetadata(url.getApplicationName(), url.getUrl());
			ServiceMetadataManager.getInstance().loadMetadata(url.getApplicationName(), url.getUrl());
			EnumerationMetadataManager.getInstance().loadMetadata(url.getApplicationName(), url.getUrl());			
			FormMetadataManager.getInstance().loadMetadata(url.getApplicationName(), url.getUrl());

			if (applicationName.equals(url.getApplicationName())) {
                // Messages metadata only needs to be loaded for the current project
				MessagesMetadataManager.getInstance().loadMetadata(url.getUrl());	
				
				// Load format information for the current project only
				FormatMetadataManager.getInstance().loadMetadata(url.getUrl());
			}
			if (log.isInfoEnabled()) {
				long stop = System.currentTimeMillis();
				log.info("Metadata for application '" + applicationName + "' has been loaded - " + (stop - start) + "ms");
			}
			
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
	
	public Map getAllServices() {
		return ServiceMetadataManager.getInstance().getCompleteMetadataMap();
	}
	
	public Map getAllServices(String applicationName) {
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
	
	public Map getAllForms() {
		return FormMetadataManager.getInstance().getCompleteMetadataMap();
	}
	
	public Map getAllForms(String applicationName) {
		return FormMetadataManager.getForms(applicationName);
	}
	
	public Form getForm(String applicationName, String formName) {
		Map forms = getAllForms(applicationName);
		return (Form) forms.get(formName);
	}
	
	public Form getForm(String formName) {
		Map forms = getAllForms();
		return (Form) forms.get(formName);
	}
	
}
