package org.tigris.atlas.service.delegate;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.atlas.messages.Message;
import org.tigris.atlas.messages.MessageFactory;
import org.tigris.atlas.messages.Severity;
import org.tigris.atlas.service.AsynchronousServiceDescriptor;
import org.tigris.atlas.service.AsynchronousServiceResponse;
import org.tigris.atlas.service.ServiceResponse;

public class AnsynchronousServiceDelegateImpl implements AsynchronousServiceDelegate {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AnsynchronousServiceDelegateImpl.class);

	public AsynchronousServiceResponse enqueue(AsynchronousServiceDescriptor serviceDescriptor) {
		AsynchronousServiceResponse response = new AsynchronousServiceResponse();
		
		if (serviceDescriptor == null) {
			Message m = MessageFactory.createMessage();
			m.setKey("unknown.exception");
			m.setSeverity(Severity.ERROR);				
			response.getMessages().addMessage( m );
			LOGGER.error("Cannot invoke an asynchronous service with a null AsynchronousServiceDescriptor!");
			
		} else { 			
			try {
				Context ic = new InitialContext();
				QueueConnectionFactory connectionFactory = (QueueConnectionFactory)ic.lookup("jms/QueueConnectionFactory");
				QueueConnection connection = connectionFactory.createQueueConnection();
				QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
				if (session != null) {
					Queue queue = (Queue)ic.lookup("jms/coreQueue");
					QueueSender sender = session.createSender(queue);
					ObjectMessage message = session.createObjectMessage();
					
					message.setObject(serviceDescriptor);
					
					sender.send(message);
					
				} else {
					Message m = MessageFactory.createMessage();
					m.setKey("unknown.exception");
					m.setSeverity(Severity.ERROR);				
					response.getMessages().addMessage( m );
					LOGGER.error("Could not connect to JMS resources to perform asynchronous action!");
					
				}
				
			} catch (NamingException ne) {
				response = (AsynchronousServiceResponse)serverUnavailable(ne, response);
				
			} catch (JMSException jmse) {
				String methodName = serviceDescriptor.getOperationName();
				response = (AsynchronousServiceResponse)handleJMSException(methodName, jmse, response);
				
			}
		}	
		
		return response;
	}
	
	private ServiceResponse handleJMSException(String methodName, JMSException jmse, ServiceResponse response) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Exception occurred during " + methodName, jmse);
			
		}
		
		Message m = MessageFactory.createMessage();
		m.setKey("unknown.exception");
		m.setSeverity(Severity.ERROR);
		response.getMessages().addMessage( m );
		return response;
	}
	
	private ServiceResponse serverUnavailable(NamingException ne, ServiceResponse response) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error("async resource(s) could not be found!", ne);
			
		}
		
		Message m = MessageFactory.createMessage();
		m.setKey("server.unavailable");
		m.setSeverity(Severity.ERROR);
		response.getMessages().addMessage( m );
		return response;
	}	

}
