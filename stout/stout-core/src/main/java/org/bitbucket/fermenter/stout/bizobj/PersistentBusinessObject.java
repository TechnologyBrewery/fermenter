package org.bitbucket.fermenter.stout.bizobj;

import java.io.Serializable;

/**
 * Specifies required behavior for persistent business objects.
 */
public interface PersistentBusinessObject<PK extends Serializable, BO> extends BusinessObject<BO> {

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
	 * {@inheritDoc}
	 */
	void validate();
}
