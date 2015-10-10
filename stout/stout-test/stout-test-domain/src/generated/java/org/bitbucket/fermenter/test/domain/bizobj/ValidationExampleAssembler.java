package org.bitbucket.fermenter.test.domain.bizobj;

import org.bitbucket.fermenter.test.domain.transfer.ValidationExample;

import org.bitbucket.fermenter.test.domain.transfer.ValidationExampleChild;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bitbucket.fermenter.bizobj.TransferObjectAssembler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transfer object assembler for the ValidationExample application entity. This class is used create new instances of
 * {@link ValidationExample} from an instance of a/an {@link ValidationExampleBO} or the inverse. It is also used to
 * merge an instance of a/an ValidationExampleBO with the values of a ValidationExample instance.
 * 
 * Generated Code - DO NOT MODIFY
 */
public final class ValidationExampleAssembler extends TransferObjectAssembler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationExampleAssembler.class);

	private ValidationExampleAssembler() {
		// prevent instantiation
	}

	/**
	 * Given a list of {@link ValidationExampleBO} instances, create a list of {@link ValidationExample} instances. This
	 * will iterate over the list of {@link ValidationExampleBO} instances, create a(n) {@link ValidationExample}
	 * instance from the {@link ValidationExampleBO} instance, and add the new {@link ValidationExample} instance to the
	 * result list.
	 */
	public static List<ValidationExample> getValidationExampleList(Collection<ValidationExampleBO> bos) {
		if (bos == null) {
			return null;
		}

		List<ValidationExample> tos = new ArrayList<ValidationExample>(bos.size());
		for (ValidationExampleBO bo : bos) {
			tos.add(createValidationExample(bo));
		}

		return tos;
	}

	/**
	 * Given a list of {@link ValidationExample} instances, create a list of {@link ValidationExampleBO} instances. This
	 * will iterate over the list of {@link ValidationExample} instances, create a(n) {@link ValidationExampleBO}
	 * instance from the {@link ValidationExample} instance, and add the new {@link ValidationExampleBO} instance to the
	 * result list.
	 */
	public static List<ValidationExampleBO> getValidationExampleBOList(Collection<ValidationExample> tos) {
		if (tos == null) {
			return null;
		}

		List<ValidationExampleBO> bos = new ArrayList<ValidationExampleBO>(tos.size());
		for (ValidationExample to : tos) {
			bos.add(mergeValidationExampleBO(to, null));
		}

		return bos;
	}

	/**
	 * Creates a new transfer object based on the passed business object, including children.
	 * 
	 * @param bo
	 *            The {@link ValidationExampleBO} that should be made transportable
	 * @return The transportable version of the passed business object
	 */
	public static ValidationExample createValidationExample(ValidationExampleBO bo) {
		return createValidationExample(bo, true);
	}

	/**
	 * Creates a new transfer object based on the passed business object.
	 * 
	 * @param bo
	 *            The {@link ValidationExampleBO} that should be made transportable
	 * @param createChildren
	 *            flag to suppress child creation
	 * @return The transportable version of the passed business object
	 */
	public static ValidationExample createValidationExample(ValidationExampleBO bo, boolean createChildren) {
		ValidationExample to = new ValidationExample();

		if (bo != null) {
			// pull over all fields:
			to.setId(bo.getId());
			to.setIntegerExample(bo.getIntegerExample());
			to.setBigDecimalExampleWithScale(bo.getBigDecimalExampleWithScale());
			to.setRequiredField(bo.getRequiredField());
			to.setBigDecimalExample(bo.getBigDecimalExample());
			to.setCharacterExample(bo.getCharacterExample());
			to.setStringExample(bo.getStringExample());
			to.setDateExample(bo.getDateExample());
			to.setLongExample(bo.getLongExample());
			setOplockOnTransferObject(to, bo.getOplock());

			// pull over composites:

			// pull over parent:

			// pull over relations:
			if (createChildren) {
				ValidationExampleChildBO validationExampleChildBo;
				ValidationExampleChild validationExampleChildTo;
				Set validationExampleChildSet = bo.getValidationExampleChilds();
				if ((validationExampleChildSet != null) && (validationExampleChildSet.size() > 0)) {
					Iterator validationExampleChildSetIterator = validationExampleChildSet.iterator();
					while (validationExampleChildSetIterator.hasNext()) {
						validationExampleChildBo = (ValidationExampleChildBO) validationExampleChildSetIterator.next();
						validationExampleChildTo = validationExampleChildBo.getValidationExampleChildValues();
						to.addValidationExampleChild(validationExampleChildTo);
					}
				}
			}

			// pull over references:

		}

		return to;

	}

	/**
	 * Merges the values of the passed transfer object into the passed business object. If the passed business object is
	 * null, it creates a new one. Include merging of all children.
	 * 
	 * @param to
	 *            The {@link ValidationExample} that has more up-to-date values that should be propagated onto the
	 *            passed business object
	 * @param bo
	 *            The {@link ValidationExampleBO} that should be updated with the values of the passed transfer object
	 *            (or null if no version of the business object currently exists
	 * @return The updated business object
	 */
	public static ValidationExampleBO mergeValidationExampleBO(ValidationExample to, ValidationExampleBO bo) {
		return mergeValidationExampleBO(to, bo, true);
	}

	/**
	 * Merges the values of the passed transfer object into the passed business obejct. If the passed business object is
	 * null, it creates a new one.
	 * 
	 * @param to
	 *            The {@link ValidationExample} that has more up-to-date values that should be propagated onto the
	 *            passed business object
	 * @param bo
	 *            The {@link ValidationExampleBO} that should be updated with the values of the passed transfer object
	 *            (or null if no version of the business object currently exists
	 * @param mergeChildren
	 *            whether or not to suppress the merging of children
	 * @return The updated business object
	 */
	public static ValidationExampleBO mergeValidationExampleBO(ValidationExample to, ValidationExampleBO bo,
			boolean mergeChildren) {
		if (bo == null) {
			bo = BusinessObjectFactory.createValidationExampleBO();
		}

		if ((to != null) && (bo != null)) {
			bo.setId(to.getId());
			bo.setIntegerExample(to.getIntegerExample());
			bo.setBigDecimalExampleWithScale(to.getBigDecimalExampleWithScale());
			bo.setRequiredField(to.getRequiredField());
			bo.setBigDecimalExample(to.getBigDecimalExample());
			bo.setCharacterExample(to.getCharacterExample());
			bo.setStringExample(to.getStringExample());
			bo.setDateExample(to.getDateExample());
			bo.setLongExample(to.getLongExample());
			bo.setOplock(to.getOplock());

			// pull over composites:

			// pull over parent:

			// pull over relations:
			if (mergeChildren) {
				assembleValidationExampleChild(to, bo);
			}

			// pull over references:

		}

		return bo;

	}

	/**
	 * Uses set comparison to transfer a collection of children onto the business object instance.
	 * 
	 * @param to
	 *            The {@link ValidationExample} that has more up-to-date values that should be propogated onto the
	 *            passed business object
	 * @param bo
	 *            The {@link ValidationExampleBO} that should be updated with the values of the passed transfer object
	 */
	protected static void assembleValidationExampleChild(ValidationExample to, ValidationExampleBO bo) {
		// get the set of to relations (transient view):
		Set validationExampleChildToSet = to.getValidationExampleChilds();

		if ((validationExampleChildToSet != null) && (validationExampleChildToSet.size() > 0)) {
			ValidationExampleChild validationExampleChildTo;
			// get the set of bo relations (persistence view). Turn this set into a
			// key-value pair so we can compare PrimaryKey instances:
			ValidationExampleChildBO validationExampleChildBo;
			Set validationExampleChildBoSet = bo.getValidationExampleChilds();
			int size = (validationExampleChildBoSet != null) ? validationExampleChildBoSet.size() : 0;
			Map validationExampleChildKeyToBoMap = new HashMap((int) (size * 1.25));
			if (size > 0) {
				Iterator validationExampleChildBoSetIterator = validationExampleChildBoSet.iterator();
				while (validationExampleChildBoSetIterator.hasNext()) {
					validationExampleChildBo = (ValidationExampleChildBO) validationExampleChildBoSetIterator.next();
					validationExampleChildKeyToBoMap.put(validationExampleChildBo.getKey(), validationExampleChildBo);

				}
			}

			// iterate over the children and perform the correct CRUD action:
			Iterator validationExampleChildToSetIterator = validationExampleChildToSet.iterator();
			while (validationExampleChildToSetIterator.hasNext()) {
				validationExampleChildTo = (ValidationExampleChild) validationExampleChildToSetIterator.next();
				validationExampleChildBo = (ValidationExampleChildBO) validationExampleChildKeyToBoMap
						.get(validationExampleChildTo.getKey());

				if (validationExampleChildBo == null) {
					// this instance isn't known to the BO, so it is a create:
					validationExampleChildBo = new ValidationExampleChildBO();
					bo.addValidationExampleChild(validationExampleChildBo);

				} else {
					// remove the reference from the temp. BO map. Anything left in this map will
					// be removed from the BO:
					validationExampleChildKeyToBoMap.remove(validationExampleChildBo.getKey());
				}

				// update this instance with the TO:
				validationExampleChildBo.setValidationExampleChildValues(validationExampleChildTo);

			}

			// loop over any remaining BOs and delete them:
			if (validationExampleChildKeyToBoMap.size() > 0) {
				Iterator validationExampleChildKeyToBoMapIterator = validationExampleChildKeyToBoMap.values()
						.iterator();
				while (validationExampleChildKeyToBoMapIterator.hasNext()) {
					validationExampleChildBoSet.remove(validationExampleChildKeyToBoMapIterator.next());

				}
			}

		} else {
			bo.getValidationExampleChilds().clear();

		}

	}

}
