package org.tigris.atlas.messages;

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
