package org.bitbucket.fermenter.mda.metamodel.element;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

/**
 * Represents a reference on an entity.
 */
@JsonPropertyOrder({ "package", "name" })
public class ReferenceElement extends MetamodelElement implements Reference {

    @JsonInclude(Include.NON_NULL)
    protected Type type;

    @JsonInclude(Include.NON_NULL)
    protected String documentation;

    @JsonInclude(Include.NON_NULL)
    protected Boolean required;
    
    @JsonInclude(Include.NON_NULL)
    protected String localColumn; 

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
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
    	//TODO: validate this refers to a valid entity
    }
    
    /**
     * Sets the field type.
     * 
     * @param type
     *            field type
     */
    public void setType(Type type) {
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

}
