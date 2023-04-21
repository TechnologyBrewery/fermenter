package org.technologybrewery.fermenter.cookbook.domain.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.technologybrewery.fermenter.cookbook.domain.enumeration.SimpleDomainEnumeration;
import org.technologybrewery.fermenter.cookbook.domain.service.rest.ContractTestService;
import org.technologybrewery.fermenter.stout.messages.Message;
import org.technologybrewery.fermenter.stout.messages.MessageManager;
import org.technologybrewery.fermenter.stout.messages.MetaMessage;
import org.technologybrewery.fermenter.stout.messages.Severity;
import org.technologybrewery.fermenter.stout.page.PageConverter;
import org.technologybrewery.fermenter.stout.page.PageWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

/**
 * Service implementation class for the ContractTest service.
 * 
 * @see org.technologybrewery.fermenter.cookbook.domain.service.rest.ContractTestService
 *
 *      GENERATED STUB CODE - PLEASE *DO* MODIFY Genereated from service.impl.java.vm
 */
@Service
public class ContractTestServiceImpl extends ContractTestBaseServiceImpl implements ContractTestService {

    private void addMessageToReponse(Severity severity, String methodName) {
        Message message = new Message (new ContractTestMessage(), severity);
        message.addInsert("testMethodName", methodName);
        MessageManager.addMessage(message);
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void voidResponseMethodImpl() {
        addMessageToReponse(Severity.INFO, "voidResponseMethod");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String stringResponseMethodImpl() {
        addMessageToReponse(Severity.INFO, "stringResponseMethod");

        return RandomStringUtils.randomAlphanumeric(10);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SimpleDomainBO entityResponseMethodImpl() {
        addMessageToReponse(Severity.INFO, "entityResponseMethod");

        SimpleDomainBO entity = new SimpleDomainBO();
        entity.setName(RandomStringUtils.randomAlphanumeric(10));
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SimpleDomainEnumeration enumerationResponseMethodImpl() {
        addMessageToReponse(Severity.INFO, "enumerationResponseMethod");

        return SimpleDomainEnumeration.FIRST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<String> multipleStringsResponseMethodImpl() {
        addMessageToReponse(Severity.INFO, "multipleStringsResponseMethod");
        return returnMultipleStrings();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<SimpleDomainBO> multipleEntitiesResponseMethodImpl() {
        addMessageToReponse(Severity.INFO, "multipleEntitiesResponseMethod");

        return returnMultipleEntities();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PageWrapper<String> multipleStringsPagedResponseMethodImpl(Integer startPage, Integer count) {
        addMessageToReponse(Severity.INFO, "multipleStringsPagedResponseMethod");
        List<String> responseCollection = returnMultipleStrings();
        PageConverter<String> stringPageMapper = new PageConverter<>();
        Page<String> entities = new PageImpl<>(responseCollection);
        return stringPageMapper.convertToPageWrapper(entities);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PageWrapper<SimpleDomainBO> multipleEntitiesPagedResponseMethodImpl(Integer startPage, Integer count) {
        addMessageToReponse(Severity.INFO, "multipleEntitiesPagedResponseMethod");
        List<SimpleDomainBO> responseCollection = returnMultipleEntities();
        PageConverter<SimpleDomainBO> simpleDomainBOPageMapper = new PageConverter<>();
        Page<SimpleDomainBO> entities = new PageImpl<>(responseCollection);
        return simpleDomainBOPageMapper.convertToPageWrapper(entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void noParameterMethodImpl() {
        addMessageToReponse(Severity.INFO, "noParameterMethod");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void stringParameterMethodImpl(String inputStr) {
        addMessageToReponse(Severity.INFO, "stringParameterMethod");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void entityParameterMethodImpl(SimpleDomainBO entity) {
        addMessageToReponse(Severity.INFO, "entityParameterMethod");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void enumerationParameterMethodImpl(SimpleDomainEnumeration enumeration) {
        addMessageToReponse(Severity.INFO, "enumerationParameterMethod");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void entityAndStringParametersMethodImpl(String inputStr, SimpleDomainBO entity) {
        addMessageToReponse(Severity.INFO, "entityAndStringParametersMethod");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void multipleStringParametersMethodImpl(List<String> inputStr) {
        addMessageToReponse(Severity.INFO, "multipleStringParametersMethod");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void multipleEntitiesParameterMethodImpl(List<SimpleDomainBO> entity) {
        addMessageToReponse(Severity.INFO, "multipleEntitiesParameterMethod");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void errorMessagesReturnedMethodImpl() {
        addMessageToReponse(Severity.ERROR, "errorMessagesReturnedMethod");

    }

    private List<SimpleDomainBO> returnMultipleEntities() {
        List<SimpleDomainBO> entities = new ArrayList<>();

        SimpleDomainBO entity1 = new SimpleDomainBO();
        entity1.setAnEnumeratedValue(SimpleDomainEnumeration.FIRST);
        entities.add(entity1);

        SimpleDomainBO entity2 = new SimpleDomainBO();
        entity2.setAnEnumeratedValue(SimpleDomainEnumeration.SECOND);
        entities.add(entity2);
        return entities;
    }

    private List<String> returnMultipleStrings() {
        List<String> strings = new ArrayList<>();
        strings.add(RandomStringUtils.randomAlphanumeric(10));
        strings.add(RandomStringUtils.randomAlphanumeric(10));
        return strings;
    }
    
    private class ContractTestMessage implements MetaMessage {
        
        @Override
        public String toString() {
            return "contract.test";
        }

        @Override
        public String getText() {
            return "This is a test. This station is conducting a test of the Service Contract options. This is only a test.";
        }
        
    }

}
