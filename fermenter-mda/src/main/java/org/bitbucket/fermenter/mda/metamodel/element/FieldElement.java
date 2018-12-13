package org.bitbucket.fermenter.mda.metamodel.element;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

/**
 * Represents a field on an entity.
 */
@JsonPropertyOrder({ "package", "name" })
public class FieldElement extends MetamodelElement implements Field {

    @JsonInclude(Include.NON_NULL)
    protected String column;

    @JsonInclude(Include.NON_NULL)
    protected Type type;

    @JsonInclude(Include.NON_NULL)
    protected String documentation;

    @JsonInclude(Include.NON_NULL)
    protected Boolean required;

    @JsonInclude(Include.NON_NULL)
    protected Generator generator;    

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColumn() {
        return column;
    }

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
    public Generator getGenerator() {
        return generator;
    }    

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
    	//TODO: validate this refers to a valid type (once type dictionary is present) 
    }

    /**
     * Sets the column value.
     * 
     * @param column
     *            column value
     */
    public void setColumn(String column) {
        this.column = column;
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
     * Sets the generator.
     * 
     * @param generator
     *            generator
     */
    public void setGenerator(String generator) {
        this.generator = Generator.fromString(generator);

        if (StringUtils.isNoneBlank(generator) && this.generator == null) {
            messageTracker.addErrorMessage("Could not map generator '" + generator
                    + "' to one of the known generator types! (" + Generator.options() + ") ");
        }
    }    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", name).toString();
    }

}
