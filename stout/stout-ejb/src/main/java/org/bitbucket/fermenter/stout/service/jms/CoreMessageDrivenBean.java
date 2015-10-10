package org.bitbucket.fermenter.stout.service.jms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.bitbucket.fermenter.stout.factory.FactoryManager;
import org.bitbucket.fermenter.stout.service.AbstractServiceFactoryInterface;
import org.bitbucket.fermenter.stout.service.AsynchronousServiceDescriptor;
import org.bitbucket.fermenter.stout.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base implementation of a MDB.
 */
public class CoreMessageDrivenBean implements MessageDrivenBean, MessageListener {
	
	private static final long serialVersionUID = -4880598904719510378L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreMessageDrivenBean.class);
	
	private MessageDrivenContext context;
	
	/**
	 * {@inheritDoc}
	 */
	public void onMessage(Message message) {
		LOGGER.debug("Message received...");
		
		ObjectMessage objectMessage = getObjectMessage(message);		
		AsynchronousServiceDescriptor serviceDescriptor = getPayload(objectMessage);
		String serviceName = serviceDescriptor.getServiceName();
		String operationName = serviceDescriptor.getOperationName();
		List parameterList = serviceDescriptor.getParameterList();
		List classList = serviceDescriptor.getClassList();
		
		AbstractServiceFactoryInterface factory = (AbstractServiceFactoryInterface) FactoryManager.createFactory(FactoryManager.SERVICE, serviceName);
		Service service = factory.createService(serviceName);
		Class serviceClass = service.getClass();
		
		//create class and instance arrays:
		int size = (parameterList != null) ? parameterList.size() : 0;
		Class[] paramClasses = new Class[size];
		Object[] paramInstances = new Object[size];
		if (size > 0) {		
			int location = 0;
			Iterator paramIterator = parameterList.iterator();
			Iterator classIterator = classList.iterator();
			while ((paramIterator.hasNext()) && (classIterator.hasNext())) {
				paramInstances[location] = paramIterator.next();
				paramClasses[location++] = (Class)classIterator.next();
			}
		}
		
		
		try {
			Method method = serviceClass.getMethod(operationName, paramClasses);
			method.invoke(service, paramInstances);		
			
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			LOGGER.error("Unexpected problem invoking MDB method!" , e);
		} catch (NoSuchMethodException e) {
			String opName = buildMethodName(operationName, classList).toString();
			LOGGER.error("Cannot find method: " + opName, e);
		}		
		
	}

	private StringBuilder buildMethodName(String operationName, List classList) {
		StringBuilder opName = new StringBuilder();
		opName.append(operationName).append("(");
		Iterator classIterator = (classList != null) 
			? classList.iterator() : Collections.EMPTY_LIST.iterator();
		boolean isFirst = true;
		while (classIterator.hasNext()) {
			if (!isFirst) {
				opName.append(", ");
			}
			opName.append(((Class)classIterator.next()).getName());
			isFirst = false;
		}
		opName.append(")");
		return opName;
	}

	/**
	 * Casts the message into a {@link ObjectMessage}.
	 * @param message The message passed into this MDB
	 * @return The expected {@link ObjectMessage} or null if the object does not exist
 	 */
	private ObjectMessage getObjectMessage(Message message) {
		ObjectMessage objectMessage = null;
		try {
			objectMessage = (ObjectMessage)message;
			
		} catch (ClassCastException cce) {
			LOGGER.error("CoreMessageDrivenBean only accepts ObjectMessages - the passed message was :" 
				+ message.getClass(), cce);			
		}
		return objectMessage;
	}

	/**
	 * Extracts the payload from the passed message.
	 * @param objectMessage The message containing the payload.
	 * @return The payload from the message or null if the payload does not exist
	 */
	private AsynchronousServiceDescriptor getPayload(ObjectMessage objectMessage) {
		Object o = null;
		AsynchronousServiceDescriptor serviceDescriptor = null;
		try {
			o = objectMessage.getObject();
			if (o != null) {
				serviceDescriptor = (AsynchronousServiceDescriptor)o;
			} else {
				LOGGER.error("A payload must exist!");
			}
		} catch (ClassCastException cce) {
			LOGGER.error("CoreMessageDrivenBean must have object payloads of AsynchronousServiceDescriptor - " +
				"the passed object was :" + o.getClass(), cce);
		} catch (JMSException jmse) {
			String msg = "An error was encountered while reading the payload from a message";
			LOGGER.error(msg, jmse);
			throw new EJBException(msg, jmse);
			
		}
		return serviceDescriptor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void ejbRemove() {
	}

        /**
         * {@inheritDoc}
         */
	public void setMessageDrivenContext(MessageDrivenContext context) {
		this.context = context;
		
	}
	
	

}
