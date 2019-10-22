package org.bitbucket.askllc.fermenter.cookbook.input;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;

public class SimpleDomainInput {

    private String name;
    
    public SimpleDomainBO convertToBO() {
        SimpleDomainBO bo = new SimpleDomainBO();
        bo.setName(name);
        return bo;
    }
    
}
