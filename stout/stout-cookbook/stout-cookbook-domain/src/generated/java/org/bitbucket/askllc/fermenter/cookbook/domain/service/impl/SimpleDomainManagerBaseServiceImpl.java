package org.bitbucket.askllc.fermenter.cookbook.domain.service.impl;

import java.lang.Integer;
import java.lang.String;
import java.util.Collection;
import java.util.List;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;

import javax.inject.Inject;

import org.bitbucket.fermenter.stout.messages.AbstractMsgMgrAwareService;
import org.bitbucket.fermenter.stout.messages.DefaultMessages;

import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.service.VoidServiceResponse;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainManagerService;

/**
 * Service implementation class for the SimpleDomainManager service
 * 
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainManagerService
 */
public abstract class SimpleDomainManagerBaseServiceImpl extends AbstractMsgMgrAwareService implements
		SimpleDomainManagerService {

	@Inject
	protected DefaultMessages messages;

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public VoidServiceResponse deleteAllSimpleDomains() {
		initMsgMgr(messages);

		deleteAllSimpleDomainsImpl();

		VoidServiceResponse response = new VoidServiceResponse();

		addAllMsgMgrToResponse(response);
		return response;
	}

	/**
	 * Performs the business function for DeleteAllSimpleDomains.
	 * 
	 * @return A instance of {@link null}
	 */
	protected abstract void deleteAllSimpleDomainsImpl();

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public ValueServiceResponse<Integer> getSimpleDomainCount() {
		initMsgMgr(messages);

		Integer result = getSimpleDomainCountImpl();

		ValueServiceResponse<Integer> response = new ValueServiceResponse<Integer>(result);

		addAllMsgMgrToResponse(response);
		return response;
	}

	/**
	 * Performs the business function for GetSimpleDomainCount.
	 */
	protected abstract Integer getSimpleDomainCountImpl();

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ValueServiceResponse<String> echoPlusWazzup(String echoRoot) {
		initMsgMgr(messages);

		String result = echoPlusWazzupImpl(echoRoot);

		ValueServiceResponse<String> response = new ValueServiceResponse<String>(result);

		addAllMsgMgrToResponse(response);
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
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public VoidServiceResponse createAndPropagateErrorMessages(Integer numErrorMessagesToGenerate) {
		initMsgMgr(messages);

		createAndPropagateErrorMessagesImpl(numErrorMessagesToGenerate);

		VoidServiceResponse response = new VoidServiceResponse();

		addAllMsgMgrToResponse(response);
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
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ValueServiceResponse<SimpleDomainBO> saveSimpleDomainEntityAndPropagateErrorMessages(String targetNameValue,
			Integer numErrorMessagesToGenerate) {
		initMsgMgr(messages);

		SimpleDomainBO result = saveSimpleDomainEntityAndPropagateErrorMessagesImpl(targetNameValue,
				numErrorMessagesToGenerate);

		ValueServiceResponse<SimpleDomainBO> response = new ValueServiceResponse<SimpleDomainBO>(result);

		addAllMsgMgrToResponse(response);
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
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ValueServiceResponse<SimpleDomainBO> someBusinessOperation(SimpleDomainBO someBusinessEntity,
			String otherImportantData) {
		initMsgMgr(messages);

		SimpleDomainBO result = someBusinessOperationImpl(someBusinessEntity, otherImportantData);

		ValueServiceResponse<SimpleDomainBO> response = new ValueServiceResponse<SimpleDomainBO>(result);

		addAllMsgMgrToResponse(response);
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
	protected abstract SimpleDomainBO someBusinessOperationImpl(SimpleDomainBO someBusinessEntity,
			String otherImportantData);

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ValueServiceResponse<Integer> countNumInputs(List<SimpleDomainBO> input) {
		initMsgMgr(messages);

		Integer result = countNumInputsImpl(input);

		ValueServiceResponse<Integer> response = new ValueServiceResponse<Integer>(result);

		addAllMsgMgrToResponse(response);
		return response;
	}

	/**
	 * Performs the business function for CountNumInputs.
	 * 
	 * @param input
	 */
	protected abstract Integer countNumInputsImpl(List<SimpleDomainBO> input);

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public ValueServiceResponse<Collection<SimpleDomainBO>> selectAllSimpleDomains() {
		initMsgMgr(messages);

		Collection<SimpleDomainBO> result = selectAllSimpleDomainsImpl();

		ValueServiceResponse<Collection<SimpleDomainBO>> response = new ValueServiceResponse<Collection<SimpleDomainBO>>(
				result);

		addAllMsgMgrToResponse(response);
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
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public ValueServiceResponse<Collection<SimpleDomainBO>> selectAllSimpleDomainsWithPaging(Integer startPage,
			Integer pageSize) {
		initMsgMgr(messages);

		Collection<SimpleDomainBO> result = selectAllSimpleDomainsWithPagingImpl(startPage, pageSize);

		ValueServiceResponse<Collection<SimpleDomainBO>> response = new ValueServiceResponse<Collection<SimpleDomainBO>>(
				result);

		addAllMsgMgrToResponse(response);
		return response;
	}

	/**
	 * Performs the business function for SelectAllSimpleDomainsWithPaging.
	 * 
	 * @param startPage
	 *            0-based start page
	 * @param pageSize
	 *            Maximum number of results to return
	 * @return A {@link Collection} of {@link SimpleDomainBO}
	 */
	protected abstract Collection<SimpleDomainBO> selectAllSimpleDomainsWithPagingImpl(Integer startPage,
			Integer pageSize);

	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public ValueServiceResponse<Collection<SimpleDomainBO>> selectAllSimpleDomainsByType(String type) {
		initMsgMgr(messages);

		Collection<SimpleDomainBO> result = selectAllSimpleDomainsByTypeImpl(type);

		ValueServiceResponse<Collection<SimpleDomainBO>> response = new ValueServiceResponse<Collection<SimpleDomainBO>>(
				result);

		addAllMsgMgrToResponse(response);
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
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ValueServiceResponse<SimpleDomainBO> returnNullEntity() {
		initMsgMgr(messages);

		SimpleDomainBO result = returnNullEntityImpl();

		ValueServiceResponse<SimpleDomainBO> response = new ValueServiceResponse<SimpleDomainBO>(result);

		addAllMsgMgrToResponse(response);
		return response;
	}

	/**
	 * Performs the business function for ReturnNullEntity.
	 */
	protected abstract SimpleDomainBO returnNullEntityImpl();

}