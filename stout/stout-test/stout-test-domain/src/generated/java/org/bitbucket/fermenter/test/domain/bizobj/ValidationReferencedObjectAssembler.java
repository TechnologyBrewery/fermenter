package org.bitbucket.fermenter.test.domain.bizobj;

import org.bitbucket.fermenter.test.domain.transfer.ValidationReferencedObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bitbucket.fermenter.bizobj.TransferObjectAssembler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transfer object assembler for the ValidationReferencedObject application entity. This class is used create new
 * instances of {@link ValidationReferencedObject} from an instance of a/an {@link ValidationReferencedObjectBO} or the
 * inverse. It is also used to merge an instance of a/an ValidationReferencedObjectBO with the values of a
 * ValidationReferencedObject instance.
 * 
 * Generated Code - DO NOT MODIFY
 */
public final class ValidationReferencedObjectAssembler extends TransferObjectAssembler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationReferencedObjectAssembler.class);

	private ValidationReferencedObjectAssembler() {
		// prevent instantiation
	}

	/**
	 * Given a list of {@link ValidationReferencedObjectBO} instances, create a list of
	 * {@link ValidationReferencedObject} instances. This will iterate over the list of
	 * {@link ValidationReferencedObjectBO} instances, create a(n) {@link ValidationReferencedObject} instance from the
	 * {@link ValidationReferencedObjectBO} instance, and add the new {@link ValidationReferencedObject} instance to the
	 * result list.
	 */
	public static List<ValidationReferencedObject> getValidationReferencedObjectList(
			Collection<ValidationReferencedObjectBO> bos) {
		if (bos == null) {
			return null;
		}

		List<ValidationReferencedObject> tos = new ArrayList<ValidationReferencedObject>(bos.size());
		for (ValidationReferencedObjectBO bo : bos) {
			tos.add(createValidationReferencedObject(bo));
		}

		return tos;
	}

	/**
	 * Given a list of {@link ValidationReferencedObject} instances, create a list of
	 * {@link ValidationReferencedObjectBO} instances. This will iterate over the list of
	 * {@link ValidationReferencedObject} instances, create a(n) {@link ValidationReferencedObjectBO} instance from the
	 * {@link ValidationReferencedObject} instance, and add the new {@link ValidationReferencedObjectBO} instance to the
	 * result list.
	 */
	public static List<ValidationReferencedObjectBO> getValidationReferencedObjectBOList(
			Collection<ValidationReferencedObject> tos) {
		if (tos == null) {
			return null;
		}

		List<ValidationReferencedObjectBO> bos = new ArrayList<ValidationReferencedObjectBO>(tos.size());
		for (ValidationReferencedObject to : tos) {
			bos.add(mergeValidationReferencedObjectBO(to, null));
		}

		return bos;
	}

	/**
	 * Creates a new transfer object based on the passed business object, including children.
	 * 
	 * @param bo
	 *            The {@link ValidationReferencedObjectBO} that should be made transportable
	 * @return The transportable version of the passed business object
	 */
	public static ValidationReferencedObject createValidationReferencedObject(ValidationReferencedObjectBO bo) {
		return createValidationReferencedObject(bo, true);
	}

	/**
	 * Creates a new transfer object based on the passed business object.
	 * 
	 * @param bo
	 *            The {@link ValidationReferencedObjectBO} that should be made transportable
	 * @param createChildren
	 *            flag to suppress child creation
	 * @return The transportable version of the passed business object
	 */
	public static ValidationReferencedObject createValidationReferencedObject(ValidationReferencedObjectBO bo,
			boolean createChildren) {
		ValidationReferencedObject to = new ValidationReferencedObject();

		if (bo != null) {
			// pull over all fields:
			to.setId(bo.getId());
			to.setSomeDataField(bo.getSomeDataField());
			setOplockOnTransferObject(to, bo.getOplock());

			// pull over composites:

			// pull over parent:

			// no relations to assemble

			// pull over references:

		}

		return to;

	}

	/**
	 * Merges the values of the passed transfer object into the passed business object. If the passed business object is
	 * null, it creates a new one. Include merging of all children.
	 * 
	 * @param to
	 *            The {@link ValidationReferencedObject} that has more up-to-date values that should be propagated onto
	 *            the passed business object
	 * @param bo
	 *            The {@link ValidationReferencedObjectBO} that should be updated with the values of the passed transfer
	 *            object (or null if no version of the business object currently exists
	 * @return The updated business object
	 */
	public static ValidationReferencedObjectBO mergeValidationReferencedObjectBO(ValidationReferencedObject to,
			ValidationReferencedObjectBO bo) {
		return mergeValidationReferencedObjectBO(to, bo, true);
	}

	/**
	 * Merges the values of the passed transfer object into the passed business obejct. If the passed business object is
	 * null, it creates a new one.
	 * 
	 * @param to
	 *            The {@link ValidationReferencedObject} that has more up-to-date values that should be propagated onto
	 *            the passed business object
	 * @param bo
	 *            The {@link ValidationReferencedObjectBO} that should be updated with the values of the passed transfer
	 *            object (or null if no version of the business object currently exists
	 * @param mergeChildren
	 *            whether or not to suppress the merging of children
	 * @return The updated business object
	 */
	public static ValidationReferencedObjectBO mergeValidationReferencedObjectBO(ValidationReferencedObject to,
			ValidationReferencedObjectBO bo, boolean mergeChildren) {
		if (bo == null) {
			bo = BusinessObjectFactory.createValidationReferencedObjectBO();
		}

		if ((to != null) && (bo != null)) {
			bo.setId(to.getId());
			bo.setSomeDataField(to.getSomeDataField());
			bo.setOplock(to.getOplock());

			// pull over composites:

			// pull over parent:

			// no children to merge

			// pull over references:

		}

		return bo;

	}

}
