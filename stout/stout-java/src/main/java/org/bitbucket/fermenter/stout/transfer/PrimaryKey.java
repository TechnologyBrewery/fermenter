package org.bitbucket.fermenter.stout.transfer;

import java.io.Serializable;

/**
 * Defines behavior for primary keys
 * 
 * @author Steve Andrews 
 */
public interface PrimaryKey extends Serializable {
	
	public String getEntityName();
	
	public String toString();
	
	public Serializable getValue();

}
