package org.bitbucket.fermenter.test.domain.persist;

import java.util.List;
import java.lang.String;
import java.lang.String;

import org.bitbucket.fermenter.persist.Dao;

import org.bitbucket.fermenter.test.domain.bizobj.SimpleDomainBO;
import org.bitbucket.fermenter.test.domain.transfer.SimpleDomainPK;

import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.bitbucket.fermenter.test.domain.enumeration.SimpleDomainEnumeration;

/**
 * Data access object for the SimpleDomain application entity
 * 
 * Generated Code - DO NOT MODIFY
 */

public interface SimpleDomainBaseDao extends Dao<SimpleDomainBO, SimpleDomainPK> {

	public static final String SelectAllSimpleDomains = "SelectAllSimpleDomains";
	public static final String SelectAllSimpleDomainsByName = "SelectAllSimpleDomainsByName";
	public static final String SelectAllSimpleDomainsByType = "SelectAllSimpleDomainsByType";

	/**
	 * Execute the SelectAllSimpleDomains query
	 * 
	 * @return List of {@link SimpleDomainBO}s
	 */
	public List<SimpleDomainBO> selectAllSimpleDomains();

	/**
	 * Execute the SelectAllSimpleDomainsByName query
	 * 
	 * @param name
	 *            The name
	 * @return List of {@link SimpleDomainBO}s
	 */
	public List<SimpleDomainBO> selectAllSimpleDomainsByName(String name);

	/**
	 * Execute the SelectAllSimpleDomainsByType query
	 * 
	 * @param type
	 *            The type
	 * @return List of {@link SimpleDomainBO}s
	 */
	public List<SimpleDomainBO> selectAllSimpleDomainsByType(String type);
}