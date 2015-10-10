package org.bitbucket.fermenter.test.domain.service.ejb;

import java.lang.Integer;
import java.lang.String;
import java.util.Collection;
import java.util.List;
import org.bitbucket.fermenter.test.domain.transfer.SimpleDomain;

import org.bitbucket.fermenter.test.domain.bizobj.*;
import org.bitbucket.fermenter.test.domain.transfer.*;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.bitbucket.fermenter.messages.InjectableMessages;
import org.bitbucket.fermenter.messages.ThreadLocalMessages;
import org.bitbucket.fermenter.service.ValueServiceResponse;
import org.bitbucket.fermenter.service.VoidServiceResponse;

/**
 * Service session bean for the SimpleDomainManager service.
 * 
 * Generated Code - DO NOT MODIFY
 */
public abstract class SimpleDomainManagerBaseService {

	@Inject
	private InjectableMessages messages;

	@Resource
	private EJBContext ejbContext;

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@ThreadLocalMessages
	public ValueServiceResponse<Long> getSimpleDomainCount() {

		Long implResult = getSimpleDomainCountImpl();

		ValueServiceResponse<Long> response = new ValueServiceResponse<Long>(implResult, this.messages);

		return response;
	}

	/**
	 * Performs the business function for GetSimpleDomainCount.
	 */
	protected abstract Long getSimpleDomainCountImpl();

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@ThreadLocalMessages
	public VoidServiceResponse doSomething() {

		doSomethingImpl();

		VoidServiceResponse response = new VoidServiceResponse(this.messages);

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}
		return response;
	}

	/**
	 * Performs the business function for DoSomething.
	 * 
	 * @return A instance of {@link null}
	 */
	protected abstract void doSomethingImpl();

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@ThreadLocalMessages
	public ValueServiceResponse<String> echoPlusWazzup(String echoRoot) {

		String implResult = echoPlusWazzupImpl(echoRoot);

		ValueServiceResponse<String> response = new ValueServiceResponse<String>(implResult, this.messages);

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}
		return response;
	}

	/**
	 * Performs the business function for EchoPlusWazzup.
	 * 
	 * @param echoRoot
	 */
	protected abstract String echoPlusWazzupImpl(String echoRoot);

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@ThreadLocalMessages
	public VoidServiceResponse createAndPropagateErrorMessages(Integer numErrorMessagesToGenerate) {

		createAndPropagateErrorMessagesImpl(numErrorMessagesToGenerate);

		VoidServiceResponse response = new VoidServiceResponse(this.messages);

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}
		return response;
	}

	/**
	 * Performs the business function for CreateAndPropagateErrorMessages.
	 * 
	 * @param numErrorMessagesToGenerate
	 * @return A instance of {@link null}
	 */
	protected abstract void createAndPropagateErrorMessagesImpl(Integer numErrorMessagesToGenerate);

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@ThreadLocalMessages
	public ValueServiceResponse<SimpleDomain> saveSimpleDomainEntityAndPropagateErrorMessages(String targetNameValue,
			Integer numErrorMessagesToGenerate) {

		SimpleDomainBO implResult = saveSimpleDomainEntityAndPropagateErrorMessagesImpl(targetNameValue,
				numErrorMessagesToGenerate);

		SimpleDomain result = implResult != null ? SimpleDomainAssembler.createSimpleDomain(implResult) : null;
		ValueServiceResponse<SimpleDomain> response = new ValueServiceResponse<SimpleDomain>(result, this.messages);

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}
		return response;
	}

	/**
	 * Performs the business function for SaveSimpleDomainEntityAndPropagateErrorMessages.
	 * 
	 * @param targetNameValue
	 * @param numErrorMessagesToGenerate
	 */
	protected abstract SimpleDomainBO saveSimpleDomainEntityAndPropagateErrorMessagesImpl(String targetNameValue,
			Integer numErrorMessagesToGenerate);

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@ThreadLocalMessages
	public ValueServiceResponse<SimpleDomain> someBusinessOperation(SimpleDomain someBusinessEntity,
			String otherImportantData) {

		SimpleDomainBO someBusinessEntityBO = SimpleDomainAssembler.mergeSimpleDomainBO(someBusinessEntity, null);

		SimpleDomainBO implResult = someBusinessOperationImpl(someBusinessEntityBO, otherImportantData);

		SimpleDomain result = implResult != null ? SimpleDomainAssembler.createSimpleDomain(implResult) : null;
		ValueServiceResponse<SimpleDomain> response = new ValueServiceResponse<SimpleDomain>(result, this.messages);

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}
		return response;
	}

	/**
	 * Performs the business function for SomeBusinessOperation.
	 * 
	 * @param someBusinessEntity
	 *            The entity on which we should perform some business operation
	 * @param otherImportantData
	 *            Critical data to supplement the business entity
	 */
	protected abstract SimpleDomainBO someBusinessOperationImpl(SimpleDomainBO someBusinessEntityBO,
			String otherImportantData);

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@ThreadLocalMessages
	public ValueServiceResponse<Collection<SimpleDomain>> selectAllSimpleDomains() {

		Collection<SimpleDomainBO> implResult = selectAllSimpleDomainsImpl();

		Collection<SimpleDomain> result = implResult != null ? SimpleDomainAssembler.getSimpleDomainList(implResult)
				: null;
		ValueServiceResponse<Collection<SimpleDomain>> response = new ValueServiceResponse<Collection<SimpleDomain>>(
				result, this.messages);

		return response;
	}

	/**
	 * Performs the business function for SelectAllSimpleDomains.
	 * 
	 * @return A {@link Collection} of {@link SimpleDomainBO}
	 */
	protected abstract Collection<SimpleDomainBO> selectAllSimpleDomainsImpl();

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@ThreadLocalMessages
	public ValueServiceResponse<String> doSomethingAndReturnACharacter() {

		String implResult = doSomethingAndReturnACharacterImpl();

		ValueServiceResponse<String> response = new ValueServiceResponse<String>(implResult, this.messages);

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}
		return response;
	}

	/**
	 * Performs the business function for DoSomethingAndReturnACharacter.
	 */
	protected abstract String doSomethingAndReturnACharacterImpl();

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@ThreadLocalMessages
	public ValueServiceResponse<Integer> count(List<SimpleDomain> input) {

		List<SimpleDomainBO> inputBOs = SimpleDomainAssembler.getSimpleDomainBOList(input);

		Integer implResult = countImpl(inputBOs);

		ValueServiceResponse<Integer> response = new ValueServiceResponse<Integer>(implResult, this.messages);

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}
		return response;
	}

	/**
	 * Performs the business function for Count.
	 * 
	 * @param input
	 */
	protected abstract Integer countImpl(List<SimpleDomainBO> inputBO);

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@ThreadLocalMessages
	public ValueServiceResponse<SimpleDomain> returnSingleSimpleDomain() {

		SimpleDomainBO implResult = returnSingleSimpleDomainImpl();

		SimpleDomain result = implResult != null ? SimpleDomainAssembler.createSimpleDomain(implResult) : null;
		ValueServiceResponse<SimpleDomain> response = new ValueServiceResponse<SimpleDomain>(result, this.messages);

		return response;
	}

	/**
	 * Performs the business function for ReturnSingleSimpleDomain.
	 */
	protected abstract SimpleDomainBO returnSingleSimpleDomainImpl();

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@ThreadLocalMessages
	public ValueServiceResponse<Collection<SimpleDomain>> selectAllSimpleDomainsByType(String type) {

		Collection<SimpleDomainBO> implResult = selectAllSimpleDomainsByTypeImpl(type);

		Collection<SimpleDomain> result = implResult != null ? SimpleDomainAssembler.getSimpleDomainList(implResult)
				: null;
		ValueServiceResponse<Collection<SimpleDomain>> response = new ValueServiceResponse<Collection<SimpleDomain>>(
				result, this.messages);

		return response;
	}

	/**
	 * Performs the business function for SelectAllSimpleDomainsByType.
	 * 
	 * @param type
	 * @return A {@link Collection} of {@link SimpleDomainBO}
	 */
	protected abstract Collection<SimpleDomainBO> selectAllSimpleDomainsByTypeImpl(String type);

	/**
	 * {@inheritDoc}
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@ThreadLocalMessages
	public ValueServiceResponse<SimpleDomain> returnNullEntity() {

		SimpleDomainBO implResult = returnNullEntityImpl();

		SimpleDomain result = implResult != null ? SimpleDomainAssembler.createSimpleDomain(implResult) : null;
		ValueServiceResponse<SimpleDomain> response = new ValueServiceResponse<SimpleDomain>(result, this.messages);

		if (this.messages.hasErrorMessages()) {
			this.ejbContext.setRollbackOnly();
		}
		return response;
	}

	/**
	 * Performs the business function for ReturnNullEntity.
	 */
	protected abstract SimpleDomainBO returnNullEntityImpl();

}