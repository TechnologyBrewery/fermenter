package org.bitbucket.fermenter.stout.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.aeonbits.owner.KrauseningConfig.KrauseningSources;
import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.bitbucket.krausening.Krausening;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exposes several beans that are wired up and initialized with the help of {@link Krausening} properties for usage
 * within Spring's application context.
 */
@Configuration
public class KrauseningBasedSpringConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(KrauseningBasedSpringConfig.class);

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
		
		alterPropertyAnnonationName(this.dataSourcePropertiesFileName);
		DataSourceConfig config = KrauseningConfigFactory.create(DataSourceConfig.class, System.getProperties());
		String interleavedUrl = config.getUrl();	
		
		if (interleavedUrl != null) {
			dataSourceProps.put("url", interleavedUrl);
		}
		
		if (dataSourceProps != null) {
        		for (String propName : dataSourceProps.stringPropertyNames()) {
        			try {
        				BeanUtils.setProperty(dataSource, propName, dataSourceProps.getProperty(propName));
        			} catch (IllegalAccessException | InvocationTargetException e) {
        				throw new RuntimeException("Could not set data source property [" + propName + "] to value ["
        						+ dataSourceProps.getProperty(propName) + "]");
        			}
        		}
        		
		} else {
		    logger.error("Could not find properties for {}!", this.dataSourcePropertiesFileName);
		    
		}
	
		return dataSource;
	}

	@Bean
	public Properties krauseningJpaProperties() {
		return Krausening.getInstance().getProperties(this.jpaPropertiesFileName);
	}
	
	private static void alterPropertyAnnonationName(String dataSourcePropertiesFileName) {
		try {
			ExtendedKrauseningSources updatedSources = new ExtendedKrauseningSources(dataSourcePropertiesFileName);
			
            Method method = Class.class.getDeclaredMethod("annotationData", null);
            method.setAccessible(true);
            Object annotationData = method.invoke(DataSourceConfig.class);
            Field annotations = annotationData.getClass().getDeclaredField("annotations");
            annotations.setAccessible(true);
            Map<Class<? extends Annotation>, Annotation> map =
                (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);
            map.put(KrauseningSources.class, updatedSources);
        } catch (Exception e) {
            throw new RuntimeException("Could not update " + DataSourceConfig.class.getSimpleName() + "KrauseningSources property!", e);
        }
	}
	
}
