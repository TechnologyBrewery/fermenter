package org.bitbucket.fermenter.service.jms;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.bitbucket.fermenter.messages.Message;
import org.bitbucket.fermenter.messages.MessageFactory;
import org.bitbucket.fermenter.service.AsynchronousService;
import org.bitbucket.fermenter.service.AsynchronousServiceDescriptor;
import org.bitbucket.fermenter.service.AsynchronousServiceResponse;
import org.bitbucket.fermenter.service.ServiceResponse;
import org.bitbucket.fermenter.service.ejb.EjbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsynchronousServiceJmsImpl implements AsynchronousService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsynchronousServiceJmsImpl.class);

	public AsynchronousServiceResponse enqueue(AsynchronousServiceDescriptor serviceDescritor) {
		AsynchronousServiceResponse response = new AsynchronousServiceResponse();

		Context ic = null;
		QueueConnection connection = null;
		QueueSession session = null;
		QueueSender sender = null;
		
		try {
			ic = new InitialContext();
			QueueConnectionFactory connectionFactory = (QueueConnectionFactory)ic.lookup("jms/QueueConnectionFactory");
			connection = connectionFactory.createQueueConnection();
			session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue)ic.lookup("jms/coreQueue");
			sender = session.createSender(queue);
			ObjectMessage message = session.createObjectMessage();
			
			message.setObject(serviceDescritor);
			
			sender.send(message);
			
		} catch (NamingException ne) {
			response = (AsynchronousServiceResponse)serverUnavailable(ne, response);
			
		} catch (JMSException jmse) {
			String methodName = serviceDescritor.getOperationName();
			response = (AsynchronousServiceResponse)handleJMSException(methodName, jmse, response);
			
		} finally {
			
			if (sender != null) {
				try {
					sender.close();
				} catch (JMSException e) {
					LOGGER.warn("Encounted a problem cleaning up a Sender", e);
				}
			}
			
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					LOGGER.warn("Encounted a problem cleaning up a Session",e);
				}
			}
			
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					LOGGER.warn("Encounted a problem cleaning up a Connection", e);
				}
			}
			
			EjbUtil.closeContext(ic);
			
		}
		
		return response;
	
	}
	
	private ServiceResponse handleJMSException(String methodName, JMSException jmse, ServiceResponse response) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Exception occurred during " + methodName, jmse);
			
		}
		
		Message m = MessageFactory.createMessage();
		m.setKey("unknown.exception");
		response.getMessages().addMessage( m );
		return response;
	}
	
	private ServiceResponse serverUnavailable(NamingException ne, ServiceResponse response) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("async resource(s) could not be found!", ne);
			
		}
		
		Message m = MessageFactory.createMessage();
		m.setKey("server.unavailable");
		response.getMessages().addMessage( m );
		return response;
	}	


}