package org.bitbucket.fermenter.stout.test.domain.service.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageUtils;
import org.bitbucket.fermenter.stout.test.domain.bizobj.BusinessObjectFactory;
import org.bitbucket.fermenter.stout.test.domain.bizobj.SimpleDomainBO;
import org.bitbucket.fermenter.stout.test.domain.enumeration.SimpleDomainEnumeration;
import org.bitbucket.fermenter.stout.test.domain.transfer.SimpleDomain;
import org.bitbucket.fermenter.stout.test.domain.transfer.TransferObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	@EJB
	private SimpleDomainMaintenanceService simpleDomainMaintenanceService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDomainManagerServiceImpl.class);
	
	private static boolean hasInitiated;

	/**
	 * {@inheritDoc}
	 */
	protected Long getSimpleDomainCountImpl() {
		return RandomUtils.nextLong(0L, 100L);
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
    protected Collection<SimpleDomainBO> selectAllSimpleDomainsWithPagingImpl(Integer firstResult, Integer maxResults) {
        if (!hasInitiated) {
            deleteAllSimpleDomains();
            
            for (int i = 0; i < 10; i++) {
                SimpleDomainBO bo = BusinessObjectFactory.createSimpleDomainBO();
                bo.setName("SimpleDomainPaging-" + i);
                simpleDomainMaintenanceService.saveOrUpdate(bo.getSimpleDomainValues());
            }
            
            hasInitiated = true;
        }                
        
        return SimpleDomainBO.selectAllSimpleDomainsByPage(firstResult, maxResults);
    }	

    /**
     * {@inheritDoc}
     */
	protected Collection<SimpleDomainBO> deleteAllSimpleDomainsImpl() {
		// create an instance:
		SimpleDomain simpleDomain = TransferObjectFactory.createSimpleDomain();
		simpleDomain.setName("DELETE ME");
		simpleDomainMaintenanceService.saveOrUpdate(simpleDomain);
		
		// determine how many are currently around (at least one should be):
		List<SimpleDomainBO> allInstanceList = SimpleDomainBO.selectAllSimpleDomains();
		int size = allInstanceList.size();
		
		// delete and verify that the number reported as deleted equals the number found in the prior step:
		int numberDeleted = SimpleDomainBO.deleteAllSimpleDomains();
		if (numberDeleted != size) {
			if (size == 0) {
				MessageManager.addMessage(MessageUtils.createErrorMessage("bad-number-of-deletions",
						new String[] {}, new Object[] {}));
			}	
		}
		
		return SimpleDomainBO.selectAllSimpleDomains();
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
		int value = RandomUtils.nextInt(0, 5);
		return (value == 0) ? 1 : value;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void doSomethingImpl() {
		LOGGER.info("did something!");

	}

	@Override
	protected void doSomethingWithPrimitiveInputsImpl(String inputStr, Integer inputInt) {
		LOGGER.info("did something with " + inputStr + " and " + inputInt);
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
		simpleDomain.setAnEnumeratedValue(SimpleDomainEnumeration.values()[RandomUtils.nextInt(0, SimpleDomainEnumeration
				.values().length)]);
		simpleDomain.save();

		createAndPropagateErrorMessagesImpl(numErrorMessagesToGenerate);

		return simpleDomain;
	}

	@Override
	protected SimpleDomainBO returnNullEntityImpl() {
		// this service operation implementation intentionally returns null
		return null;
	}

}