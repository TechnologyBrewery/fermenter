package org.bitbucket.fermenter.stout.bizobj;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.bitbucket.fermenter.stout.messages.CoreMessages;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageUtils;
import org.slf4j.Logger;

/**
 * Provides a base implementation of a business object that delegates to JSR-303 for object validation.
 *
 * @param <BO>
 */
public abstract class BaseSpringBO<BO> implements BusinessObject<BO> {

    @Inject
    private Validator validator;

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
        validateFields();
        validateReferences();
        validateRelations();

        if (!MessageManager.hasErrorMessages()) {
            complexValidation();
            complexValidationOnRelations();
        }
    }

    /**
     * Lifecycle method that is invoked before this business object's pre-save validation occurs. This method can be
     * overridden by concrete business object implementations to perform any needed data normalization functionality,
     * such as setting computed properties (such as the object's last updated date).
     */
    protected void preValidate() {

    }

    /**
     * Lifecycle method that is invoked after this business object's pre-save validation occurs. This method will *ONLY*
     * be invoked if validation successfully proceeds without any errors.
     */
    protected void postValidate() {

    }

    protected abstract Logger getLogger();

    /**
     * Uses JSR-303 annotations generated on to concrete business object implementations to perform field-level
     * validation. Any detected validation errors are added to the {@link MessageManager}.
     */
    public void validateFields() {
        Set<ConstraintViolation<BO>> violations = getValidator().validate((BO) this);
        for (ConstraintViolation<BO> violation : violations) {
            addConstraintViolationToMsgMgr(violation);
        }
    }
    

	/**
	 * Adds the given {@link ConstraintViolation} as an error message to the {@link MessageManager}.
	 *
	 * @param violation
	 *            JSR-303 violation to record in the {@link MessageManager}.
	 */
	protected void addConstraintViolationToMsgMgr(ConstraintViolation<BO> violation) {
		String invalidPropertyName = violation.getPropertyPath().toString();
		MessageManager.addMessage(MessageUtils.createErrorMessage(CoreMessages.INVALID_FIELD,
				new String[] { invalidPropertyName }, new Object[] { invalidPropertyName, violation.getMessage() }));

	}
	
	protected void checkIfReferenceExists(Object reference) {
		if(reference == null) {
	    		MessageManager.addMessage(MessageUtils.createErrorMessage(CoreMessages.INVALID_REFERENCE,
			new String[] { "Invalid reference" }, 
	         new Object[] { "Invalid reference" , "Invalid reference, the reference does not exist"  }));
		}
	}
	
	protected abstract void validateReferences();
	
    protected abstract void validateRelations();

    protected abstract void complexValidation();

    protected abstract void complexValidationOnRelations();

    protected Validator getValidator() {
        return this.validator;
    }
    
}
