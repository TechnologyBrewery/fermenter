package org.tigris.atlas.service.ejb;

import javax.naming.Context;
import javax.naming.NamingException;

/**
 * Contains utility methods for working with EJBs
 */
public class EjbUtil {
	
	//private static Log log = LogFactory.getLog(EjbUtil.class);

	/**
	 * This method encapsulated the logic to safely close an initial context.  If an
	 * exception is encountered while performing this operation, then a warning-level
	 * log messgae will be exectued.  This method is primarily aimed at keeping EJB
	 * code more terse.
	 * @param context The <tt>Context</tt> to close.  
	 */
	public static void closeContext(Context context) {
		if (context != null) {
			try {
				context.close();
			} catch (NamingException ne) {
				//This is okay at this point, so do nothing
				
				//if (log.isWarnEnabled()) {
				//	log.warn("Problems were encountered while closing a context!", ne);
				//}
			}
		}
	}
	
}
