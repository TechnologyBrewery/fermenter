package org.bitbucket.fermenter.test.domain.bizobj;

import org.bitbucket.fermenter.test.domain.transfer.SimpleDomain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bitbucket.fermenter.bizobj.TransferObjectAssembler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transfer object assembler for the SimpleDomain application entity. This class is used create new instances of
 * {@link SimpleDomain} from an instance of a/an {@link SimpleDomainBO} or the inverse. It is also used to merge an
 * instance of a/an SimpleDomainBO with the values of a SimpleDomain instance.
 * 
 * Generated Code - DO NOT MODIFY
 */
public final class SimpleDomainAssembler extends TransferObjectAssembler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDomainAssembler.class);

	private SimpleDomainAssembler() {
		// prevent instantiation
	}

	/**
	 * Given a list of {@link SimpleDomainBO} instances, create a list of {@link SimpleDomain} instances. This will
	 * iterate over the list of {@link SimpleDomainBO} instances, create a(n) {@link SimpleDomain} instance from the
	 * {@link SimpleDomainBO} instance, and add the new {@link SimpleDomain} instance to the result list.
	 */
	public static List<SimpleDomain> getSimpleDomainList(Collection<SimpleDomainBO> bos) {
		if (bos == null) {
			return null;
		}

		List<SimpleDomain> tos = new ArrayList<SimpleDomain>(bos.size());
		for (SimpleDomainBO bo : bos) {
			tos.add(createSimpleDomain(bo));
		}

		return tos;
	}

	/**
	 * Given a list of {@link SimpleDomain} instances, create a list of {@link SimpleDomainBO} instances. This will
	 * iterate over the list of {@link SimpleDomain} instances, create a(n) {@link SimpleDomainBO} instance from the
	 * {@link SimpleDomain} instance, and add the new {@link SimpleDomainBO} instance to the result list.
	 */
	public static List<SimpleDomainBO> getSimpleDomainBOList(Collection<SimpleDomain> tos) {
		if (tos == null) {
			return null;
		}

		List<SimpleDomainBO> bos = new ArrayList<SimpleDomainBO>(tos.size());
		for (SimpleDomain to : tos) {
			bos.add(mergeSimpleDomainBO(to, null));
		}

		return bos;
	}

	/**
	 * Creates a new transfer object based on the passed business object, including children.
	 * 
	 * @param bo
	 *            The {@link SimpleDomainBO} that should be made transportable
	 * @return The transportable version of the passed business object
	 */
	public static SimpleDomain createSimpleDomain(SimpleDomainBO bo) {
		return createSimpleDomain(bo, true);
	}

	/**
	 * Creates a new transfer object based on the passed business object.
	 * 
	 * @param bo
	 *            The {@link SimpleDomainBO} that should be made transportable
	 * @param createChildren
	 *            flag to suppress child creation
	 * @return The transportable version of the passed business object
	 */
	public static SimpleDomain createSimpleDomain(SimpleDomainBO bo, boolean createChildren) {
		SimpleDomain to = new SimpleDomain();

		if (bo != null) {
			// pull over all fields:
			to.setId(bo.getId());
			to.setName(bo.getName());
			to.setTheLong1(bo.getTheLong1());
			to.setBigDecimalValue(bo.getBigDecimalValue());
			to.setType(bo.getType());
			to.setAnEnumeratedValue(bo.getAnEnumeratedValue());
			to.setTheDate1(bo.getTheDate1());
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
	 *            The {@link SimpleDomain} that has more up-to-date values that should be propagated onto the passed
	 *            business object
	 * @param bo
	 *            The {@link SimpleDomainBO} that should be updated with the values of the passed transfer object (or
	 *            null if no version of the business object currently exists
	 * @return The updated business object
	 */
	public static SimpleDomainBO mergeSimpleDomainBO(SimpleDomain to, SimpleDomainBO bo) {
		return mergeSimpleDomainBO(to, bo, true);
	}

	/**
	 * Merges the values of the passed transfer object into the passed business obejct. If the passed business object is
	 * null, it creates a new one.
	 * 
	 * @param to
	 *            The {@link SimpleDomain} that has more up-to-date values that should be propagated onto the passed
	 *            business object
	 * @param bo
	 *            The {@link SimpleDomainBO} that should be updated with the values of the passed transfer object (or
	 *            null if no version of the business object currently exists
	 * @param mergeChildren
	 *            whether or not to suppress the merging of children
	 * @return The updated business object
	 */
	public static SimpleDomainBO mergeSimpleDomainBO(SimpleDomain to, SimpleDomainBO bo, boolean mergeChildren) {
		if (bo == null) {
			bo = BusinessObjectFactory.createSimpleDomainBO();
		}

		if ((to != null) && (bo != null)) {
			bo.setId(to.getId());
			bo.setName(to.getName());
			bo.setTheLong1(to.getTheLong1());
			bo.setBigDecimalValue(to.getBigDecimalValue());
			bo.setType(to.getType());
			bo.setAnEnumeratedValue(to.getAnEnumeratedValue());
			bo.setTheDate1(to.getTheDate1());
			bo.setOplock(to.getOplock());

			// pull over composites:

			// pull over parent:

			// no children to merge

			// pull over references:

		}

		return bo;

	}

}
