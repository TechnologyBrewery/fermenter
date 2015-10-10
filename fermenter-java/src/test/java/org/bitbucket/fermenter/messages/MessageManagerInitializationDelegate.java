package org.bitbucket.fermenter.messages;

import org.bitbucket.fermenter.messages.DefaultMessages;
import org.bitbucket.fermenter.messages.MessageManager;

/**
 * Helper class that can be used to perform {@link MessageManager} initialization and cleanup tasks in unit tests that
 * would be otherwise performed by the application server through {@link ThreadLocalMessageManagementInterceptor}.
 */
public class MessageManagerInitializationDelegate {

    public static void initializeMessageManager() {
	MessageManager.initialize(new DefaultMessages());
    }

    public static void cleanupMessageManager() {
	MessageManager.cleanup();
    }
}
