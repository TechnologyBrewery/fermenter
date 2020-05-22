package org.bitbucket.fermenter.stout.config;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

/**
 * Contains configuration options for changing Stout transaction settings.
 **/
@KrauseningSources(value = "stout-transaction.properties")
public interface StoutTransactionConfig extends KrauseningConfig {

    /**
     * Allows the Narayana object store location to be changed. By default, we set it because it is internally
     * inconsistent and results in both an ObjectStore and PutObjectStoreHere.
     * 
     * @return The path to use for the object store, if you choose to override
     */
    @Key(value = "object.store.path")
    @DefaultValue("narayana-object-store")
    String getCustomObjectStorePath();

}
