package org.bitbucket.fermenter.stout.util;

import java.util.Date;

import feign.Param;
public class ToDateExpander implements Param.Expander{
    
    @Override
    public  String expand(Object value) {
        if (value.getClass().equals(java.sql.Date.class)) {
            java.util.Date utilDate = new java.util.Date(((Date) value).getTime());
            return utilDate.toString();
        }
        return value.toString();
    }

}
