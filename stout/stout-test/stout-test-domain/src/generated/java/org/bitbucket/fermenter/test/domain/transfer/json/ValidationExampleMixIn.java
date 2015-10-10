package org.bitbucket.fermenter.test.domain.transfer.json;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonManagedReference;

import org.bitbucket.fermenter.test.domain.transfer.*;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.bitbucket.fermenter.transfer.*;

import java.util.Set;

/**
 * Transfer object json contract for the ValidationExample application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class ValidationExampleMixIn {

	@JsonProperty
	private String id;
	@JsonProperty
	private Integer oplock;
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
	private Set<ValidationExampleChild> validationExampleChilds;

	@JsonIgnore
	public abstract String getEntityName();

	@JsonIgnore
	public abstract PrimaryKey getKey();

	@JsonIgnore
	public abstract ValidationExamplePK getValidationExamplePK();

	@JsonIgnore
	public abstract void setKey(PrimaryKey pk);

	@JsonIgnore
	public abstract void setValidationExamplePK(ValidationExamplePK pk);

}
