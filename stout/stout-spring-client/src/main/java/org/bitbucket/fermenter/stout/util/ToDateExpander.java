package org.bitbucket.fermenter.stout.util;

import java.util.Date;
import feign.Param;

/**
 * Parameters annotated with Param expand based on their toString().
 * Custom expander ToDateExpander can be specified to convert java.sql.Date to java.util.Date
 * before expanding it into a string using the expand method.
 * If the result is null or an empty string, the value is omitted.
 */
public class ToDateExpander implements Param.Expander{
    
    @Override
    public  String expand(Object value) {
        if ((java.sql.Date.class).equals(value.getClass())) {
            java.util.Date utilDate = new java.util.Date(((Date) value).getTime());
            return utilDate.toString();
        }
        return value.toString();
    }

}
