package org.bitbucket.fermenter.stout.bizobj;

import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.persist.Dao;
import org.bitbucket.fermenter.stout.transfer.PrimaryKey;
import org.slf4j.Logger;

/**
 * Base class for all parent business objects.
 */
public abstract class BaseBO implements BusinessObject {

	/**
	 * {@inheritDoc}
	 */
	public void save() {
		validate();
		if (!MessageManager.hasErrorMessages()) {
			normalize();
			Dao<BusinessObject, PrimaryKey> dao = getDao();
			dao.save(this);
		} else {
			if (getLogger().isWarnEnabled()) {
				getLogger().warn(
						"Attempt to save BO of type [" + this.getClass() + "] with PK = [" + this.getKey().getValue()
								+ "] was ignored due to collected errors");
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void delete() {
		Dao<BusinessObject, PrimaryKey> dao = getDao();
		dao.delete(this.getKey());
	}

	/**
	 * Perform simple data entry validation using the transfer object.
	 * 
	 * Validation is performed in two steps. First, each object in the tree has its field validation performed. This
	 * ensures that complex validation is performed only if all the business objects in the hierarchy contains
	 * well-formed values. As a result, complex validation logic does not have to worry about the data it is using from
	 * a field validation perspective.
	 * 
	 * Subclasses must provide complex validation logic.
	 */
	public void validate() {
		fieldValidation();
		compositeValidation();
		referenceValidation();

		if (!MessageManager.hasErrorMessages()) {
			complexValidation();
			complexValidationOnComposites();

			if (!MessageManager.hasErrorMessages()) {
				complexValidationOnChildren();
			}
		}
	}

	/**
	 * Lifecycle method to allow for formatting of data fields after validation and prior to saving. Facilitates data
	 * consistency.
	 * 
	 */
	protected void normalize() {

	}

	protected abstract <D extends Dao<? extends BusinessObject, ? extends PrimaryKey>> D getDao();

	protected abstract Logger getLogger();

	protected abstract void fieldValidation();

	protected abstract void compositeValidation();

	protected abstract void referenceValidation();

	protected abstract void complexValidation();

	protected abstract void complexValidationOnChildren();

	protected abstract void complexValidationOnComposites();

}
