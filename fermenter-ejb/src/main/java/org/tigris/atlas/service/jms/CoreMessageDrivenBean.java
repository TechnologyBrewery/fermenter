package org.tigris.atlas.service.jms;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tigris.atlas.factory.FactoryFactory;
import org.tigris.atlas.service.AbstractServiceFactoryInterface;
import org.tigris.atlas.service.Service;
import org.tigris.atlas.service.AsynchronousServiceDescriptor;

public class CoreMessageDrivenBean implements MessageDrivenBean, MessageListener {
	
	private static Log log = LogFactory.getLog(CoreMessageDrivenBean.class);
	
	private MessageDrivenContext context;
	
	/**
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	public void onMessage(Message message) {
		log.debug("Message received...");
		
		ObjectMessage objectMessage = getObjectMessage(message);		
		AsynchronousServiceDescriptor serviceDescriptor = getPayload(objectMessage);
		String serviceName = serviceDescriptor.getServiceName();
		String operationName = serviceDescriptor.getOperationName();
		List parameterList = serviceDescriptor.getParameterList();
		List classList = serviceDescriptor.getClassList();
		
		AbstractServiceFactoryInterface factory = (AbstractServiceFactoryInterface) FactoryFactory.createFactory(FactoryFactory.SERVICE, serviceName);
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
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			String opName = buildMethodName(operationName, classList).toString();
			log.error("Cannot find method: " + opName, e);
			//TODO: remove
			System.out.println("Cannot find method: " + opName);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

	private StringBuffer buildMethodName(String operationName, List classList) {
		StringBuffer opName = new StringBuffer();
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
	 * Casts the message into a <tt>ObjectMessage</tt>
	 * @param message The message passed into this MDB
	 * @return The expected <tt>ObjectMessage</tt> or null if the object does not exist
 	 */
	private ObjectMessage getObjectMessage(Message message) {
		ObjectMessage objectMessage = null;
		try {
			objectMessage = (ObjectMessage)message;
		} catch (ClassCastException cce) {
			log.error("CoreMessageDrivenBean only accepts ObjectMessages - the passed message was :" 
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
				log.error("A payload must exist!");
			}
		} catch (ClassCastException cce) {
			log.error("CoreMessageDrivenBean must have object payloads of AsynchronousServiceDescriptor - " +
				"the passed object was :" + o.getClass(), cce);
		} catch (JMSException jmse) {
			String msg = "An error was encountered while reading the payload from a message";
			log.error(msg, jmse);
			throw new EJBException(msg, jmse);
			
		}
		return serviceDescriptor;
	}

	/**
	 * @see javax.ejb.MessageDrivenBean#ejbCreate()
	 */
	public void ejbCreate() throws CreateException, EJBException {
	}
	
	/**
	 * @see javax.ejb.MessageDrivenBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException {
	}

	/**
	 * @see javax.ejb.MessageDrivenBean#setMessageDrivenContext(javax.ejb.MessageDrivenContext)
	 */
	public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException {
		this.context = context;
		
	}
	
	

}
