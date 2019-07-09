package org.bitbucket.fermenter.stout.content;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

/**
 * Configuration options for Stout's Content capability via Oak.
 */
@KrauseningSources(value = "stout-content.properties")
public interface ContentConfig extends KrauseningConfig {
    
    /**
     * The username used for the underlying Oak content repository
     * 
     * @return oak username
     */
    @Key("respository.username")
    @DefaultValue("admin")    
    String getRepositoryUsername();
    
    /**
     * The password used for the underlying Oak content repository
     * 
     * @return oak password
     */
    @Key("respository.password")
    @DefaultValue("admin")    
    String getRepositoryPassword();    
    
}
