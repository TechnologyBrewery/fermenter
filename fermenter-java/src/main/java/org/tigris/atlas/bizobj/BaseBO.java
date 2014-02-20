package org.tigris.atlas.bizobj;

import org.tigris.atlas.messages.Messages;

/**
 * Base class for all parent business objects.
 */
public abstract class BaseBO extends ComplexType implements BusinessObject {
	
	public BaseBO() {
		super();
	}
	
	/**
	 * Perform simple data entry validation using the transfer object.
	 * 
	 * Validation is performed in two steps.  First, each object in the 
	 * tree has its field validation performed.  This ensures that complex 
	 * validation is performed only if all the business objects in the 
	 * hierarchy contains well-formed values.  As a result, complex validation
	 * logic does not have to worry about the data it is using from a field
	 * validation perspective.
	 * 
	 * Subclasses must provide complex validation logic.
	 */
	public void validate() {
		fieldValidation();
		compositeValidation();
	    referenceValidation();
	    
	    Messages allMessages = getAllMessages(); 
	    if(allMessages != null && !allMessages.hasErrorMessages() ) {
	    	complexValidation();
	    	complexValidationOnComposites();
	    	
	    	if (!allMessages.hasErrorMessages()) {
	    		complexValidationOnChildren();
	    	}
	    }
	}
	
	/**
	 * Lifecycle method to allow for formatting of data fields after validation and prior to saving.
	 * Facilitates data consistency.
	 *
	 */
	protected void normalize() {
		
	}

}
