package org.bitbucket.fermenter.stout.persist.hibernate.config;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.bitbucket.fermenter.stout.persist.hibernate.HibernateSessionFactoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Startup
@Singleton
public class HibernateSessionFactoryStartup {

	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateSessionFactoryStartup.class);

	/**
	 * Force an initialization outside a transition for Schema Export can work, when configured. This is needed because
	 * DDL often autocommits, so such operations cannot respect the transactionality of a method.
	 */
	@PostConstruct
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void configureHibernateSessionFactory() {
		LOGGER.debug("Starting Hibernate configuration...");
		HibernateSessionFactoryManager.getInstance();
		LOGGER.debug("Completed Hibernate configuration.");
	}

}
