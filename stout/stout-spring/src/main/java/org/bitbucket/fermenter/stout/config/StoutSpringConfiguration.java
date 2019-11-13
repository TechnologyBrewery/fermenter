package org.bitbucket.fermenter.stout.config;

import org.bitbucket.fermenter.stout.transaction.TransactionManagerComponent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = TransactionManagerComponent.class)
public class StoutSpringConfiguration {

}
