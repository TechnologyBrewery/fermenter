package org.bitbucket.askllc.fermenter.cookbook.domain.transfer;

import java.util.Date;

/**
 * Transfer object for the SimpleDomain entity.
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.transfer.SimpleDomainBase
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
public class SimpleDomain extends SimpleDomainBase{

    private String largeString;

    private Date updatedAt;

    public String getLargeString() {
        return largeString;
    }

    public void setLargeString(String largeString) {
        this.largeString = largeString;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
}