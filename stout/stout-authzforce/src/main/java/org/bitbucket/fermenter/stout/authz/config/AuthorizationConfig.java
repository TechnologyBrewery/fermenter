package org.bitbucket.fermenter.stout.authz.config;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

/**
 * Configuration options for authorization within Stout.
 */
@KrauseningSources("stout-authorization.properties")
public interface AuthorizationConfig extends KrauseningConfig {

    /**
     * Location of the pdp.xml file to use for Authzforce Policy Decision Point configuration.
     * 
     * @return path to file in authzforce path naming standards
     */
    @Key("pdp.configuration.location")
    @DefaultValue("classpath:authorization/pdp.xml")
    public String getPdpConfigurationLocation();

    /**
     * Location of the catalog.xml file to use for Authzforce Policy Decision Point configuration.
     * 
     * @return path to file in authzforce path naming standards
     */
    @Key("pdp.catalog.location")
    @DefaultValue("classpath:pdp-ext-catalog.xml")
    public String getPdpCatalogLocation();

    /**
     * Location of the pdp-ext.xsd file to use for Authzforce Policy Decision Point configuration.
     * 
     * @return path to file in authzforce path naming standards
     */
    @Key("pdp.extension.xsd.location")
    @DefaultValue("classpath:pdp-ext.xsd")
    public String getPdpExtensionXsdLocation();

    /**
     * Directory in which to look for attribute definition json files.
     * 
     * @return directory path
     */
    @Key("attribute.definition.location")
    @DefaultValue("authorization/attributes")
    public String getAttributeDefinitionLocation();

    /**
     * Determines how external schemas should be accessed.
     * 
     * @return javax.xml.accessExternalSchema scheme
     */
    @Key("javax.xml.accessExternalSchema")
    @DefaultValue("http")
    public String getAccessExternalSchemaType();

}
