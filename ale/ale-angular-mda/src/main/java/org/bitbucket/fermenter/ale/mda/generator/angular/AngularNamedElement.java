package org.bitbucket.fermenter.ale.mda.generator.angular;

import com.google.common.base.CaseFormat;

public interface AngularNamedElement {

    public String getName();
    
    default String getNameLowerHyphen() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, getName());
    }

    default String getNameLowerCamel() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, getName());
    }
}
