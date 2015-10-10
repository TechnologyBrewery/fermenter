package org.bitbucket.fermenter.mda.metadata.element;

import java.util.List;

public interface Query {

	public String getStatement();

	/**
	 * @return Returns the criteria.
	 */
	public List getCriteria();

	/**
	 * @return Returns the name.
	 */
	public String getName();
	
	public String getDocumentation();

}