package org.technologybrewery.fermenter.stout.client.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.technologybrewery.fermenter.stout.exception.UnrecoverableException;
import org.technologybrewery.fermenter.stout.util.SpringAutowiringUtil;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Manages level one caching for entity maintenance services.
 *
 * @param <K>
 *            primary key class
 * @param <T>
 *            transfer object class
 */
public class LevelOneCacheManager<K, T> {

    private Map<Transaction, LevelOneCache<K, T>> levelOneCacheByTransaction = new ConcurrentHashMap<>();

    @Inject
    private JtaTransactionManager springTxManager;

    /**
     * New instance.
     */
    public LevelOneCacheManager() {
        SpringAutowiringUtil.autowireBizObj(this);
    }

    /**
     * Gets a the level one cache for the primary key/entity combination on the current transaction. If no cache is
     * found, one will be created and bound to the transaction.
     * 
     * @return new level one cache
     */
    public LevelOneCache<K, T> getCacheForCurrentTransaction() {
        Transaction currentTransaction = getCurrentTransaction();
        LevelOneCache<K, T> cacheForCurrentTransaction = levelOneCacheByTransaction.get(currentTransaction);

        if (cacheForCurrentTransaction == null) {
            cacheForCurrentTransaction = new LevelOneCache<>();
            levelOneCacheByTransaction.put(currentTransaction, cacheForCurrentTransaction);
        }

        return cacheForCurrentTransaction;
    }

    private Transaction getCurrentTransaction() {
        Transaction currentTransaction = null;
        try {
            if (springTxManager == null) {
                SpringAutowiringUtil.autowireBizObj(this);
                
            }
            TransactionManager transactionManager = springTxManager.getTransactionManager();
            currentTransaction = transactionManager.getTransaction();

        } catch (SystemException e) {
            throw new UnrecoverableException("Could not get transaction for current thread!", e);

        }
        return currentTransaction;
    }

    /**
     * Removes all level one caches bound to the current transaction.
     */
    public void removeTransaction() {
        Transaction currentTransaction = getCurrentTransaction();
        levelOneCacheByTransaction.remove(currentTransaction);

    }

}
