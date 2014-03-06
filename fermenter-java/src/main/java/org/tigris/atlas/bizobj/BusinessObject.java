package org.tigris.atlas.bizobj;

import org.tigris.atlas.messages.Messages;
import org.tigris.atlas.transfer.PrimaryKey;

/**
 * This interface specifies required behavior for business objects.
 * 
 * @author Steve Andrews
 * 
 */
public interface BusinessObject {

    /**
     * Generic getter for key field
     * 
     * @return The key field value.
     */
    PrimaryKey getKey();

    /**
     * Generic setter for key field
     * 
     * @param key
     *            The key field value
     */
    void setKey(PrimaryKey key);

    /**
     * Save the state of the entity
     * 
     */
    void save();

    /**
     * Delete the entity
     * 
     */
    void delete();

    /**
     * Intrinsic validations for a validatable object
     * 
     */
    void validate();

    Messages getMessages();

    Messages getAllMessages();

}
