package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.Objects;

import org.bitbucket.fermenter.mda.element.ValidatedElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.MoreObjects;

/**
 * Implements the contract for a metamodel element that requires a package and name.
 */
public abstract class NamespacedMetamodelElement extends MetamodelElement
        implements ValidatedElement, NamespacedMetamodel {

    protected static final String NAME = "name";
    protected static final String PACKAGE = "package";

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = PACKAGE, required = true)
    protected String packageName;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPackage() {
        return packageName;
    }

    /**
     * Sets the packageName of the metadata element. param packageName element packageName
     * 
     * @param packageName
     *            namespace/package of the element
     */
    public void setPackage(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Leverages both name and namespace to create a hash code. {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(packageName, name);
    }

    /**
     * Leverages both name and namespace to perform equality. {@inheritDoc}
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
            NamespacedMetamodelElement other = (NamespacedMetamodelElement) obj;
            areEqual = Objects.equals(this.packageName, other.getPackage())
                    && Objects.equals(this.name, other.getName());

        }

        return areEqual;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add(PACKAGE, getPackage()).add(NAME, name).toString();
    }

}
