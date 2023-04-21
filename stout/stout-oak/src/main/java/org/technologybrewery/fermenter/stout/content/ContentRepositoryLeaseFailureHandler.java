package org.technologybrewery.fermenter.stout.content;

import org.apache.jackrabbit.oak.plugins.document.LeaseFailureHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles cleaning up the repository when a lease fails. In short, this mans that Oak is trying to check its connection
 * to the database but is not able to connect to the database and renew its "lease". Leases help it speed up document
 * handling via smart caching.
 */
public class ContentRepositoryLeaseFailureHandler implements LeaseFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(ContentRepositoryLeaseFailureHandler.class);

    private ContentRepository repository;

    /**
     * Creates a new lease failure handler with package access to {@link ContentRepository} resources.
     * 
     * @param repository
     *            content repository resources
     */
    public ContentRepositoryLeaseFailureHandler(ContentRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleLeaseFailure() {
        logger.warn(
                "A lease failure has been detected!  Destroying the ContentRepository (will be recreated on demand)!");

        synchronized (repository) {
            repository.cleanUp();
        }

    }

}
