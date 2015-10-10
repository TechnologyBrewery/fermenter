package org.bitbucket.fermenter.test.domain.bizobj;

import org.bitbucket.fermenter.test.domain.transfer.ValidationReferenceExample;

import org.bitbucket.fermenter.test.domain.transfer.ValidationReferencedObject;
import org.bitbucket.fermenter.test.domain.transfer.ValidationReferencedObjectPK;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bitbucket.fermenter.bizobj.TransferObjectAssembler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transfer object assembler for the ValidationReferenceExample application entity. This class is used create new
 * instances of {@link ValidationReferenceExample} from an instance of a/an {@link ValidationReferenceExampleBO} or the
 * inverse. It is also used to merge an instance of a/an ValidationReferenceExampleBO with the values of a
 * ValidationReferenceExample instance.
 * 
 * Generated Code - DO NOT MODIFY
 */
public final class ValidationReferenceExampleAssembler extends TransferObjectAssembler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationReferenceExampleAssembler.class);

	private ValidationReferenceExampleAssembler() {
		// prevent instantiation
	}

	/**
	 * Given a list of {@link ValidationReferenceExampleBO} instances, create a list of
	 * {@link ValidationReferenceExample} instances. This will iterate over the list of
	 * {@link ValidationReferenceExampleBO} instances, create a(n) {@link ValidationReferenceExample} instance from the
	 * {@link ValidationReferenceExampleBO} instance, and add the new {@link ValidationReferenceExample} instance to the
	 * result list.
	 */
	public static List<ValidationReferenceExample> getValidationReferenceExampleList(
			Collection<ValidationReferenceExampleBO> bos) {
		if (bos == null) {
			return null;
		}

		List<ValidationReferenceExample> tos = new ArrayList<ValidationReferenceExample>(bos.size());
		for (ValidationReferenceExampleBO bo : bos) {
			tos.add(createValidationReferenceExample(bo));
		}

		return tos;
	}

	/**
	 * Given a list of {@link ValidationReferenceExample} instances, create a list of
	 * {@link ValidationReferenceExampleBO} instances. This will iterate over the list of
	 * {@link ValidationReferenceExample} instances, create a(n) {@link ValidationReferenceExampleBO} instance from the
	 * {@link ValidationReferenceExample} instance, and add the new {@link ValidationReferenceExampleBO} instance to the
	 * result list.
	 */
	public static List<ValidationReferenceExampleBO> getValidationReferenceExampleBOList(
			Collection<ValidationReferenceExample> tos) {
		if (tos == null) {
			return null;
		}

		List<ValidationReferenceExampleBO> bos = new ArrayList<ValidationReferenceExampleBO>(tos.size());
		for (ValidationReferenceExample to : tos) {
			bos.add(mergeValidationReferenceExampleBO(to, null));
		}

		return bos;
	}

	/**
	 * Creates a new transfer object based on the passed business object, including children.
	 * 
	 * @param bo
	 *            The {@link ValidationReferenceExampleBO} that should be made transportable
	 * @return The transportable version of the passed business object
	 */
	public static ValidationReferenceExample createValidationReferenceExample(ValidationReferenceExampleBO bo) {
		return createValidationReferenceExample(bo, true);
	}

	/**
	 * Creates a new transfer object based on the passed business object.
	 * 
	 * @param bo
	 *            The {@link ValidationReferenceExampleBO} that should be made transportable
	 * @param createChildren
	 *            flag to suppress child creation
	 * @return The transportable version of the passed business object
	 */
	public static ValidationReferenceExample createValidationReferenceExample(ValidationReferenceExampleBO bo,
			boolean createChildren) {
		ValidationReferenceExample to = new ValidationReferenceExample();

		if (bo != null) {
			// pull over all fields:
			to.setId(bo.getId());
			to.setSomeDataField(bo.getSomeDataField());
			setOplockOnTransferObject(to, bo.getOplock());

			// pull over composites:

			// pull over parent:

			// no relations to assemble

			// pull over references:
			ValidationReferencedObjectBO requiredReferenceRef = bo.getRequiredReference();
			if (requiredReferenceRef != null) {
				to.setRequiredReference(requiredReferenceRef.getValidationReferencedObjectValues());

			}

		}

		return to;

	}

	/**
	 * Merges the values of the passed transfer object into the passed business object. If the passed business object is
	 * null, it creates a new one. Include merging of all children.
	 * 
	 * @param to
	 *            The {@link ValidationReferenceExample} that has more up-to-date values that should be propagated onto
	 *            the passed business object
	 * @param bo
	 *            The {@link ValidationReferenceExampleBO} that should be updated with the values of the passed transfer
	 *            object (or null if no version of the business object currently exists
	 * @return The updated business object
	 */
	public static ValidationReferenceExampleBO mergeValidationReferenceExampleBO(ValidationReferenceExample to,
			ValidationReferenceExampleBO bo) {
		return mergeValidationReferenceExampleBO(to, bo, true);
	}

	/**
	 * Merges the values of the passed transfer object into the passed business obejct. If the passed business object is
	 * null, it creates a new one.
	 * 
	 * @param to
	 *            The {@link ValidationReferenceExample} that has more up-to-date values that should be propagated onto
	 *            the passed business object
	 * @param bo
	 *            The {@link ValidationReferenceExampleBO} that should be updated with the values of the passed transfer
	 *            object (or null if no version of the business object currently exists
	 * @param mergeChildren
	 *            whether or not to suppress the merging of children
	 * @return The updated business object
	 */
	public static ValidationReferenceExampleBO mergeValidationReferenceExampleBO(ValidationReferenceExample to,
			ValidationReferenceExampleBO bo, boolean mergeChildren) {
		if (bo == null) {
			bo = BusinessObjectFactory.createValidationReferenceExampleBO();
		}

		if ((to != null) && (bo != null)) {
			bo.setId(to.getId());
			bo.setSomeDataField(to.getSomeDataField());
			bo.setOplock(to.getOplock());

			// pull over composites:

			// pull over parent:

			// no children to merge

			// pull over references:
			ValidationReferencedObject requiredReferenceToRef = to.getRequiredReference();
			if (requiredReferenceToRef != null) {
				ValidationReferencedObjectPK requiredReferencePk = (ValidationReferencedObjectPK) requiredReferenceToRef
						.getKey();
				ValidationReferencedObjectBO requiredReferenceRef = ValidationReferencedObjectBO
						.findByPrimaryKey(requiredReferencePk);
				if (requiredReferenceRef != null) {
					bo.setRequiredReference(requiredReferenceRef);

				} else {
					// just translate it over - we'll check to ensure it is valid before saving, but this
					// gives folks a chance to do something with the inbound data before we cull it:
					ValidationReferencedObjectBO potentialRequiredReferenceRef = ValidationReferencedObjectAssembler
							.mergeValidationReferencedObjectBO(requiredReferenceToRef, null);
					bo.setRequiredReference(potentialRequiredReferenceRef);

				}
			}

		}

		return bo;

	}

}
