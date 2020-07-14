package org.bitbucket.fermenter.mda.metamodel.element;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

/**
 * Represents a enumeration of declared constants.
 */
@JsonPropertyOrder({ "package", "type" })
public class ParentElement extends NamespacedMetamodelElement implements Parent {

    @JsonProperty(required = true)
    protected String type;

    @JsonInclude(Include.NON_NULL)
    protected InheritanceStrategy inheritanceStrategy;

    /**
     * Override to make optional (for base types) and not write if null.
     * 
     * {@inheritDoc}
     */
    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = PACKAGE, required = false)
    @Override
    public String getPackage() {
        return super.getPackage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InheritanceStrategy getInheritanceStrategy() {
        return inheritanceStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {

        // default many when not specified:
        if (inheritanceStrategy == null) {
            inheritanceStrategy = InheritanceStrategy.MAPPED_SUPERCLASS;
        }

        DefaultModelInstanceRepository modelRespository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);
        Entity parentEntity = modelRespository.getEntity(packageName, type);
        if (parentEntity == null) {
            messageTracker.addErrorMessage("Parent type of '" + type + "' could not be found!");
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSchemaFileName() {
        return "fermenter-2-entity-schema.json";
    }

    /**
     * Sets the parent type.
     * 
     * @param type
     *            parent type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the inheritance strategy on for the return.
     * 
     * @param inheritanceStrategy
     *            inheritance strategy
     */
    public void setInheritanceStrategy(String inheritanceStrategy) {
        this.inheritanceStrategy = InheritanceStrategy.fromString(inheritanceStrategy);

        if (StringUtils.isNoneBlank(inheritanceStrategy) && this.inheritanceStrategy == null) {
            messageTracker.addErrorMessage("Could not map inheritance strategy '" + inheritanceStrategy
                    + "' to one of the known inheritance strategy types! (" + InheritanceStrategy.options() + ") ");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("package", getPackage()).add("type", type)
                .add("inheritanceStrategy", inheritanceStrategy).toString();
    }

}
