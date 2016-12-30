package org.bitbucket.askllc.fermenter.cookbook.domain.transfer.json;

import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.*;
import org.bitbucket.askllc.fermenter.cookbook.domain.persist.SimpleDomainRepository;

import java.util.Set;

import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.bitbucket.askllc.fermenter.cookbook.domain.enumeration.SimpleDomainEnumeration;

/**
 * Transfer object JSON contract for the SimpleDomain application entity. A simple domain with one (generated) id field,
 * fields of different types, and two sample queries.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class SimpleDomainBaseMixIn {

	@JsonIgnore
	protected abstract Logger getLogger();

	@JsonIgnore
	private SimpleDomainRepository repository;

	@JsonProperty
	private String id;
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
	public abstract String getKey();

}
