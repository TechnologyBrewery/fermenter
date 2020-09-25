package org.bitbucket.fermenter.stout.util;

import java.util.ArrayList;
import java.util.List;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.bitbucket.fermenter.stout.config.QueryConfig;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * Provides common support for limiting queries.
 * 
 */
public final class QueryUtils {

    private static final QueryConfig queryConfig = KrauseningConfigFactory.create(QueryConfig.class);
    public static final int QUERY_LIMIT = queryConfig.getDefaultQueryResultLimit();
    public static final int DEFAULT_PAGE = 0;

    private QueryUtils() {
        // private constructor to prevent instantiation of all static class
    }

    /**
     * Returns a default page that has query limit defined for page zero.
     * 
     * @return default page request
     */
    public static PageRequest getDefaultPageRequest() {
        List<Sort.Order> orders = new ArrayList<>();
        return PageRequest.of(DEFAULT_PAGE, QUERY_LIMIT);
    }

}
