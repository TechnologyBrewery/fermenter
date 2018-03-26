package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.Objects;

import org.bitbucket.fermenter.mda.element.ValidatedElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * Defines the contract for a metamodel element that requires a package and name.
 */
public abstract class NamespacedMetamodelElement extends MetamodelElement implements ValidatedElement, NamespacedMetamodel {

    @JsonProperty(value = "package", required = true)
    protected String packageName;

    /* (non-Javadoc)
     * @see org.bitbucket.fermenter.mda.metamodel.element.NamespacedMetamodel#getPackage()
     */
    @Override
    public String getPackage() {
        return packageName;
    }

    /**
     * Sets the packageName of the metadata element. param packageName element packageName
     */
    public void setPackage(String packageName) {
        this.packageName = packageName;
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
        return MoreObjects.toStringHelper(this).add("package", getPackage()).add("name", name).toString();
    }

}
