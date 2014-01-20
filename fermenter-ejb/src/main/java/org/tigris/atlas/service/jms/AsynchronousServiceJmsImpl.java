package org.tigris.atlas.service.jms;


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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.MessageFactory;
import org.tigris.atlas.service.AsynchronousService;
import org.tigris.atlas.service.AsynchronousServiceDescriptor;
import org.tigris.atlas.service.AsynchronousServiceResponse;
import org.tigris.atlas.service.ServiceResponse;

public class AsynchronousServiceJmsImpl implements AsynchronousService {

	private static Log log = LogFactory.getLog(AsynchronousServiceJmsImpl.class);

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
					log.warn(e);
				}
			}
			
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					log.warn(e);
				}
			}
			
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					log.warn(e);
				}
			}
			
			if (ic != null) {			
				try {
					ic.close();
				} catch (NamingException e) {
					log.warn(e);
				}			
			}
			
		}
		
		return response;
	
	}
	
	private ServiceResponse handleJMSException(String methodName, JMSException jmse, ServiceResponse response) {
		if (log.isErrorEnabled()) {
			log.error("Exception occurred during " + methodName, jmse);
			
		}
		
		Message m = MessageFactory.createMessage();
		m.setKey("unknown.exception");
		response.getMessages().addMessage( m );
		return response;
	}
	
	private ServiceResponse serverUnavailable(NamingException ne, ServiceResponse response) {
		if (log.isErrorEnabled()) {
			log.error("async resource(s) could not be found!", ne);
			
		}
		
		Message m = MessageFactory.createMessage();
		m.setKey("server.unavailable");
		response.getMessages().addMessage( m );
		return response;
	}	


}
