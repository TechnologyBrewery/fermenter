package org.tigris.atlas.bizobj;

import org.slf4j.Logger;
import org.tigris.atlas.messages.Messages;
import org.tigris.atlas.persist.Dao;
import org.tigris.atlas.transfer.PrimaryKey;

/**
 * Base class for all parent business objects.
 */
public abstract class BaseBO extends ComplexType implements BusinessObject {

    /**
     * {@inheritDoc}
     */
    public void save() {
	validate();
	if (!getAllMessages().hasErrorMessages()) {
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

	Messages allMessages = getAllMessages();
	if (allMessages != null && !allMessages.hasErrorMessages()) {
	    complexValidation();
	    complexValidationOnComposites();

	    if (!allMessages.hasErrorMessages()) {
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

}
