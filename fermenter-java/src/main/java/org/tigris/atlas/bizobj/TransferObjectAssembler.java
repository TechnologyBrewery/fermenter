package org.tigris.atlas.bizobj;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.tigris.atlas.transfer.TransferObject;

/**
 * Provides default transfer object assembler behavior.
 */
public abstract class TransferObjectAssembler {

	/**
	 * Returns the subclasses {@link Logger} instance to allow for more granular logging.
	 * @return {@link Logger}
	 */
	protected abstract Logger getLog();

	/**
	 * Sets oplock on the the transfer object.
	 * @param to The object on which the oplock should be set
	 * @param oplockValue The oplock value to set
	 */
	protected void setOplockOnTransferObject(TransferObject to, Integer oplockValue) {
		Class toClass = to.getClass();
		Class[] paramClasses  = {Integer.class};
		Object[] paramInstanes = {oplockValue};

		try {
			Method m = toClass.getDeclaredMethod("setOplock", paramClasses);
			m.setAccessible(true);
			m.invoke(to, paramInstanes);
		} catch (SecurityException e) {
			getLog().error("A security exception was encountered while setting oplock!", e);
		} catch (IllegalArgumentException e) {
			getLog().error("An illegal argument exception was encountered while setting oplock!", e);
		} catch (NoSuchMethodException e) {
			getLog().error("Setter method exception was encountered while setting oplock!", e);
		} catch (IllegalAccessException e) {
			getLog().error("An illegal access exception was encountered while setting oplock!", e);
		} catch (InvocationTargetException e) {
			getLog().error("An invocationx exception was encountered while setting oplock!", e);
		}
	}

}
