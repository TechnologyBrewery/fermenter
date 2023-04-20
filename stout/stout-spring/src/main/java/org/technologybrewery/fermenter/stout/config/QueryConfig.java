package org.technologybrewery.fermenter.stout.config;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

/**
 * Provides configurations relating to query set up and performance.
 */
@KrauseningSources(value = "query.properties")
public interface QueryConfig extends KrauseningConfig {

    /**
     * The default number of records that an unconstrained query should return
     * to ensure that performance is reasonable. We typically want to use this
     * via the Spring Data Pageable interface to only return up to this
     * configuration value of records.
     * 
     * @return default query result limit in number of records
     */
    @Key(value = "default.query.result.limit")
    @DefaultValue(value = "100")
    int getDefaultQueryResultLimit();

}
