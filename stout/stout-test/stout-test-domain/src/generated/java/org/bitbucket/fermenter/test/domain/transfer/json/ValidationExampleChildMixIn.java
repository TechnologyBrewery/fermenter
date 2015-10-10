package org.bitbucket.fermenter.test.domain.transfer.json;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonManagedReference;

import org.bitbucket.fermenter.test.domain.transfer.*;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.bitbucket.fermenter.transfer.*;

/**
 * Transfer object json contract for the ValidationExampleChild application entity.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class ValidationExampleChildMixIn {

	@JsonProperty
	private String id;
	@JsonProperty
	private Integer oplock;
	@JsonProperty
	private String requiredField;
	@JsonBackReference(value = "ValidationExample-ValidationExampleChild")
	private ValidationExample parentValidationExample;

	@JsonIgnore
	public abstract String getEntityName();

	@JsonIgnore
	public abstract PrimaryKey getKey();

	@JsonIgnore
	public abstract ValidationExampleChildPK getValidationExampleChildPK();

	@JsonIgnore
	public abstract void setKey(PrimaryKey pk);

	@JsonIgnore
	public abstract void setValidationExampleChildPK(ValidationExampleChildPK pk);

	@JsonIgnore
	public abstract ValidationExample getValidationExample();

	@JsonIgnore
	public abstract void setValidationExample(ValidationExample parent);

}
