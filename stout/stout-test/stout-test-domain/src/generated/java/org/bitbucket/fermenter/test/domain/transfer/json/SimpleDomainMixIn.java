package org.bitbucket.fermenter.test.domain.transfer.json;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonManagedReference;

import org.bitbucket.fermenter.test.domain.transfer.*;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.bitbucket.fermenter.test.domain.enumeration.SimpleDomainEnumeration;
import org.bitbucket.fermenter.transfer.*;

/**
 * Transfer object json contract for the SimpleDomain application entity. A simple domain with one (generated) id field,
 * fields of different types, and two sample queries.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class SimpleDomainMixIn {

	@JsonProperty
	private String id;
	@JsonProperty
	private Integer oplock;
	@JsonProperty
	private String name;
	@JsonProperty
	private Long theLong1;
	@JsonProperty
	private BigDecimal bigDecimalValue;
	@JsonProperty
	private String type;
	@JsonProperty
	private SimpleDomainEnumeration anEnumeratedValue;
	@JsonProperty
	private Date theDate1;

	@JsonIgnore
	public abstract String getEntityName();

	@JsonIgnore
	public abstract PrimaryKey getKey();

	@JsonIgnore
	public abstract SimpleDomainPK getSimpleDomainPK();

	@JsonIgnore
	public abstract void setKey(PrimaryKey pk);

	@JsonIgnore
	public abstract void setSimpleDomainPK(SimpleDomainPK pk);

}
