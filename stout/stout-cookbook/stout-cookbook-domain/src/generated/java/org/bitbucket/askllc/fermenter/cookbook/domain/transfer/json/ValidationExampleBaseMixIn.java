package org.bitbucket.askllc.fermenter.cookbook.domain.transfer.json;

import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.*;
import org.bitbucket.askllc.fermenter.cookbook.domain.persist.ValidationExampleRepository;

import java.util.Set;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Transfer object JSON contract for the ValidationExample application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class ValidationExampleBaseMixIn {

	@JsonIgnore
	protected abstract Logger getLogger();

	@JsonIgnore
	private ValidationExampleRepository repository;

	@JsonProperty
	private String id;
	@JsonProperty
	private Integer integerExample;
	@JsonProperty
	private BigDecimal bigDecimalExampleWithScale;
	@JsonProperty
	private String requiredField;
	@JsonProperty
	private BigDecimal bigDecimalExample;
	@JsonProperty
	private String characterExample;
	@JsonProperty
	private String stringExample;
	@JsonProperty
	private Date dateExample;
	@JsonProperty
	private Long longExample;
	@JsonManagedReference(value = "ValidationExample-ValidationExampleChild")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Set<ValidationExampleChildBO> validationExampleChilds;

	@JsonIgnore
	public abstract String getKey();

}
