package org.bitbucket.fermenter.stout.persist.hibernate;

import org.bitbucket.fermenter.stout.service.Resource;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateResource implements Resource {

	private static SessionFactory SESSION_FACTORY = null;
	private static final Logger LOG = LoggerFactory.getLogger(HibernateResource.class);

	public void init() {
		LOG.info("Initializing Hibernate Session Factory");
		try {
			HibernateSessionFactoryManager manager = HibernateSessionFactoryManager.getInstance();
			SESSION_FACTORY = manager.getSessionFactory();
		} catch (HibernateException e) {
			LOG.error("Error Loading Session Factory: " + e.getMessage());
			throw e;
		}
	}

	public static Session getSession() {
		if (SESSION_FACTORY == null) {
			LOG.error("Session factory not initialized.");
			return null;
		} else {
			return SESSION_FACTORY.getCurrentSession();
		}
	}

}
