package org.bitbucket.askllc.fermenter.cookbook.domain.service.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainChildBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.enumeration.SimpleDomainEnumeration;
import org.bitbucket.askllc.fermenter.cookbook.domain.message.SampleMessages;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainManagerService;
import org.bitbucket.fermenter.stout.messages.CoreMessages;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.Severity;
import org.bitbucket.fermenter.stout.page.PageConverter;
import org.bitbucket.fermenter.stout.page.PageWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation class for the SimpleDomainManager service.
 * 
 * @see org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainManagerService
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY
 */
@Service
public class SimpleDomainManagerServiceImpl extends SimpleDomainManagerBaseServiceImpl
        implements SimpleDomainManagerService {

    private static final PageConverter<SimpleDomainBO> simpleDomainPageConverter = new PageConverter<>();
    private static final Logger logger = LoggerFactory.getLogger(SimpleDomainManagerServiceImpl.class);

    /**
     * Demonstrates how a one-off service operation that requires elements not
     * yet supported natively by Fermenter can be integrated into a
     * Fermenter-generated architecture.
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Response getLargeString(String simpleDomainId) {
        final SimpleDomainBO simpleDomain = SimpleDomainBO.findByPrimaryKey(UUID.fromString(simpleDomainId));
        if (simpleDomain == null) {
            return Response.status(Status.NOT_FOUND).build();
        } else {
            StreamingOutput output = new StreamingOutput() {

                @Override
                public void write(OutputStream output) throws IOException, WebApplicationException {
                    try (Writer writer = new BufferedWriter(new OutputStreamWriter(output))) {
                        writer.write(simpleDomain.getLargeString());
                    }
                }
            };
            return Response.ok(output).build();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getSimpleDomainCountImpl() {
        List<SimpleDomainBO> allSimpleDomains = SimpleDomainBO.findAll();
        return allSimpleDomains != null ? allSimpleDomains.size() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createAndPropagateErrorMessagesImpl(Integer numErrorMessagesToGenerate) {
        for (int iter = 0; iter < numErrorMessagesToGenerate; iter++) {
            Message message = new Message(CoreMessages.UNKNOWN_EXCEPTION_OCCURRED, Severity.ERROR);
            message.addInsert("differentiateFromOtherInstances", RandomStringUtils.random(10));
            MessageManager.addMessage(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createErrorMessageForGlobalErrorHandlerImpl(Integer numErrorMessagesToGenerate) {
        for (int i = 0; i < numErrorMessagesToGenerate; i++) {
            Message message = new Message(SampleMessages.EXAMPLE_SERVICE_FAILURE, Severity.ERROR);
            message.addInsert("attemptNumber", "" + i);
            MessageManager.addMessage(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SimpleDomainBO saveSimpleDomainEntityAndPropagateErrorMessagesImpl(String targetNameValue,
            Integer numErrorMessagesToGenerate) {
        SimpleDomainBO simpleDomain = new SimpleDomainBO();
        simpleDomain.setName(targetNameValue);
        simpleDomain.setType(RandomStringUtils.randomAlphabetic(10));
        simpleDomain.setTheDate1(new Date());
        simpleDomain.setAnEnumeratedValue(
                SimpleDomainEnumeration.values()[RandomUtils.nextInt(0, SimpleDomainEnumeration.values().length)]);
        simpleDomain.save();

        createAndPropagateErrorMessagesImpl(numErrorMessagesToGenerate);

        return simpleDomain;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<SimpleDomainBO> selectAllSimpleDomainsImpl() {
        return SimpleDomainBO.findAll();
    }

    @Override
    protected Collection<SimpleDomainBO> selectAllSimpleDomainsLazySimpleDomainChildImpl() {
        return SimpleDomainBO.findAllLazyLoadSimpleDomainChild();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SimpleDomainBO returnNullEntityImpl() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void deleteAllSimpleDomainsImpl() {
        SimpleDomainBO.deleteAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String echoPlusWazzupImpl(String echoRoot) {
        StringBuilder sb = new StringBuilder();
        sb.append(echoRoot).append("Wazzup");
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SimpleDomainBO someBusinessOperationImpl(SimpleDomainBO someBusinessEntity, String otherImportantData) {
        someBusinessEntity.setName("This data is really important: " + otherImportantData);
        return someBusinessEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<SimpleDomainBO> selectAllSimpleDomainsWithPagingImpl(Integer startPage, Integer pageSize) {
        return SimpleDomainBO.findAll(startPage, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<SimpleDomainBO> selectAllSimpleDomainsByTypeImpl(String type) {
        return SimpleDomainBO.findByType(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer countNumInputsImpl(List<SimpleDomainBO> input) {
        return input != null ? input.size() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void returnVoidImpl() {
        logger.info("did something, returning void");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSomethingWithPrimitiveInputsImpl(String inputStr, Integer inputInt) {
        logger.info("did something with {} and {} ", inputStr, inputInt);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String doSomethingAndReturnAPrimitiveImpl() {
        return RandomStringUtils.randomAlphabetic(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SimpleDomainBO methodWithSingleEntityAsParamImpl(SimpleDomainBO simpleDomain) {
        simpleDomain.setName(simpleDomain.getName() + " updated");
        return simpleDomain;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<Integer> returnManyPrimitivesImpl(Integer input, Integer returnListSize) {
        return IntStream.range(0, returnListSize).mapToObj(n -> input).collect(Collectors.toList());
    }

    @Override
    protected Collection<SimpleDomainBO> selectAllSimpleDomainsByDateImpl(Date inputDate) {
        return SimpleDomainBO.findAllByDate(inputDate);
    }

    @Override
    protected Collection<SimpleDomainBO> selectAllSimpleDomainsBeforeDateImpl(Date inputDate) {
        return SimpleDomainBO.findAllBefore(inputDate);
    }

    @Override
    protected Collection<SimpleDomainBO> selectAllSimpleDomainsAfterDateImpl(Date inputDate) {
        return SimpleDomainBO.findAllAfter(inputDate);
    }

    @Override
    protected Integer getCountForDateImpl(Date inputDate) {
        return SimpleDomainBO.getSimpleDomainCount(inputDate);
    }

    @Override
    protected void returnVoidForDateInputImpl(Date inputDate) {
        logger.info("made a call for SimpleDomain with {} ", inputDate);
    }

    @Override
    protected SimpleDomainBO updateAllSimpleDomainChildNamesImpl(String simpleDomainId, String childName) {
        SimpleDomainBO simpleDomain = SimpleDomainBO.findByPrimaryKey(UUID.fromString(simpleDomainId), false);
        for (SimpleDomainChildBO child : simpleDomain.getSimpleDomainChilds()) {
            child.setName(childName);
        }
        return simpleDomain;
    }

    @Override
    protected PageWrapper<SimpleDomainBO> getPagedSimpleDomainsImpl(Integer startPage, Integer count) {
        Page<SimpleDomainBO> entities = SimpleDomainBO.findAllPaged(startPage, count);
        return simpleDomainPageConverter.convertToPageWrapper(entities);
    }

    @Override
    protected PageWrapper<SimpleDomainBO> getPagedSimpleDomainsWithParameterImpl(String nameFilter, Integer startPage,
            Integer count) {
        Page<SimpleDomainBO> entities = SimpleDomainBO.findByNamePaged(nameFilter, startPage, count);
        return simpleDomainPageConverter.convertToPageWrapper(entities);
    }

    @Override
    protected PageWrapper<SimpleDomainBO> getPagedSimpleDomainsAsPostImpl(SimpleDomainBO simpleDomain,
            String nameFilter, Integer startPage, Integer count) {
        Page<SimpleDomainBO> entities = SimpleDomainBO.findByNamePaged(nameFilter, startPage, count);
        return simpleDomainPageConverter.convertToPageWrapper(entities);
    }

    @Override
    protected PageWrapper<SimpleDomainBO> getPagedResponseWithoutSpringPageImpl(Integer numberOfItemsInList,
            Integer startPage, Integer count) {
        List<SimpleDomainBO> simpleDomains = new ArrayList<>();
        for (int i = 0; i < count && i < numberOfItemsInList; i++) {
            SimpleDomainBO simpleDomain = new SimpleDomainBO();
            simpleDomain.setName(Integer.toString(i));
            simpleDomains.add(simpleDomain);
        }
        PageWrapper<SimpleDomainBO> pageWrapper = new PageWrapper<>();
        pageWrapper.setContent(simpleDomains);
        pageWrapper.setFirst(true);
        pageWrapper.setLast(count < numberOfItemsInList);
        pageWrapper.setItemsPerPage(count);
        pageWrapper.setNumberOfElements(simpleDomains.size());
        pageWrapper.setStartPage(startPage);
        pageWrapper.setTotalPages(numberOfItemsInList / count);
        pageWrapper.setTotalResults(numberOfItemsInList.longValue());

        return pageWrapper;
    }

    @Override
    protected Boolean provideNullInputFromFrontendImpl(String expectingThisToBeNull) {
        return expectingThisToBeNull == null;
    }

    @Override
    protected Boolean listAsParamFromFrontendImpl(List<String> genericList) {
        return genericList.size() == 2;
    }

}
