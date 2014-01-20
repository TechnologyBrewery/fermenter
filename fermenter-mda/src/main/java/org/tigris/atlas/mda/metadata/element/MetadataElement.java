package org.tigris.atlas.mda.metadata.element;

/**
 * Rights now this looks like an interface, but more common methods can be pulled
 * up in the fufure...
 */
public abstract class MetadataElement {

	/**
	 * Ensure that the this metdata elements is in a valid
	 * form
	 */
	public abstract void validate();

}
