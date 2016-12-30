package org.bitbucket.fermenter.stout.bizobj;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.bitbucket.fermenter.stout.messages.CoreMessages;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageUtils;
import org.slf4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Provides a base implementation of a business object that delegates to Spring Data JPA for persistence and JSR-303 for
 * object validation.
 *
 * @param <PK>
 * @param <BO>
 */
public abstract class BaseSpringBO<PK extends Serializable, BO, JPA extends JpaRepository<BO, PK>> implements BusinessObject<PK, BO> {

	@Inject
	private Validator validator;

	/**
	 * Transient ID that is not persistent but guaranteed to be non-null during an object's lifecycle and thereby usable
	 * in hashCode() and equals() methods as a fallback for scenarios where an entity may not have a non-null PK value.
	 */
	protected final UUID internalTransientID = UUID.randomUUID();

	/**
	 * {@inheritDoc}
	 */
	public BO save() {
		preValidate();
		validate();
		if (!MessageManager.hasErrorMessages()) {
			postValidate();
			BO persistedBizObj = getRepository().save((BO) this);
			postSave();
			return persistedBizObj;
		} else {
			if (getLogger().isWarnEnabled()) {
				getLogger().warn("Attempt to save BO of type [" + this.getClass() + "] with PK = [" + this.getKey()
						+ "] was ignored due to collected errors");
			}
			return (BO) this;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void delete() {
		getRepository().delete((BO) this);
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
		validateFields();
		validateRelations();

		if (!MessageManager.hasErrorMessages()) {
			complexValidation();
			complexValidationOnRelations();
		}
	}

	protected abstract JPA getRepository();

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

	/**
	 * Lifecycle method that is invoked after this business object has been persisted. Developers may override this
	 * method in concrete business implementations to do things like broadcasting a notification after an object has
	 * been successfully saved.
	 */
	protected void postSave() {

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
		MessageManager.addMessage(MessageUtils.createErrorMessage(CoreMessages.INVALID_FIELD, new String[] {},
				new Object[] { violation.getPropertyPath().toString(), violation.getMessage() }));

	}

	protected abstract void validateRelations();

	protected abstract void complexValidation();

	protected abstract void complexValidationOnRelations();

	protected Validator getValidator() {
		return this.validator;
	}

	@Override
	public boolean equals(Object o) {
		boolean areEqual = false;

		try {
			BaseSpringBO thatBizObj = (BaseSpringBO) o;

			PK thisPk = getKey();
			PK thatPk = (PK) (thatBizObj != null ? thatBizObj.getKey() : null);
			if (thatBizObj != null && thisPk == null && thatPk == null) {
				return this.internalTransientID == thatBizObj.internalTransientID;
			} else if (thisPk == thatPk || (thisPk.equals(thatPk))) {
				areEqual = true;
			}
		} catch (ClassCastException ex) {
			areEqual = false;
		}

		return areEqual;
	}

	@Override
	public int hashCode() {
		return (getKey() == null) ? internalTransientID.hashCode() : getKey().hashCode();
	}
}
