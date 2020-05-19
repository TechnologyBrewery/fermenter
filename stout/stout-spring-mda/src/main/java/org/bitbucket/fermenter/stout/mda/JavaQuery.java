package org.bitbucket.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Query;

/**
 * Implements the {@link Query} interface with typical Java implementation details.
 */
public class JavaQuery implements Query {
	
	private Query query;
	private List<Field> decoratedCriteriaList;
	
	/**
	 * Create a new instance of {@link Query} with the correct functionality set 
	 * to generate Java code
	 * @param queryToDecorate The {@link Query} to decorate
	 */
	public JavaQuery(Query queryToDecorate) {
		if (queryToDecorate == null) {
			throw new IllegalArgumentException("JavaQuery instances must be instatiated with a non-null query!");
		}
		query = queryToDecorate;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getStatement() {
		return query.getStatement();
	}
	
	/**
	 * Determines whether the statement is an update or delete via typical SQL/HQL languages.
	 * @return true for update or delete, false otherwise
	 */
	public boolean isUpdateOrDelete() {
		boolean isUpdateOrDelete = false;
		
		String statement = getStatement();
		if ((StringUtils.startsWithIgnoreCase(statement, "update")) 
				|| (StringUtils.startsWithIgnoreCase(statement, "delete"))) {
			isUpdateOrDelete = true;
		}
		
		return isUpdateOrDelete;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Field> getCriteria() {
		if (decoratedCriteriaList == null) {
			List<Field> queryCriteriaList = query.getCriteria();
			if ((queryCriteriaList == null) || (queryCriteriaList.isEmpty())) {
				decoratedCriteriaList = Collections.emptyList();
				
			} else {
				decoratedCriteriaList = new ArrayList<Field>((int)(queryCriteriaList.size()));
				for (Field f : queryCriteriaList) {
					decoratedCriteriaList.add(new JavaField(f));
					
				}
			}
		}
		
		return decoratedCriteriaList;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return query.getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getDocumentation() {
		return query.getDocumentation();
	}

	/**
	 * Returns the name of the query as an uncapitalized string.
	 * @return uncapitalized name
	 */
	public String getLowercaseName() {
		return StringUtils.uncapitalize(query.getName());
	}

    /**
     * {@inheritDoc}
     */
    public String getPagination() {
        return query.getPagination();
    }
    
    /**
     * Determines if the query has pagination.
     * @return pagination exists
     */
    public boolean hasPagination() {
        return !query.getPagination().equals(PAGINATION_NONE);
    }

}
