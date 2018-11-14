package org.bitbucket.fermenter.stout.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.stereotype.Component;

/**
 * Custom scope for cucumber testing to mimic a request scope.
 */
@Component
public class MockRequestScope extends SimpleThreadScope {

    private static final String SCOPED_TARGET_REQUEST_SCOPED_MESSAGES = "scopedTarget.requestScopedMessages";

    private static final Logger LOGGER = LoggerFactory.getLogger(MockRequestScope.class);

    // CANNOT USE THIS CLASS FOR MULTI-THREADED TESTING BECAUSE OF THIS:
    private static boolean cleanMessagesOnNextGet = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        super.registerDestructionCallback(name, callback);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object remove(String name) {
        Object removedObject = super.remove(name);
        if (removedObject != null) {
            LOGGER.debug("MessageManager cleaned up within " + this.getClass().getSimpleName());

        } else {
            LOGGER.warn("Expecting MessageManager to be cleaned up within " + this.getClass().getSimpleName()
                    + ", but it was either not found or not in the correct scope!");

        }
        return removedObject;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        // This is delayed because Spring scopes the MessageManager within a Service call, so
        // it will be the next request, not the current one:
        if (cleanMessagesOnNextGet && SCOPED_TARGET_REQUEST_SCOPED_MESSAGES.equals(name)) {
            remove(name);
            cleanMessagesOnNextGet = false;
        }
        return super.get(name, objectFactory);
    }

    /**
     * Marks this scope to clear out the {@link MessageManager}.
     */
    public void cleanMessageManager() {
        cleanMessagesOnNextGet = true;
    }

}