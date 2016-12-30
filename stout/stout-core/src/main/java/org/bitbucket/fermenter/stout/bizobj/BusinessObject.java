package org.bitbucket.fermenter.stout.bizobj;

import java.io.Serializable;

/**
 * Specifies required behavior for persistent business objects.
 */
public interface BusinessObject<PK extends Serializable, BO> {

	/**
	 * Retrieves the persistent primary key that uniquely identifies this business object.
	 * 
	 * @return
	 */
	PK getKey();

	/**
	 * Sets the persistent primary key that uniquely identifies this business object.
	 * 
	 * @param key
	 */
	void setKey(PK key);

	/**
	 * Saves this business object to the desired persistence store and returns it.
	 */
	BO save();

	/**
	 * Deletes this business object.
	 */
	void delete();

	/**
	 * Performs intrinsic validations on the attributes of this business object.
	 */
	void validate();
}
