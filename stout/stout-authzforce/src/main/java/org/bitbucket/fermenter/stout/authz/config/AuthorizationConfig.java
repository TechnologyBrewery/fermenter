package org.bitbucket.fermenter.stout.authz.config;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

/**
 * Configuration options for authorization within Stout.
 */
@KrauseningSources("stout-authorization.properties")
public interface AuthorizationConfig extends KrauseningConfig {

    /**
     * Location of the pdp.xml file to use for Authzforce Policy Decision Point
     * configuration.
     * 
     * @return path to file in authzforce path naming standards
     */
    @Key("pdp.configuration.location")
    @DefaultValue("classpath:authorization/pdp.xml")
    public String getPdpConfigurationLocation();

    /**
     * Location of the catalog.xml file to use for Authzforce Policy Decision
     * Point configuration.
     * 
     * @return path to file in authzforce path naming standards
     */
    @Key("pdp.catalog.location")
    @DefaultValue("classpath:pdp-ext-catalog.xml")
    public String getPdpCatalogLocation();

    /**
     * Location of the pdp-ext.xsd file to use for Authzforce Policy Decision
     * Point configuration.
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
     * Determines how external schemas should be accessed. This is a comma
     * delimited list, ie "http,file". The list must include http for AuthzCore
     * (see System Requirements here: https://github.com/authzforce/core) so if
     * the default is modified, ensure that http is included.
     * 
     * @return javax.xml.accessExternalSchema scheme
     */
    @Key("javax.xml.accessExternalSchema")
    @DefaultValue("all")
    public String getAccessExternalSchemaType();
    
    /**
     * Returns the clock skew to use for both "not before" and "expiration" times.
     * @return clock skew in seconds.  Defaults to 1 minute.
     */
    @Key("token.skew")
    @DefaultValue("60")
    public long getTokenSkewInSeconds();

    /**
     * Returns the time to add to the current time in order to set an expiration for a token.
     * @return expiration offset (not including skew), in seconds.  Defaults to 1 hour.
     */
    @Key("token.expiration")
    @DefaultValue("3600")
    public long getTokenExpirationInSeconds();
    
    /**
     * Returns the issuer for tokens. 
     * @return token issuer
     */
    @Key("token.issuer")
    public String getTokenIssuer();

    /**
     * Returns alias for private key to be used when signing JWT.
     * @return key alias
     */
    @Key("private.key.alias")
    public String getKeyAlias();

    /**
     * Returns system path to keystore.
     * @return keystore location
     */
    @Key("keystore.location")
    @DefaultValue("javax.net.ssl.keyStore")
    public String getKeyStoreLocation();

    /**
     * Returns system path to keystore password. It is assumed that this password will match the private key password.
     * @return keystore / private key password location
     */
    @Key("keystore.password.location")
    @DefaultValue("javax.net.ssl.keyStorePassword")
    public String getKeyStorePasswordLocation();

    /**
     * Returns keystore type.
     * @return default type of keystore
     */
    @Key("keystore.type")
    @DefaultValue("JKS")
    public String getKeyStoreType();
    
    /**
     * Returns the number of minutes before expiration for a policy decision (after it is added to cache).
     * @return expiration time, in minutes.  Defaults to 5 minutes.
     */
    @Key("decision.cache.expiration")
    @DefaultValue("5")
    public long getDecisionCacheExpirationInMinutes();
    
    /**
     * Returns the number of minutes before expiration for a attribute value (after it is added to cache).
     * @return expiration time, in minutes.  Defaults to 5 minutes.
     */
    @Key("atttribute.cache.expiration")
    @DefaultValue("5")
    public long getAttributeCacheExpirationInMinutes();
    
}
