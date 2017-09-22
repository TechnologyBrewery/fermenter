package org.bitbucket.askllc.fermenter.cookbook.domain.bizobj;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.transfer.json.SimpleDomainMixIn;
import org.bitbucket.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;

/**
 * Business object for the SimpleDomain entity.
 * 
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBaseBO
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Entity
@Table(name = "SIMPLE_DOMAIN")
public class SimpleDomainBO extends SimpleDomainBaseBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDomainBO.class);

	/**
	 * Developers may create additional persistent properties and/or relationships as needed if native support for the
	 * desired type/relation isn't immediately supported by Fermenter. This property's serialization logic is defined in
	 * {@link SimpleDomainMixIn}
	 */
	@Lob
	@Column(name = "LARGE_STRING")
	private String largeString;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_AT")
	@NotNull
	private Date updatedAt;

	public SimpleDomainBO() {
		super();
		SpringAutowiringUtil.autowireBizObj(this);
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void complexValidation() {

	}
	
	public static List<SimpleDomainBO> findAllLazyLoadSimpleDomainChild() {
		return getDefaultRepository().findAll();
	}

	public static List<SimpleDomainBO> findAll() {
		return getDefaultRepository().findAll(eagerFetchRelations());
	}

	public static List<SimpleDomainBO> findAll(int startPage, int pageSize) {
		return getDefaultRepository().findAllEagerFetchRelations(new PageRequest(startPage, pageSize)).getContent();
	}

	public static void deleteAll() {
		getDefaultRepository().deleteAll();
	}

	public static List<SimpleDomainBO> findByType(String type) {
		return getDefaultRepository().findByType(type);
	}

	public String getLargeString() {
		return largeString;
	}

	public void setLargeString(String largeString) {
		this.largeString = largeString;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	@Override
	protected void defaultValues() {
		super.defaultValues();
		
		if (StringUtils.isBlank(getType())) {
			setType(RandomStringUtils.randomAlphabetic(3));
		}
	}

	@Override
	protected void preValidate() {
		setUpdatedAt(new Date());
		setLargeString(RandomStringUtils.randomAlphanumeric(50));
	}

	protected void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}