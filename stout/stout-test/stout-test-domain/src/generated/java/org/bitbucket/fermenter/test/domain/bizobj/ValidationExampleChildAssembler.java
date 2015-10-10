package org.bitbucket.fermenter.test.domain.bizobj;

import org.bitbucket.fermenter.test.domain.transfer.ValidationExampleChild;

import org.bitbucket.fermenter.test.domain.transfer.ValidationExample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bitbucket.fermenter.bizobj.TransferObjectAssembler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transfer object assembler for the ValidationExampleChild application entity. This class is used create new instances
 * of {@link ValidationExampleChild} from an instance of a/an {@link ValidationExampleChildBO} or the inverse. It is
 * also used to merge an instance of a/an ValidationExampleChildBO with the values of a ValidationExampleChild instance.
 * 
 * Generated Code - DO NOT MODIFY
 */
public final class ValidationExampleChildAssembler extends TransferObjectAssembler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExampleChildAssembler.class);

	private ValidationExampleChildAssembler() {
		// prevent instantiation
	}

	/**
	 * Given a list of {@link ValidationExampleChildBO} instances, create a list of {@link ValidationExampleChild}
	 * instances. This will iterate over the list of {@link ValidationExampleChildBO} instances, create a(n)
	 * {@link ValidationExampleChild} instance from the {@link ValidationExampleChildBO} instance, and add the new
	 * {@link ValidationExampleChild} instance to the result list.
	 */
	public static List<ValidationExampleChild> getValidationExampleChildList(Collection<ValidationExampleChildBO> bos) {
		if (bos == null) {
			return null;
		}

		List<ValidationExampleChild> tos = new ArrayList<ValidationExampleChild>(bos.size());
		for (ValidationExampleChildBO bo : bos) {
			tos.add(createValidationExampleChild(bo));
		}

		return tos;
	}

	/**
	 * Given a list of {@link ValidationExampleChild} instances, create a list of {@link ValidationExampleChildBO}
	 * instances. This will iterate over the list of {@link ValidationExampleChild} instances, create a(n)
	 * {@link ValidationExampleChildBO} instance from the {@link ValidationExampleChild} instance, and add the new
	 * {@link ValidationExampleChildBO} instance to the result list.
	 */
	public static List<ValidationExampleChildBO> getValidationExampleChildBOList(Collection<ValidationExampleChild> tos) {
		if (tos == null) {
			return null;
		}

		List<ValidationExampleChildBO> bos = new ArrayList<ValidationExampleChildBO>(tos.size());
		for (ValidationExampleChild to : tos) {
			bos.add(mergeValidationExampleChildBO(to, null));
		}

		return bos;
	}

	/**
	 * Creates a new transfer object based on the passed business object, including children.
	 * 
	 * @param bo
	 *            The {@link ValidationExampleChildBO} that should be made transportable
	 * @return The transportable version of the passed business object
	 */
	public static ValidationExampleChild createValidationExampleChild(ValidationExampleChildBO bo) {
		return createValidationExampleChild(bo, true);
	}

	/**
	 * Creates a new transfer object based on the passed business object.
	 * 
	 * @param bo
	 *            The {@link ValidationExampleChildBO} that should be made transportable
	 * @param createChildren
	 *            flag to suppress child creation
	 * @return The transportable version of the passed business object
	 */
	public static ValidationExampleChild createValidationExampleChild(ValidationExampleChildBO bo,
			boolean createChildren) {
		ValidationExampleChild to = new ValidationExampleChild();

		if (bo != null) {
			// pull over all fields:
			to.setId(bo.getId());
			to.setRequiredField(bo.getRequiredField());
			setOplockOnTransferObject(to, bo.getOplock());

			// pull over composites:

			// pull over parent:
			ValidationExample parentValidationExample = ValidationExampleAssembler.createValidationExample(
					bo.getValidationExample(), false);
			to.setValidationExample(parentValidationExample);

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
	 *            The {@link ValidationExampleChild} that has more up-to-date values that should be propagated onto the
	 *            passed business object
	 * @param bo
	 *            The {@link ValidationExampleChildBO} that should be updated with the values of the passed transfer
	 *            object (or null if no version of the business object currently exists
	 * @return The updated business object
	 */
	public static ValidationExampleChildBO mergeValidationExampleChildBO(ValidationExampleChild to,
			ValidationExampleChildBO bo) {
		return mergeValidationExampleChildBO(to, bo, true);
	}

	/**
	 * Merges the values of the passed transfer object into the passed business obejct. If the passed business object is
	 * null, it creates a new one.
	 * 
	 * @param to
	 *            The {@link ValidationExampleChild} that has more up-to-date values that should be propagated onto the
	 *            passed business object
	 * @param bo
	 *            The {@link ValidationExampleChildBO} that should be updated with the values of the passed transfer
	 *            object (or null if no version of the business object currently exists
	 * @param mergeChildren
	 *            whether or not to suppress the merging of children
	 * @return The updated business object
	 */
	public static ValidationExampleChildBO mergeValidationExampleChildBO(ValidationExampleChild to,
			ValidationExampleChildBO bo, boolean mergeChildren) {
		if (bo == null) {
			bo = BusinessObjectFactory.createValidationExampleChildBO();
		}

		if ((to != null) && (bo != null)) {
			bo.setId(to.getId());
			bo.setRequiredField(to.getRequiredField());
			bo.setOplock(to.getOplock());

			// pull over composites:

			// pull over parent:
			ValidationExampleBO parentValidationExample = ValidationExampleAssembler.mergeValidationExampleBO(
					to.getValidationExample(), null, false);
			bo.setValidationExample(parentValidationExample);

			// no children to merge

			// pull over references:

		}

		return bo;

	}

}
