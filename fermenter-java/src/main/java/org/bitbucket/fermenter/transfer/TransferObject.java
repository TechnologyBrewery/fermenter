package org.bitbucket.fermenter.transfer;

import java.io.Serializable;

/**
 * Transfer objects are used to mobe the state of an entity across application tiers.
 * 
 * @author sandrews
 */
public interface TransferObject extends Serializable {

    /**
     * 
     * @return The logical name of the entity a class represents
     */
    public String getEntityName();

    /**
     * Generic getter for key field
     * 
     * @return The key field value.
     */
    public PrimaryKey getKey();

    /**
     * Generic setter for key field
     * 
     * @param key
     *            The key field value
     */
    public void setKey(PrimaryKey key);
}