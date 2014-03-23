package org.tigris.atlas.transfer;

/**
 * Base class for all parent transfer objects.
 */
public abstract class BaseTO implements TransferObject {

    /**
     * Test for equality
     * 
     * @return boolean - Equal or not
     */
    public boolean equals(Object o) {
	try {
	    return getKey().equals(((TransferObject) o).getKey());
	} catch (ClassCastException cce) {
	    // TODO: log exception
	    return false;
	}
    }

    /**
     * Generate a unique hash
     * 
     */
    public int hashCode() {
	return getKey().hashCode();
    }

}
