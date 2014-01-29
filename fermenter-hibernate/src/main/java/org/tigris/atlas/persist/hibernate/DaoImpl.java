package org.tigris.atlas.persist.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.ObjectDeletedException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.atlas.bizobj.AbstractBusinessObjectFactoryInterface;
import org.tigris.atlas.bizobj.BusinessObject;
import org.tigris.atlas.factory.FactoryFactory;
import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.MessageFactory;
import org.tigris.atlas.messages.Severity;
import org.tigris.atlas.persist.Dao;
import org.tigris.atlas.transfer.PrimaryKey;
import org.tigris.atlas.validate.Validated;

/**
 * The base class for Hibernate persistence.
 */
public abstract class DaoImpl implements Dao {
	
	private static final Logger LOG = LoggerFactory.getLogger(DaoImpl.class);
	private static final String DATABASE_EXCEPTION_KEY = "database.exception";
	
	private Session getSession() {
		SessionFactory hibernateSessionFactory = HibernateSessionFactoryManager.getInstance().getSessionFactory();
		Session hibernateSession;
		try {
			hibernateSession = hibernateSessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			hibernateSession = hibernateSessionFactory.openSession();
		}

		return hibernateSession;
	}

	protected abstract String getEntityName();

	protected abstract String getNamespace();

	/**
	 * Save the entity and children
	 *
	 * @param entity Entity entity save
	 * @return Entity The saved entity
	 */
	protected BusinessObject save(Validated validValues) {
		BusinessObject bo = validValues.getValidValues();
		try {
			if (bo.getKey().getValue() == null) {
				getSession().persist( validValues.getValidValues() );
			} else {
				bo = (BusinessObject)getSession().merge( validValues.getValidValues() );
			}
			return bo;
		} catch(HibernateException he) {
			return handleDataAccessException( bo, he );
		}
	}

	/**
	 *
	 * @param entity The object entity delete
	 * @return The deleted object
	 */
	public BusinessObject delete(PrimaryKey pk) {
		BusinessObject bo = findByPrimaryKey(pk);

		//Let Hibernate cascade delete op
		getSession().delete( bo );

		return bo;
	}

	/**
	 * Find an object by primary key
	 * @param entity The object containing the primary key
	 * @return The selected object (the presence of a return object may not indicate a found key)
	 */
	public BusinessObject findByPrimaryKey(PrimaryKey pk) {
		BusinessObject rto = null;

		try {
			AbstractBusinessObjectFactoryInterface factory =
					(AbstractBusinessObjectFactoryInterface) FactoryFactory.createFactory(FactoryFactory.BUSINESS_OBJECT, pk.getClass());
			Class clazz = factory.createBusinessObject( pk.getEntityName() ).getClass();
			rto = (BusinessObject) getSession().load( clazz, pk.getValue() );
			// [Allan/Steve] Leave this out for now, but re-visit when testing child saves, etc.
			//getHibernateTemplate().evict( rto );

			// [Allan] The catch statements below need to remain.
			//         Some Hibernate id types throw the specific exceptions,
			//         while others throw only DAE.
		} catch (ObjectNotFoundException ex) {
			return null;
		} catch (ObjectDeletedException ex) {
			return null;
		} catch(HibernateException dae) {
			rto = handleDataAccessException( pk, dae );
		}

		return rto;
	}


	/**
	 * Answer a list of entities that match the criteria defined by a named query.
	 * @param queryName Name of the query entity execute.  Must be defined in a Hibernate mapping file.
	 * @param paramNames Array of query parameter names.  Must match param names in Hibernate mapping file.
	 * @param paramValues Array of query parameter values
	 * @param types Array of query parameter types
	 * @return
	 */
	protected List query(String queryName, String[] paramNames, Object[] paramValues, Type[] paramTypes) {
		List entities = null;

		try {
			//use getSession(true) rather than getSessionFactory()... b/c we are outside of spring's
			//hibernate template at this point.  As a result, we need to trigger the session to bind
			//to the transaction manually.  The problem only presents itself when using a JTA transaction
			//manager in spring.
			Session session = getSession();
			Query query = session.getNamedQuery(queryName);
			if (paramNames != null) {
				int length = paramNames.length;
				for (int i = 0; i < length; i++) {
					query.setParameter(paramNames[i], paramValues[i], paramTypes[i]);
				}

				entities = query.list();
			}

		} catch (HibernateException dae) {
			entities = new ArrayList( 1 );
			entities.add( handleDataAccessException( dae ) );
			return entities;

		}

		return entities;
	}

	/**
	 * Turn the database exception into a meaningful error message
	 * @param pk
	 * @param dae
	 * @return
	 */
	private BusinessObject handleDataAccessException(PrimaryKey pk, HibernateException dae) {
		Throwable cause = dae.getCause();

		if (cause != null && cause instanceof ObjectDeletedException) {
			// Object was deleted - return null
			return null;
		} else if (cause != null && cause instanceof ObjectNotFoundException) {
			// Object not found - return null
			return null;
		}


		AbstractBusinessObjectFactoryInterface factory =
				(AbstractBusinessObjectFactoryInterface) FactoryFactory.createFactory(FactoryFactory.BUSINESS_OBJECT, pk.getClass());
		BusinessObject bo = factory.createBusinessObject( pk.getEntityName() );
		bo.setKey( pk );
		return handleDataAccessException( bo, dae );
	}

	/**
	 * Turn the database exception into a meaningful error message
	 * @param dae
	 * @return
	 */
	protected BusinessObject handleDataAccessException(HibernateException dae) {
		AbstractBusinessObjectFactoryInterface factory =
				(AbstractBusinessObjectFactoryInterface) FactoryFactory.createFactory(FactoryFactory.BUSINESS_OBJECT, getClass());
		BusinessObject bo = factory.createBusinessObject( getEntityName() );
		return handleDataAccessException( bo, dae );
	}

	/**
	 * Turn the database exception into a meaningful error message
	 * @param entity
	 * @param dae
	 * @return
	 */
	private BusinessObject handleDataAccessException(BusinessObject bo, HibernateException dae) {
		if (LOG.isDebugEnabled()) {
			LOG.error("A problem was encountered interacting with the database:", dae);
		}
		Message message = MessageFactory.createMessage();
		message.setKey(DATABASE_EXCEPTION_KEY);
		message.addInsert(dae.getMessage());
		message.setSeverity(Severity.getSeverity(Severity.ERROR));
		if (bo != null) {
			bo.getMessages().addMessage(message);

		} else {
			LOG.debug("No BO on which to attach the an error message!", dae);
		}

		return bo;
	}
}
