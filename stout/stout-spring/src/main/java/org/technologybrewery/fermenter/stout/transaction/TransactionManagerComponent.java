package org.technologybrewery.fermenter.stout.transaction;

import javax.transaction.UserTransaction;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.technologybrewery.fermenter.stout.config.StoutTransactionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;

@Component
@EnableTransactionManagement
public class TransactionManagerComponent {

    private static final Logger logger = LoggerFactory.getLogger(TransactionManagerComponent.class);

    @Bean
    public JtaTransactionManager transactionManager() {

        overrideObjectStoreLocation();

        JtaTransactionManager jtaTxManager = new JtaTransactionManager();

        TransactionManagerImple txManager = new TransactionManagerImple();
        jtaTxManager.setTransactionManager(txManager);

        UserTransaction userTransaction = com.arjuna.ats.jta.UserTransaction.userTransaction();
        jtaTxManager.setUserTransaction(userTransaction);

        return jtaTxManager;
    }

    /**
     * The Narayana object store configuration is a bit difficult to set - this allows a Krausening value to be read and
     * used to override the appropriate Object Store location without having to work through settings that only seem to
     * half fix the problem when using the out of the box approach. This mimics how Red Hat products seems to override
     * this setting, so it's not just us.
     */
    protected void overrideObjectStoreLocation() {
        StoutTransactionConfig config = KrauseningConfigFactory.create(StoutTransactionConfig.class);

        String customObjectStorePath = config.getCustomObjectStorePath();

        ObjectStoreEnvironmentBean defaultActionStoreObjectStoreEnvironmentBean = BeanPopulator
                .getDefaultInstance(ObjectStoreEnvironmentBean.class, null);
        defaultActionStoreObjectStoreEnvironmentBean.setObjectStoreDir(customObjectStorePath);

        ObjectStoreEnvironmentBean communicationStoreObjectStoreEnvironmentBean = BeanPopulator
                .getNamedInstance(ObjectStoreEnvironmentBean.class, "communicationStore");
        communicationStoreObjectStoreEnvironmentBean.setObjectStoreDir(customObjectStorePath);

        logger.debug("Set custom object store path to {}", customObjectStorePath);
    }

}
