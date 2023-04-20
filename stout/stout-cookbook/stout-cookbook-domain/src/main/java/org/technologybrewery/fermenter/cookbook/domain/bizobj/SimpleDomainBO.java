package org.technologybrewery.fermenter.cookbook.domain.bizobj;

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
import org.technologybrewery.fermenter.cookbook.domain.transfer.json.SimpleDomainMixIn;
import org.technologybrewery.fermenter.stout.util.SpringAutowiringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Business object for the SimpleDomain entity.
 * @see org.technologybrewery.fermenter.cookbook.domain.bizobj.SimpleDomainBaseBO
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 *
 * Originally generated from templates/bo.java.vm
 */
@Entity
@Table(name="SIMPLE_DOMAIN")
public class SimpleDomainBO extends SimpleDomainBaseBO {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleDomainBO.class);

    /**
     * Developers may create additional persistent properties and/or
     * relationships as needed if native support for the desired type/relation
     * isn't immediately supported by Fermenter. This property's serialization
     * logic is defined in {@link SimpleDomainMixIn}
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
		return logger;
	}

    public static void deleteAllSimpleDomain() {
        getDefaultRepository().deleteAllInBatch();
    }

    public static List<SimpleDomainBO> findAllLazyLoadSimpleDomainChild() {
        return getDefaultRepository().findAll();
    }

    public static List<SimpleDomainBO> findAll() {
        return getDefaultRepository().findAll(eagerFetchRelations());
    }

    public static List<SimpleDomainBO> findAll(int pageIndex, int pageSize) {
        return getDefaultRepository().findAllEagerFetchRelations(PageRequest.of(pageIndex, pageSize)).getContent();
    }

    public static Page<SimpleDomainBO> findAllPaged(int pageIndex, int pageSize) {
        return getDefaultRepository().findAll(PageRequest.of(pageIndex, pageSize));
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
    protected void defaultComplexValues() {

        if (StringUtils.isBlank(getType())) {
            setType(RandomStringUtils.randomAlphabetic(3));
        }

        if (StringUtils.isBlank(getLargeString())) {
            setLargeString(RandomStringUtils.randomAlphanumeric(50));
        }
    }

    @Override
    protected void preValidate() {
        setUpdatedAt(new Date());
    }

    protected void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static List<SimpleDomainBO> findAllBefore(Date date) {
        return getDefaultRepository().findByTheDate1Before(date);
    }

    public static List<SimpleDomainBO> findAllAfter(Date date) {
        return getDefaultRepository().findByTheDate1After(date);
    }

    public static List<SimpleDomainBO> findAllByDate(Date date) {
        return getDefaultRepository().findByTheDate1(date);
    }

    public static Integer getSimpleDomainCount(Date date) {
        return findAllByDate(date) != null ? findAllByDate(date).size() : 0;
    }

    public static Page<SimpleDomainBO> findByNamePaged(String nameFilter, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return getDefaultRepository().findByNameContains(nameFilter, pageable);
    }

    
    /**
    * Lifecycle method that is invoked when saving SimpleDomain via a creation or update, 
    * only if the entity's fields were validated successfully. 
    * 
    * If SimpleDomain requires additional business logic in order to validate its data prior to saving, 
    * implement that logic here.
    * 
    * @see <a href="https://fermenter.atlassian.net/wiki/spaces/FER/pages/62128129/Stout#Stout-Savelifecycle">Stout: Save Lifecycle</a>
    */
	@Override
	protected void complexValidation() {
        /* add complex validation logic here */
	}
}
