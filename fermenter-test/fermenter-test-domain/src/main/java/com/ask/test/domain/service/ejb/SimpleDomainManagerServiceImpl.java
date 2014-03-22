package com.ask.test.domain.service.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ask.test.domain.bizobj.BusinessObjectFactory;
import com.ask.test.domain.bizobj.SimpleDomainBO;

/**
 * Service implementation class for the SimpleDomainManager service.
 * 
 * @see com.ask.test.domain.service.SimpleDomainManagerService
 * 
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Local(SimpleDomainManagerService.class)
@Stateless
public class SimpleDomainManagerServiceImpl extends SimpleDomainManagerBaseService implements SimpleDomainManagerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDomainManagerServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	protected Long getSimpleDomainCountImpl() {
		return RandomUtils.nextLong();
	}

	/**
	 * {@inheritDoc}
	 */
	protected String echoPlusWazzupImpl(String echoRoot) {
		StringBuilder sb = new StringBuilder();
		sb.append(echoRoot).append("WAZZUP");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Collection<SimpleDomainBO> selectAllSimpleDomainsImpl() {
		Collection<SimpleDomainBO> allInstances = new ArrayList<SimpleDomainBO>();
		for (int i = 0; i < randomPostiveInt(); i++) {
			SimpleDomainBO bo = BusinessObjectFactory.createSimpleDomainBO();
			bo.setName("SimpleDomain" + i);
			allInstances.add(bo);
		}

		return allInstances;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Integer countImpl(List<SimpleDomainBO> inputBOs) {		
		return (inputBOs == null) ? 0 : inputBOs.size();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Collection<SimpleDomainBO> selectAllSimpleDomainsByTypeImpl(String type) {
		Collection<SimpleDomainBO> allInstances = new ArrayList<SimpleDomainBO>();
		for (int i = 0; i < randomPostiveInt(); i++) {
			SimpleDomainBO bo = BusinessObjectFactory.createSimpleDomainBO();
			bo.setName("SimpleDomain" + i);
			bo.setType(type);
			allInstances.add(bo);
		}

		return allInstances;
	}
	
	private int randomPostiveInt() {
		int value = RandomUtils.nextInt(5);
		return (value == 0) ? 1 : value;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doSomethingImpl() {
		LOGGER.info("did something!");

	}

	/**
	 * {@inheritDoc}
	 */
	protected String doSomethingAndReturnACharacterImpl() {
		return RandomStringUtils.randomAlphabetic(1);
	}

	/**
	 * {@inheritDoc}
	 */
	protected SimpleDomainBO someBusinessOperationImpl(SimpleDomainBO someBusinessEntityBO, String otherImportantData) {
		someBusinessEntityBO.setName("This data is really important: " + otherImportantData);
		return someBusinessEntityBO;
	}

}