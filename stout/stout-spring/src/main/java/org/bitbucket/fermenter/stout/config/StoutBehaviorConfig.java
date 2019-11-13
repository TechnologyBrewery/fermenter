package org.bitbucket.fermenter.stout.config;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

/**
 * Contains configuration options for changing Stout behavior at runtime.
 */
@KrauseningSources(value = "stout-behavior.properties")
public interface StoutBehaviorConfig extends KrauseningConfig {

    /**
     * Whether or not a nonexistent find by primary key *service* call should return an error or just return null
     * without an error. The null return is the legacy approach with Stout while the error approach is more consistent
     * with traditional JEE patterns.
     * 
     * @return true if an error should be created, false if null should be returned without an error
     */
    @Key(value = "should.create.error.on.nonexistent.service.findbyprimarykey")
    @DefaultValue(value = "false")
    boolean shouldCreateErrorOnNonexistentServiceFindByPrimaryKey();

}
