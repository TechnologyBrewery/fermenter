package org.technologybrewery.fermenter.cookbook.input;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.SimpleDomainBO;

public class SimpleDomainInput {

    private String name;
    
    public SimpleDomainBO convertToBO() {
        SimpleDomainBO bo = new SimpleDomainBO();
        bo.setName(name);
        return bo;
    }
    
}
