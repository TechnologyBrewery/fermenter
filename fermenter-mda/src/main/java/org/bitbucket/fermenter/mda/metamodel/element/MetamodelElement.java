package org.bitbucket.fermenter.mda.metamodel.element;

import java.net.URL;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.collections4.CollectionUtils;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
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
	protected URL fileUrl;

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
	 * Sets the URL of the file used for this root element, if one exists (e.g., an
	 * Enumeration will have one, but an Operation will not because it doesn't sit
	 * in the root of a model file).
	 * 
	 * @param file {@link URL} of the file containing this root instance
	 */
	public void setFileUrl(URL file) {
		this.fileUrl = file;
	}

	/**
	 * Returns the name of the file used for this root element as a valid URL.
	 */
	@JsonIgnore
	public String getFileName() {
		return fileUrl != null ? fileUrl.getFile() : null;
	}

	@Override
	public void validate() {
		String localFileName;
		if (StringUtils.isBlank(getName())) {
			messageTracker.addErrorMessage("Name is a required attribute!");

		} else if ((localFileName = getFileName()) != null) {
			// NB localFileName is represented as a URL and will only have '/' path
			// separators regardless of the underlying OS
			String strippedFileName = localFileName.substring(localFileName.lastIndexOf("/") + 1,
					localFileName.lastIndexOf('.'));
			if (!getName().equals(strippedFileName)) {
				messageTracker.addErrorMessage("The file name must match the element name!  Expected: '" + getName()
						+ "', but found: '**" + strippedFileName + "**.json' (file: " + localFileName + ")");
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
