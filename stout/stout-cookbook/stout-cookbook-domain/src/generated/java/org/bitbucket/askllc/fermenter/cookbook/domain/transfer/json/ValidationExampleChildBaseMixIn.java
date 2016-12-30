package org.bitbucket.askllc.fermenter.cookbook.domain.transfer.json;

import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.*;
import org.bitbucket.askllc.fermenter.cookbook.domain.persist.ValidationExampleChildRepository;

import java.util.Set;

import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Transfer object JSON contract for the ValidationExampleChild application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class ValidationExampleChildBaseMixIn {

	@JsonIgnore
	protected abstract Logger getLogger();

	@JsonIgnore
	private ValidationExampleChildRepository repository;

	@JsonProperty
	private String id;
	@JsonProperty
	private String requiredField;
	@JsonBackReference(value = "ValidationExample-ValidationExampleChild")
	private ValidationExampleBO parentValidationExample;

	@JsonIgnore
	public abstract ValidationExampleBO getValidationExample();

	@JsonIgnore
	public abstract void setValidationExample(ValidationExampleBO parent);

	@JsonIgnore
	public abstract String getKey();

}
