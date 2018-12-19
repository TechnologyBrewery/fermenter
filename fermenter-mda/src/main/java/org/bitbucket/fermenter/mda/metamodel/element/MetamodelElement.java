package org.bitbucket.fermenter.mda.metamodel.element;

import java.io.File;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
//import org.apache.commons.collections4.CollectionUtils;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.MoreObjects;

/**
 * Implements the contract for a basic metamodel element.
 */
public abstract class MetamodelElement implements Metamodel {

	protected static MessageTracker messageTracker = MessageTracker.getInstance();

	@JsonProperty(required = true)
	@JsonInclude(Include.NON_NULL)
	protected String name;

	@JsonIgnore
	protected String fileName;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the metadata element.
	 * 
	 * @param name name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the filename used for this root element, if one exists (e.g., an
	 * Enumeration will have one, but an Operation will not because it doesn't sit
	 * in the root of a model file).
	 * 
	 * @param fileName name of the file containing this root instance
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	@Override
	public void validate() {
		if (StringUtils.isBlank(getName())) {
			messageTracker.addErrorMessage("Name is a required attribute!");

		} else if (getFileName() != null) {
			String localFileName = getFileName();
			String stippedFileName = getFileName().substring(localFileName.lastIndexOf(File.separatorChar) + 1,
					localFileName.lastIndexOf('.'));
			if (!getName().equals(stippedFileName)) {
				messageTracker.addErrorMessage("The file name must match the element name!  Excepted: '" + getName()
						+ "', but found: '**" + stippedFileName + "**.json' (file: " + localFileName + ")");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		boolean areEqual;
		if (obj == null) {
			areEqual = false;

		} else if (obj == this) {
			areEqual = true;

		} else if (obj.getClass() != getClass()) {
			areEqual = false;

		} else {
			Metamodel other = (Metamodel) obj;
			areEqual = Objects.equals(this.name, other.getName());

		}

		return areEqual;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("name", name).toString();
	}

}
