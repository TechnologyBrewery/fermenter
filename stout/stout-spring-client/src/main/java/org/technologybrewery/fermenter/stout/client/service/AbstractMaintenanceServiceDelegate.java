package org.technologybrewery.fermenter.stout.client.service;

import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Provides common functionality for service delegates.
 */
public abstract class AbstractMaintenanceServiceDelegate implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMaintenanceServiceDelegate.class);

    private TransactionManager transactionManager;

    /**
     * {@inheritDoc}
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        try {
            JtaTransactionManager springTxManager = applicationContext.getBean(JtaTransactionManager.class);
            transactionManager = springTxManager.getTransactionManager();

        } catch (NoSuchBeanDefinitionException e) {
            logger.info("Not in a transactional context, level one rest client caching disabled.");

        }
    }

    /**
     * Determines if we are in a context where the level 1 cache is active.
     * 
     * @return true if active, false otherwise
     */
    protected boolean checkLevelOneCache() {
        boolean useLevelOneCache = false;

        try {
            useLevelOneCache = (transactionManager != null && transactionManager.getTransaction() != null);

        } catch (SystemException e) {
            logger.error("TransactionManager is present but fails to return current transaction!", e);

        }

        return useLevelOneCache;
    }

    /**
     * Flushes the transactionally-cached create, update associated with this delegate. Separate from delete as the
     * often need to happen in reverse order for referential integrity.
     */
    public abstract void flushInsertsAndUpdates();

    /**
     * Flushes the deletes associated with this delegate. Separate from insert and update as the often need to happen in
     * reverse order for referential integrity.
     */
    public abstract void flushDeletes();

}
