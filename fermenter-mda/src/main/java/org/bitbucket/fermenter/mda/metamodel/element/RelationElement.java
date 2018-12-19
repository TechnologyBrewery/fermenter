package org.bitbucket.fermenter.mda.metamodel.element;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

/**
 * Represents a reference on an entity.
 */
@JsonPropertyOrder({ "type", "package", "multiplicity" })
public class RelationElement implements Relation {
	
	@JsonIgnore
	private static MessageTracker messageTracker = MessageTracker.getInstance();

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = NamespacedMetamodelElement.PACKAGE)
    protected String packageName;
	
    @JsonInclude(Include.NON_NULL)
    protected String type;

    @JsonInclude(Include.NON_NULL)
    protected String documentation;

    @JsonInclude(Include.NON_NULL)
    protected Multiplicity multiplicity;
    
    @JsonInclude(Include.NON_NULL)
    protected String localColumn; 
    
    @JsonInclude(Include.NON_NULL)
    protected FetchMode fetchMode;    

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPackage() {
        return packageName;
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
    public String getDocumentation() {
        return documentation;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public Multiplicity getMultiplicity() {
		return multiplicity;
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
	public FetchMode getFetchMode() {
		return fetchMode;
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
    	//TODO: validate this refers to a valid entity
    	
    	if (multiplicity == null) {
    		multiplicity = Multiplicity.ONE_TO_MANY;
    	}
    	
    	if (fetchMode == null) {
    		fetchMode = FetchMode.EAGER;
    	}
    }

    /**
     * Sets the relation type package.
     * 
     * @param package
     *            relation type package
     */
    public void setPackage(String packageName) {
        this.packageName = packageName;
    }      
    
    /**
     * Sets the relation type.
     * 
     * @param type
     *            relation type
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
     * Sets the multiplicity value.
     * 
     * @param multiplicityAsString
     *            multiplicity value
     */
    public void setMultiplicity(String multiplicityAsString) {
        this.multiplicity = Multiplicity.fromString(multiplicityAsString);
        
		if (StringUtils.isNoneBlank(multiplicityAsString) && multiplicity == null) {
			messageTracker.addErrorMessage("Could not map multiplicity '" + multiplicityAsString
					+ "' to one of the known multiplicity types! (" + Multiplicity.options() + ") ");
		}        
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
     * Sets the fetch mode value.
     * 
     * @param fetchModeAsString
     *            fetch mode value
     */
    public void setFetchMode(String fetchModeAsString) {
        this.fetchMode = FetchMode.fromString(fetchModeAsString);
        
		if (StringUtils.isNoneBlank(fetchModeAsString) && fetchMode == null) {
			messageTracker.addErrorMessage("Could not map fetch mode '" + fetchModeAsString
					+ "' to one of the known fetch mode types! (" + FetchMode.options() + ") ");
		}
    }    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("type", type).toString();
    }

}
