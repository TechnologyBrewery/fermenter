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
	 * @return The key field value.
	 */
	public PrimaryKey getKey();
	
	/**
	 * Generic setter for key field
	 * @param key The key field value
	 */
	public void setKey(PrimaryKey key);
	
	/**
	 * Save the state of the entity
	 *
	 */
	public void save();
	
	/**
	 * Delete the entity
	 *
	 */
	public void delete();
    
    /**
     * Intrinsic validations for a validatable object
     *
     */
    public void validate();
    
    public Messages getMessages();
    
    public Messages getAllMessages();
   
}
