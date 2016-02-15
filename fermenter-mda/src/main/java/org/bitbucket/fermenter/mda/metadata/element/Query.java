package org.bitbucket.fermenter.mda.metadata.element;

import java.util.List;

/**
 * Defines a query. This concept is not implementation specific and could be a SQL, 
 * HQL, Cypher, or other tpye of statement
 */
public interface Query {

	/**
	 * Returns the statement that represents this query.
	 * @return
	 */
	public String getStatement();

	/**
	 * Returns the fields that are leveraged by this query.
	 * @return Returns the criteria.
	 */
	public List<Field> getCriteria();

	/**
	 * @return Returns the name.
	 */
	public String getName();
	
	/**
	 * Returns the documentation about this query.
	 * @return documentation
	 */
	public String getDocumentation();

}