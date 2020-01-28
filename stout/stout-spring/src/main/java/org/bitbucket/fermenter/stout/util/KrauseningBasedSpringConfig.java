package org.bitbucket.fermenter.stout.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.sql.DataSource;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.bitbucket.fermenter.stout.exception.UnrecoverableException;
import org.bitbucket.krausening.Krausening;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exposes several beans that are wired up and initialized with the help of {@link Krausening} properties for usage
 * within Spring's application context.
 */
public class KrauseningBasedSpringConfig {

    private static final Logger logger = LoggerFactory.getLogger(KrauseningBasedSpringConfig.class);

    protected String jpaPropertiesFileName;
    protected String dataSourcePropertiesFileName;

    protected static final String DEFAULT_JPA_PROPERTIES = "jpa.properties";
    protected static final String DEFAULT_DATA_SOURCE_PROPERTIES = "data-source.properties";

    /**
     * Configures this class w/ the default jpa.properties and data-source.properties Krausening files.
     */
    public KrauseningBasedSpringConfig() {
        this(DEFAULT_JPA_PROPERTIES, DEFAULT_DATA_SOURCE_PROPERTIES);
    }

    /**
     * Allows you to override the krausening profile file names used to configure JPA and the data source for this
     * domain.
     */
    public KrauseningBasedSpringConfig(String jpaPropertiesFileName, String dataSourcePropertiesFileName) {
        this.jpaPropertiesFileName = jpaPropertiesFileName;
        this.dataSourcePropertiesFileName = dataSourcePropertiesFileName;
    }

    /**
     * Configures the datasource used for this domain.
     * 
     * @return a datasource
     */
    public DataSource krauseningDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        Properties dataSourceProps = Krausening.getInstance().getProperties(this.dataSourcePropertiesFileName);
        if (dataSourceProps == null) {
            logger.error(
                    "Could not find properties for {}!  You emf bean will not be able to load without these properties! Using defaults instead...",
                    this.dataSourcePropertiesFileName);
            dataSourceProps = new Properties();
        }

        DataSourceConfig config = KrauseningConfigFactory.create(DataSourceConfig.class, dataSourcePropertiesFileName,
                System.getProperties());
        String interleavedUrl = config.getUrl();

        if (interleavedUrl != null) {
            dataSourceProps.put("url", interleavedUrl);
        }

        for (String propName : dataSourceProps.stringPropertyNames()) {
            try {
                BeanUtils.setProperty(dataSource, propName, dataSourceProps.getProperty(propName));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new UnrecoverableException("Could not set data source property [" + propName + "] to value ["
                        + dataSourceProps.getProperty(propName) + "]");
            }
        }

        return dataSource;
    }

    /**
     * Configures the JPA properties used for this domain.
     * @return properties for JPA
     */     
    public Properties krauseningJpaProperties() {
        return Krausening.getInstance().getProperties(this.jpaPropertiesFileName);
    }

}
