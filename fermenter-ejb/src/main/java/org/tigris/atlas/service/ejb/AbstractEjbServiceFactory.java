package org.tigris.atlas.service.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.tigris.atlas.AtlasException;

/**
 * Contains common, non-generated logic for implementing EJB-based service factories.
 */
public abstract class AbstractEjbServiceFactory {

	protected Object performJndiLookup(String jndiName) {
		Object service = null;
		
		try {
			final Context context = new InitialContext();
			service = context.lookup(jndiName);
			
		} catch (NamingException e) {
			throw new AtlasException("Could not find the following JNDI name: " + jndiName, e);
		}
		
		return service;
		
	}
}
