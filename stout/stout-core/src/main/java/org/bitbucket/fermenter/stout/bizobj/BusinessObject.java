package org.bitbucket.fermenter.stout.bizobj;


/**
 * Specifies required behavior for persistent business objects.
 */
public interface BusinessObject<BO> {

	/**
	 * Performs intrinsic validations on the attributes of this business object.
	 */
	void validate();
}
