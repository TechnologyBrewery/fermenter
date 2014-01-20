package org.tigris.atlas.persist.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.atlas.service.Resource;

public class HibernateResource implements Resource {

	private static final String CONFIG = "hibernate.cfg.xml";
	private static SessionFactory SESSION_FACTORY = null;
	private static final Logger LOG = LoggerFactory.getLogger(HibernateResource.class);

	public void init() {
		LOG.info( "Initializing Hibernate Session Factory" );
		try{
			SESSION_FACTORY = new Configuration().configure( CONFIG ).buildSessionFactory( );
		}
		catch(HibernateException e){
			LOG.error("Error Loading Session Factory: " +e.getMessage());
			throw e;
		}
	}

	public static Session getSession() {
		if( SESSION_FACTORY== null ) {
			LOG.error( "Session factory not initialized." );
			return null;
		}
		else {
			return SESSION_FACTORY.getCurrentSession();
		}
	}

}
