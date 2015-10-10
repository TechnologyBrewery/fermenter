package org.bitbucket.fermenter.stout.messages;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.bitbucket.fermenter.stout.bizobj.BusinessObject;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.bitbucket.fermenter.stout.transfer.TransferObject;

/**
 * Interceptor that appropriately configures and cleans up the {@link MessageManager} prior to business service
 * invocation such that objects that are not container managed, such as {@link BusinessObject}s and
 * {@link TransferObject}s, are provided with a straightforward mechanism for creating and propagating {@link Messages}
 * objects.
 */
@Interceptor
@ThreadLocalMessages
public class ThreadLocalMessageManagementInterceptor {

	@Inject
	private InjectableMessages messages;

	@AroundInvoke
	public Object manageMessagesInThreadLocal(InvocationContext context) throws Exception {
		MessageManager.initialize(messages);
		Object result = context.proceed();
		MessageManager.cleanup();
		return result;
	}
}
