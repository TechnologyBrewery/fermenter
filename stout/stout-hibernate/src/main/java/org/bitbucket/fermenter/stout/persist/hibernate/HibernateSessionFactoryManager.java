package org.bitbucket.fermenter.stout.persist.hibernate;

import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.krausening.Krausening;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.util.ConfigHelper;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This singleton class configures then contains a reference to the 
 * thread-safe Hibernate {@link SessionFactory}.
 */
public final class HibernateSessionFactoryManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateSessionFactoryManager.class);
	
	private static final String CONFIG = "hibernate.cfg.xml";
	
	private static HibernateSessionFactoryManager instance = new HibernateSessionFactoryManager();
	
	private ServiceRegistry serviceRegistry;
	private SessionFactory sessionFactory;
	
	private HibernateSessionFactoryManager() {
		
	}
	
	/**
	 * Returns the singleton instance of this class.
	 * @return singleton instance
	 */
	public static HibernateSessionFactoryManager getInstance() {
		return instance;
		
	}

	public void init() {
	    init("");
	}
	    
	public void init(String prefix) {  
		Krausening krausening = Krausening.getInstance();
		String propertiesName;
		if (StringUtils.isNotBlank(prefix)) {
		    propertiesName = prefix + (!prefix.endsWith(".") ? "." : "") + "hibernate.properties";
		    LOGGER.info("Using a customized Hibernate properties name of: " + propertiesName);
		} else {
		    propertiesName = "hibernate.properties";
		}
		Properties hibernateProperties = krausening.getProperties(propertiesName);
		if (hibernateProperties == null) {
			LOGGER.warn("A " + propertiesName + " file was not found in Krausening!");
		} else if (hibernateProperties.isEmpty()) {
			LOGGER.warn("No properties are defined within the " + propertiesName + " file in Krausening!");
		}
		
		URL configUrl = ConfigHelper.locateConfig(CONFIG);
		Configuration hibernateConfiguration = new Configuration();
		hibernateConfiguration.configure(configUrl);
		hibernateConfiguration.addProperties(hibernateProperties);
		
		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
		registryBuilder.applySettings(hibernateConfiguration.getProperties());
		serviceRegistry = registryBuilder.build();
		
		sessionFactory = hibernateConfiguration.buildSessionFactory(serviceRegistry);
		
	}

	/**
	 * Returns the {@link SessionFactory} managed by this instance.
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
}
