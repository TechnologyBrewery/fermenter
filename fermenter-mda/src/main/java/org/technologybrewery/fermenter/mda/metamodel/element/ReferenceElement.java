package org.technologybrewery.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

/**
 * Represents a reference on an entity.
 */
@JsonPropertyOrder({ "name", "type", "package", "localColumn", "documentation", "required" })
public class ReferenceElement extends NamespacedMetamodelElement implements Reference {

    @JsonInclude(Include.NON_NULL)
    protected String type;

    @JsonInclude(Include.NON_NULL)
    protected String documentation;

    @JsonInclude(Include.NON_NULL)
    protected Boolean required;

    @JsonInclude(Include.NON_NULL)
    protected String localColumn;

    @JsonIgnore
    private List<Field> foreignKeys;

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
    @JsonInclude(Include.NON_NULL)
    public Boolean isRequired() {
        return required;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentation() {
        return documentation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalColumn() {
        return localColumn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        // TODO: validate this refers to a valid entity
    }

    /**
     * Sets the field type.
     * 
     * @param type
     *            field type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the documentation value.
     * 
     * @param documentation
     *            documentation text
     */
    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    /**
     * Sets the required value.
     * 
     * @param required
     *            required value
     */
    public void setRequired(Boolean required) {
        this.required = required;
    }

    /**
     * Sets the local column value.
     * 
     * @param localColumn
     *            localColumn value
     */
    public void setLocalColumn(String localColumn) {
        this.localColumn = localColumn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", name).toString();
    }

    @Override
    public String getSchemaFileName() {
        return "fermenter-2-service-schema.json";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public List<Field> getForeignKeyFields() {
        if (foreignKeys == null) {
            foreignKeys = new ArrayList<>();
            DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                    .getMetamodelRepository(DefaultModelInstanceRepository.class);
            
            Map<String, Entity> referenceEntities = metadataRepository.getEntities(getPackage());
            Entity entity = referenceEntities.get(getType());
            if (entity == null) {
                throw new NullPointerException("Reference to '" + type + "' not found!");
            }

            Field fkidentifier = entity.getIdentifier();
            if (fkidentifier != null) { // can be null for transient
                FieldElement newId = new FieldElement();
                newId.setType(fkidentifier.getType());
                newId.setName(fkidentifier.getName());

                if (entity.isTransient() == null || !entity.isTransient()) {
                    newId.setColumn(this.localColumn != null ? this.localColumn : fkidentifier.getColumn());
                }
                foreignKeys.add(newId);
            } else {
                if (entity.isTransient() == null || !entity.isTransient()) {
                    throw new NullPointerException("Reference to '" + type + "' does not have a Identifier!");
                }
            }

        }
        return foreignKeys;
    }

}
