package org.bitbucket.askllc.fermenter.cookbook.domain.transfer.json;

import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.*;
import org.bitbucket.askllc.fermenter.cookbook.domain.persist.ValidationReferenceExampleRepository;

import java.util.Set;

import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Transfer object JSON contract for the ValidationReferenceExample application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class ValidationReferenceExampleBaseMixIn {

	@JsonIgnore
	protected abstract Logger getLogger();

	@JsonIgnore
	private ValidationReferenceExampleRepository repository;

	@JsonProperty
	private String id;
	@JsonProperty
	private String someDataField;

	@JsonProperty
	private ValidationReferencedObjectBO requiredReference;

	@JsonIgnore
	public abstract String getKey();

}
