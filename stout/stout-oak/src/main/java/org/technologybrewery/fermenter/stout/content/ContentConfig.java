package org.technologybrewery.fermenter.stout.content;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

/**
 * Configuration options for Stout's Content capability via Oak.
 */
@KrauseningSources(value = "stout-content.properties")
public interface ContentConfig extends KrauseningConfig {
    
    /**
     * The username used for the underlying Oak content repository.
     * 
     * @return oak username
     */
    @Key("respository.username")
    @DefaultValue("admin")    
    String getRepositoryUsername();
    
    /**
     * The password used for the underlying Oak content repository.
     * 
     * @return oak password
     */
    @Key("respository.password")
    @DefaultValue("admin")    
    String getRepositoryPassword();
    
    /**
     * The number of scheduler threads to spin up at load time (they time out after 1 minute).
     * 
     * This is useful as the Oak default is 32, which creates a lot of extraneous threads in normal
     * Stout use cases.
     * 
     * @return number of default scheduler threads
     */
    @Key("number.of.scheduler.threads")
    @DefaultValue("2")    
    int getNumberOfSchedulerThreads();        
    
}
