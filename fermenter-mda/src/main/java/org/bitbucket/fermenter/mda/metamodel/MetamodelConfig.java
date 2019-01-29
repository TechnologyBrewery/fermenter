package org.bitbucket.fermenter.mda.metamodel;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

/**
 * Configuration values for the metamodel processing.
 */
@KrauseningSources("metamodel.properties")
public interface MetamodelConfig extends KrauseningConfig {

    /**
     * Returns the locations within src/main/resources where enumeration model instances can be found.
     * @return path location to enumerations
     */
    @Key("enumerations.relative.path")
    @DefaultValue("enumerations")
    public String getEnumerationsRelativePath();
    
    /**
     * Returns the locations within src/main/resources where service model instances can be found.
     * @return path location to services
     */
    @Key("services.relative.path")
    @DefaultValue("services")
    public String getServicesRelativePath();    
    
    /**
     * Returns the locations within src/main/resources where entity model instances can be found.
     * @return path location to entities
     */
    @Key("entities.relative.path")
    @DefaultValue("entities")
    public String getEntitiesRelativePath();    
    
    /**
     * Returns the locations within src/main/resources where dictionary type model instances can be found.
     * @return path location to dictionary types
     */
    @Key("dictionary-types.relative.path")
    @DefaultValue("dictionaryTypes")
    public String getDictionaryTypesRelativePath();    
    
    /**
     * Returns the metadata resolver to use to lookup metadata.
     * @return url resolver class
     */
    @Key("metadata.url.resolver")
    @DefaultValue("org.bitbucket.fermenter.mda.metamodel.DefaultUrlResolver")
    public String getUrlResolver();
    
}
