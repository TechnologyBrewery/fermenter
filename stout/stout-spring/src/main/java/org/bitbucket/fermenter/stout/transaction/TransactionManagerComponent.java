package org.bitbucket.fermenter.stout.transaction;

import javax.transaction.UserTransaction;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple;

@Component
@EnableTransactionManagement
public class TransactionManagerComponent {

    @Bean
    public JtaTransactionManager transactionManager() {
        JtaTransactionManager jtaTxManager = new JtaTransactionManager();

        TransactionManagerImple txManager = new TransactionManagerImple();
        jtaTxManager.setTransactionManager(txManager);

        UserTransaction userTransaction = com.arjuna.ats.jta.UserTransaction.userTransaction();
        jtaTxManager.setUserTransaction(userTransaction);

        return jtaTxManager;
    }
    
}
