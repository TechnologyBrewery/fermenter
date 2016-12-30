package org.bitbucket.fermenter.stout.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.bitbucket.krausening.Krausening;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exposes several beans that are wired up and initialized with the help of {@link Krausening} properties for usage
 * within Spring's application context.
 */
@Configuration
public class KrauseningBasedSpringConfig {

	private String jpaPropertiesFileName;
	private String dataSourcePropertiesFileName;

	protected static final String DEFAULT_JPA_PROPERTIES = "jpa.properties";
	protected static final String DEFAULT_DATA_SOURCE_PROPERTIES = "data-source.properties";

	public KrauseningBasedSpringConfig() {
		this(DEFAULT_JPA_PROPERTIES, DEFAULT_DATA_SOURCE_PROPERTIES);
	}

	public KrauseningBasedSpringConfig(String jpaPropertiesFileName, String dataSourcePropertiesFileName) {
		this.jpaPropertiesFileName = jpaPropertiesFileName;
		this.dataSourcePropertiesFileName = dataSourcePropertiesFileName;
	}

	@Bean
	public DataSource krauseningDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		Properties dataSourceProps = Krausening.getInstance().getProperties(this.dataSourcePropertiesFileName);
		for (String propName : dataSourceProps.stringPropertyNames()) {
			try {
				BeanUtils.setProperty(dataSource, propName, dataSourceProps.getProperty(propName));
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException("Could not set data source property [" + propName + "] to value ["
						+ dataSourceProps.getProperty(propName) + "]");
			}
		}
		return dataSource;
	}

	@Bean
	public Properties krauseningJpaProperties() {
		return Krausening.getInstance().getProperties(this.jpaPropertiesFileName);
	}
}
