package org.bitbucket.fermenter.bizobj;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bitbucket.fermenter.transfer.TransferObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides default transfer object assembler behavior.
 */
public abstract class TransferObjectAssembler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferObjectAssembler.class);
	
	/**
	 * Sets oplock on the the transfer object.
	 * @param to The object on which the oplock should be set
	 * @param oplockValue The oplock value to set
	 */
	protected static void setOplockOnTransferObject(TransferObject to, Integer oplockValue) {
		Class<?> toClass = to.getClass();
		Class<?>[] paramClasses  = {Integer.class};
		Object[] paramInstanes = {oplockValue};

		try {
			Method m = toClass.getDeclaredMethod("setOplock", paramClasses);
			m.setAccessible(true);
			m.invoke(to, paramInstanes);
		} catch (SecurityException e) {
			LOGGER.error("A security exception was encountered while setting oplock!", e);
		} catch (IllegalArgumentException e) {
			LOGGER.error("An illegal argument exception was encountered while setting oplock!", e);
		} catch (NoSuchMethodException e) {
			LOGGER.error("Setter method exception was encountered while setting oplock!", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("An illegal access exception was encountered while setting oplock!", e);
		} catch (InvocationTargetException e) {
			LOGGER.error("An invocation exception was encountered while setting oplock!", e);
		}
	}

}
