package org.bitbucket.fermenter.mda.metadata.element;

import java.util.ArrayList;
import java.util.List;

public class QueryMetadata extends MetadataElement implements Query {

    private String name;
    private String documentation;
    private List   criteria;
    private String statement;
    
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Query#getStatement()
	 */
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Query#getCriteria()
	 */
    public List getCriteria() {
        if( criteria == null ) {
            criteria = new ArrayList();
        }
        return criteria;
    }
    
    /**
     * @param criteria The criteria to set.
     */
    public void setCriteria(List criteria) {
        this.criteria = criteria;
    }
    
    public void addCriterion(Field crit) {
        getCriteria().add( crit );
    }
    
    /**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Query#getName()
	 */
    public String getName() {
        return name;
    }
    
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Query#getDocumentation()
	 */
    public String getDocumentation() {
    	return documentation;
    }
    
    public void setDocumentation(String documentation) {
    	this.documentation = documentation;
    }
    
	/**
	 * Executed to ensure that valid combinations of metadata have been loaded.
	 */
	public void validate() {
	}
}
