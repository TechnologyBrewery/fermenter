package com.ask.test.domain.service.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tigris.atlas.messages.MessageManager;
import org.tigris.atlas.messages.MessageUtils;

import com.ask.test.domain.bizobj.BusinessObjectFactory;
import com.ask.test.domain.bizobj.SimpleDomainBO;
import com.ask.test.domain.enumeration.SimpleDomainEnumeration;

/**
 * Service implementation class for the SimpleDomainManager service.
 * 
 * @see com.ask.test.domain.service.SimpleDomainManagerService
 * 
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Local(SimpleDomainManagerService.class)
@Stateless
public class SimpleDomainManagerServiceImpl extends SimpleDomainManagerBaseService implements
	SimpleDomainManagerService {

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
    protected SimpleDomainBO returnSingleSimpleDomainImpl() {
	return BusinessObjectFactory.createSimpleDomainBO();
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createAndPropagateErrorMessagesImpl(Integer numErrorMessagesToGenerate) {
	for (int iter = 0; iter < numErrorMessagesToGenerate; iter++) {
	    MessageManager.addMessage(MessageUtils.createErrorMessage(RandomStringUtils.randomAlphabetic(5),
		    new String[] {}, new Object[] {}));
	}

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SimpleDomainBO saveSimpleDomainEntityAndPropagateErrorMessagesImpl(String targetNameValue,
	    Integer numErrorMessagesToGenerate) {
	SimpleDomainBO simpleDomain = BusinessObjectFactory.createSimpleDomainBO();
	simpleDomain.setName(targetNameValue);
	simpleDomain.setType(RandomStringUtils.randomAlphabetic(10));
	simpleDomain.setTheDate1(new Date());
	simpleDomain.setAnEnumeratedValue(SimpleDomainEnumeration.values()[RandomUtils.nextInt(SimpleDomainEnumeration
		.values().length)]);
	simpleDomain.save();

	createAndPropagateErrorMessagesImpl(numErrorMessagesToGenerate);

	return simpleDomain;
    }
}