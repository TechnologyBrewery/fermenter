package org.bitbucket.fermenter.service;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.bitbucket.fermenter.messages.Message;
import org.bitbucket.fermenter.messages.MessageFactory;
import org.bitbucket.fermenter.messages.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractService implements Service {

	private static final String USER_TXN_NAME = "java:comp/UserTransaction";
	private static final Logger LOG = LoggerFactory.getLogger( AbstractService.class );

	private UserTransaction txn = null;

	static {
		ResourceManager.init();
	}

	protected final void begin() {
		try {
			txn = (UserTransaction) new InitialContext().lookup( USER_TXN_NAME );
			txn.begin();
		} catch (NamingException e) {
			LOG.error( "User txn not found.", e );
		} catch (NotSupportedException e) {
			LOG.error( "Not supported exception starting txn.", e );
		} catch (SystemException e) {
			LOG.error( "Unknown exception starting txn.", e );
		}
	}

	protected final void commit() {
		try {
			txn.commit();
		} catch (SecurityException e) {
			LOG.error( "Exception encountered attempting to commit txn.", e );
		} catch (IllegalStateException e) {
			LOG.error( "Exception encountered attempting to commit txn.", e );
		} catch (RollbackException e) {
			LOG.error( "Exception encountered attempting to commit txn.", e );
		} catch (HeuristicMixedException e) {
			LOG.error( "Exception encountered attempting to commit txn.", e );
		} catch (HeuristicRollbackException e) {
			LOG.error( "Exception encountered attempting to commit txn.", e );
		} catch (SystemException e) {
			LOG.error( "Exception encountered attempting to commit txn.", e );
		}
	}

	protected final void rollback() {
		try {
			txn.rollback();
		} catch (IllegalStateException e) {
			LOG.error( "Exception encountered attempting to rollback txn.", e );
		} catch (SecurityException e) {
			LOG.error( "Exception encountered attempting to rollback txn.", e );
		} catch (SystemException e) {
			LOG.error( "Exception encountered attempting to rollback txn.", e );
		}
	}

	protected final void addMessage(ServiceResponse response, Severity severity, String msgKey, Object[] inserts) {
		Message m = MessageFactory.createMessage();
		m.setKey(msgKey);
		m.setSeverity(severity);
		for( int i=0; inserts != null && i<inserts.length; i++ ) {
			m.addInsert( inserts[ i ] );
		}

		response.getMessages().addMessage(m);
	}

	protected final void addError(ServiceResponse response, String msgKey, Object[] inserts) {
		addMessage( response, Severity.ERROR, msgKey, inserts );
	}

	protected final void addInfo(ServiceResponse response, String msgKey, Object[] inserts) {
		addMessage( response, Severity.INFO, msgKey, inserts );
	}

	protected final void addError(ServiceResponse response, String msgKey) {
		addError( response, msgKey, null );
	}

	protected final void addInfo(ServiceResponse response, String msgKey) {
		addInfo( response, msgKey, null );
	}

}
