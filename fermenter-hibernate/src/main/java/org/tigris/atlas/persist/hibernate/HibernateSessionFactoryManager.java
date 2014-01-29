package org.tigris.atlas.persist.hibernate;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.ask.krausening.Krausening;

/**
 * This singleton class configures then contains a reference to the 
 * thread-safe Hibernate {@link SessionFactory}.
 */
public final class HibernateSessionFactoryManager {

	private static HibernateSessionFactoryManager instance = new HibernateSessionFactoryManager();
	
	private static final String CONFIG = "hibernate.cfg.xml";
	
	private ServiceRegistry serviceRegistry;
	private SessionFactory sessionFactory;
	
	private HibernateSessionFactoryManager() {
		init();
		
	}
	
	/**
	 * Returns the singleton instance of this class.
	 * @return singleton instance
	 */
	public static HibernateSessionFactoryManager getInstance() {
		return instance;
		
	}
	
	private void init() {
		Krausening krausening = Krausening.getInstance();
		Properties hibernateProperties = krausening.getProperties("hibernate.properties");
		
		Configuration hibernateConfiguration = new Configuration();
		hibernateConfiguration.configure(CONFIG);
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
