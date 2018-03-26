package org.bitbucket.fermenter.stout.mda;

import org.apache.commons.lang.StringUtils;

public interface JavaNamespacedElement {

    String getName();

    default String getCapitalizedName() {
        return StringUtils.uncapitalize(getName());
    }

    default String getUncapitalizedName() {
        return StringUtils.uncapitalize(getName());
    }

}
