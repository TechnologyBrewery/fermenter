package org.bitbucket.fermenter.mda.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.MetamodelConfig;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceUrl;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;
import org.bitbucket.fermenter.mda.metamodel.element.Operation;
import org.bitbucket.fermenter.mda.metamodel.element.OperationElement;
import org.bitbucket.fermenter.mda.metamodel.element.Parameter;
import org.bitbucket.fermenter.mda.metamodel.element.ParameterElement;
import org.bitbucket.fermenter.mda.metamodel.element.Return;
import org.bitbucket.fermenter.mda.metamodel.element.ReturnElement;
import org.bitbucket.fermenter.mda.metamodel.element.Service;
import org.bitbucket.fermenter.mda.metamodel.element.ServiceElement;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ServiceSteps {

    private static final String INTEGER = "integer";
    private static final String FERMENTER_MDA = "fermenter-mda";
    private static final boolean IS_PAGED_RESPONSE_DISABLED = false;
    private static final String TX_REQUIRED = "Required";
    private static final String TX_SUPPORTS = "Supports";
    private static final String DEFAULT_NAME = "NamedService";
    private static final String DEFAULT_PACKAGE = "org.example";
    private static final String DEFAULT_OPERATION = "defaultOperation";
    private static final boolean IS_PAGED_RESPONSE_ENABLED = true;
    
    private static final MetamodelConfig config = KrauseningConfigFactory.create(MetamodelConfig.class);
    
    private ObjectMapper objectMapper = new ObjectMapper();
    private MessageTracker messageTracker = MessageTracker.getInstance();
    private File servicesDirectory = new File("target/temp-metadata", config.getServicesRelativePath());

    private String currentBasePackage;

    private File serviceFile;
    private Service loadedService;
    protected GenerationException encounteredException;
    protected DefaultModelInstanceRepository metadataRepo;

    // Also uses CommonSteps for setup and tear down    
    
    @After("@service")
    public void cleanUp() {
        loadedService = null;

        messageTracker.clear();

        currentBasePackage = null;
    }

    @Given("^a service named \"([^\"]*)\" in \"([^\"]*)\"$")
    public void a_service_named_in(String name, String packageValue) throws Throwable {
        createServiceElement(name, packageValue, null, null, null, null, false, false, null,
                IS_PAGED_RESPONSE_DISABLED);
    }

    @Given("^a service named \"([^\"]*)\" in \"([^\"]*)\" with an operation \"([^\"]*)\" with no parameters$")
    public void a_service_named_in_with_an_operation_with_no_parameters(String name, String packageValue,
            String operationName) throws Throwable {
        createServiceElement(name, packageValue, operationName, null, null, null, false, false, TX_SUPPORTS,
                IS_PAGED_RESPONSE_DISABLED);
    }

    @Given("^a service named \"([^\"]*)\" in \"([^\"]*)\" with an operation \"([^\"]*)\" with a void return type$")
    public void a_service_named_in_with_an_operation_with_a_void_return_type(String name, String packageValue,
            String operationName) throws Throwable {
        createServiceElement(name, packageValue, operationName, "void", null, null, false, false, TX_REQUIRED,
                IS_PAGED_RESPONSE_DISABLED);
    }

    @Given("^a service named \"([^\"]*)\" in \"([^\"]*)\" with an operation \"([^\"]*)\" with parameters \"([^\"]*)\" of type \"([^\"]*)\"$")
    public void a_service_named_in_with_an_operation_with_parameters_of_type(String name, String packageValue,
            String operationName, List<String> paramNames, List<String> paramValues) throws Throwable {
        createServiceElement(name, packageValue, operationName, null, paramNames, paramValues, false, false,
                TX_SUPPORTS, IS_PAGED_RESPONSE_DISABLED);
    }

    @Given("^a service named \"([^\"]*)\" in \"([^\"]*)\" with an operation \"([^\"]*)\" with the return type \"([^\"]*)\"$")
    public void a_service_named_in_with_an_operation_with_the_return_type(String name, String packageValue,
            String operationName, String returnType) throws Throwable {
        createServiceElement(name, packageValue, operationName, returnType, null, null, false, false, TX_SUPPORTS,
                IS_PAGED_RESPONSE_DISABLED);
    }

    @Given("^a service named \"([^\"]*)\" in \"([^\"]*)\" with an operation \"([^\"]*)\" with many parameters \"([^\"]*)\" of type \"([^\"]*)\"$")
    public void a_service_named_in_with_an_operation_with_many_parameters_of_type(String name, String packageValue,
            String operationName, List<String> paramNames, List<String> paramValues) throws Throwable {
        createServiceElement(name, packageValue, operationName, null, paramNames, paramValues, false, true, TX_SUPPORTS,
                IS_PAGED_RESPONSE_DISABLED);
    }

    @Given("^a service named \"([^\"]*)\" in \"([^\"]*)\" with an operation \"([^\"]*)\" with the many return type \"([^\"]*)\"$")
    public void a_service_named_in_with_an_operation_with_the_many_return_type(String name, String packageValue,
            String operationName, String returnType) throws Throwable {
        createServiceElement(name, packageValue, operationName, returnType, null, null, true, false, TX_REQUIRED,
                IS_PAGED_RESPONSE_DISABLED);
    }

    @Given("^a service named \"([^\"]*)\" in \"([^\"]*)\" with an operation \"([^\"]*)\" with the transaction attribute \"([^\"]*)\"$")
    public void a_service_named_in_with_an_operation_with_the_transaction_attribute(String name, String packageValue,
            String operationName, String transactionAttribute) throws Throwable {
        createServiceElement(name, packageValue, operationName, "void", null, null, false, false, transactionAttribute,
                IS_PAGED_RESPONSE_DISABLED);
    }

    @Given("^a service named \"([^\"]*)\" in \"([^\"]*)\" with an operation \"([^\"]*)\" with a void return type in default$")
    public void a_service_named_in_with_an_operation_with_a_void_return_type_in_default(String name,
            String packageValue, String operationName) throws Throwable {
        createServiceElement(name, packageValue, operationName, "void", null, null, false, false, null,
                IS_PAGED_RESPONSE_DISABLED);
    }

    @Given("^a service with an operation named \"([^\"]*)\" with paged response \"([^\"]*)\"$")
    public void a_service_with_an_operation_named_with_paged_response(String operationName, String isEnabled)
            throws Throwable {
        boolean isPagedResponseEnabled = "enabled".equals(isEnabled);
        createServiceElement(DEFAULT_NAME, DEFAULT_PACKAGE, operationName, INTEGER, null, null, true, false, null,
                isPagedResponseEnabled);
    }

    @Given("^a service with an operation named \"([^\"]*)\" with pagedResponse enabled and return type is void$")
    public void a_service_with_an_operation_named_with_pagedResponse_enabled_and_return_type_is_void(String arg1)
            throws Throwable {
        createServiceElement(DEFAULT_NAME, DEFAULT_PACKAGE, DEFAULT_OPERATION, "void", null, null, true, false, null,
                IS_PAGED_RESPONSE_ENABLED);
    }

    @Given("^a service with with a paged operation with \"([^\"]*)\" parameters$")
    public void the_operation_has_as_parameters(List<String> parameters) throws Throwable {
        List<String> parameterTypes = new ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            parameterTypes.add(INTEGER);
        }
        createServiceElement(DEFAULT_NAME, DEFAULT_PACKAGE, DEFAULT_OPERATION, INTEGER, parameters, parameterTypes,
                true, false, null, IS_PAGED_RESPONSE_ENABLED);
    }

    private void createServiceElement(String name, String packageValue, String operationName, String returnType,
            List<String> paramNames, List<String> paramTypes, boolean useManyResponse, boolean useManyParams,
            String transaction, boolean isPagedResponse) throws Throwable {
        ServiceElement service = new ServiceElement();
        if (StringUtils.isNotBlank(name)) {
            service.setName(name);
        }
        service.setPackage(packageValue);
        service.setDocumentation(RandomStringUtils.randomAlphanumeric(15));

        if (operationName != null) {
            OperationElement operation = new OperationElement();
            operation.setName(operationName);

            if (StringUtils.isNoneBlank(transaction)) {
                operation.setTransactionAttribute(transaction);
            }

            service.getOperations().add(operation);

            if (paramNames != null) {
                int i = 0;
                for (String paramName : paramNames) {
                    ParameterElement parameter = createParameterForOperation(paramTypes, useManyParams, i, paramName);

                    operation.getParameters().add(parameter);
                    i++;
                }

                if (paramNames.isEmpty() && !paramTypes.isEmpty()) {
                    // for testing, allow a parameter with no name in this
                    // situation:
                    ParameterElement noNameParameter = new ParameterElement();
                    noNameParameter.setType(paramTypes.iterator().next());
                    operation.getParameters().add(noNameParameter);
                }

            }

            ReturnElement returnElement = createReturnElementForOperation(returnType, useManyResponse, isPagedResponse);

            operation.setReturn(returnElement);
        }

        serviceFile = new File(servicesDirectory, name + ".json");
        objectMapper.writeValue(serviceFile, service);
        assertTrue("Services not written to file!", serviceFile.exists());

        currentBasePackage = packageValue;
    }

    private ParameterElement createParameterForOperation(List<String> paramTypes, boolean useManyParams, int i,
            String paramName) {
        ParameterElement parameter = new ParameterElement();
        parameter.setName(paramName);
        String type = paramTypes != null && !paramTypes.isEmpty() ? paramTypes.get(i) : null;
        parameter.setType(type);
        if (useManyParams) {
            parameter.setMany(true);
        }
        return parameter;
    }

    private ReturnElement createReturnElementForOperation(String returnType, boolean useManyResponse,
            boolean isPagedResponse) {
        ReturnElement returnElement = new ReturnElement();
        if (returnType == null) {
            returnElement.setType("void");

        } else {
            returnElement.setType(returnType);

        }

        if (useManyResponse) {
            returnElement.setMany(true);
        }

        returnElement.setPagedResponse(isPagedResponse);
        return returnElement;
    }

    @When("^services are read$")
    public void services_are_read() throws Throwable {
        encounteredException = null;

        try {
            ModelRepositoryConfiguration config = new ModelRepositoryConfiguration();
            config.setArtifactId(FERMENTER_MDA);
            config.setBasePackage(currentBasePackage);
            Map<String, ModelInstanceUrl> metadataUrlMap = config.getMetamodelInstanceLocations();
            metadataUrlMap.put(FERMENTER_MDA,
                    new ModelInstanceUrl(FERMENTER_MDA, servicesDirectory.getParentFile().toURI().toString()));

            metadataRepo = new DefaultModelInstanceRepository(config);
            metadataRepo.load();
            metadataRepo.validate();

        } catch (GenerationException e) {
            encounteredException = e;
        }
    }

    @Then("^a service metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\"$")
    public void a_service_metamodel_instance_is_returned_for_the_name_in(String name, String packageValue)
            throws Throwable {
        validateLoadedServices(name, packageValue, null, null, null, null, false);
    }

    @Then("^NO service metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\"$")
    public void no_service_metamodel_instance_is_returned_for_the_name_in(String name, String packageValue)
            throws Throwable {
        if (encounteredException != null) {
            throw encounteredException;
        }

        Map<String, Service> packageServices = metadataRepo.getServices(packageValue);
        loadedService = (packageServices != null) ? packageServices.get(name) : null;
        assertNull("Should not have found a service for this package and name!", loadedService);
    }

    @Then("^an operation \"([^\"]*)\" without parameters is found on service \"([^\"]*)\" in \"([^\"]*)\"$")
    public void an_operation_without_parameters_is_found_on_service_in(String operationName, String name,
            String packageValue) throws Throwable {
        validateLoadedServices(name, packageValue, null, null, null, null, false);
        Operation foundOperation = getOperationFromLoadedService(operationName);
        assertEquals("Unexpected number of parameters found!", 0, foundOperation.getParameters().size());
    }

    @Then("^an operation \"([^\"]*)\" with a void return type is found on service \"([^\"]*)\" in \"([^\"]*)\"$")
    public void an_operation_with_a_void_return_type_is_found_on_service_in(String operationName, String name,
            String packageValue) throws Throwable {
        validateLoadedServices(name, packageValue, null, null, null, null, false);
        Operation foundOperation = getOperationFromLoadedService(operationName);
        assertEquals("Unexpected return type found!", "void", foundOperation.getReturn().getType());
    }

    @Then("^an operation \"([^\"]*)\" is found on service \"([^\"]*)\" in \"([^\"]*)\" with parameters \"([^\"]*)\" of type \"([^\"]*)\"$")
    public void an_operation_is_found_on_service_in_with_parameters_of_type(String operationName, String name,
            String packageValue, List<String> expectedParamNames, List<String> expectedParamValues) throws Throwable {
        validateParameters(operationName, name, packageValue, expectedParamNames, expectedParamValues, false);
    }

    @Then("^an operation \"([^\"]*)\" is found on service \"([^\"]*)\" in \"([^\"]*)\" with the return type \"([^\"]*)\"$")
    public void an_operation_is_found_on_service_in_with_the_return_type(String operationName, String name,
            String packageValue, String expectedReturnType) throws Throwable {
        validateReturnType(operationName, name, packageValue, expectedReturnType, false);
    }

    @Then("^an operation \"([^\"]*)\" is found on service \"([^\"]*)\" in \"([^\"]*)\" with many parameters \"([^\"]*)\" of type \"([^\"]*)\"$")
    public void an_operation_is_found_on_service_in_with_many_parameters_of_type(String operationName, String name,
            String packageValue, List<String> expectedParamNames, List<String> expectedParamValues) throws Throwable {
        validateParameters(operationName, name, packageValue, expectedParamNames, expectedParamValues, true);
    }

    @Then("^an operation \"([^\"]*)\" is found on service \"([^\"]*)\" in \"([^\"]*)\" with the many return type \"([^\"]*)\"$")
    public void an_operation_is_found_on_service_in_with_the_many_return_type(String operationName, String name,
            String packageValue, String expectedReturnType) throws Throwable {
        validateReturnType(operationName, name, packageValue, expectedReturnType, true);
    }

    @Then("^an operation \"([^\"]*)\" is found on service \"([^\"]*)\" in \"([^\"]*)\" with the transaction attribute \"([^\"]*)\"$")
    public void an_operation_is_found_on_service_in_with_the_transaction_attribute(String operationName, String name,
            String packageValue, String expectedTransactionAttribute) throws Throwable {
        validateLoadedServices(name, packageValue, null, null, null, null, false);
        Operation foundOperation = getOperationFromLoadedService(operationName);
        String foundTransactionAttribute = foundOperation.getTransactionAttribute();
        assertEquals("Unexpected transaction attribute value!", expectedTransactionAttribute,
                foundTransactionAttribute);
    }

    @Then("^a service metamodel instance is returned with operation \"([^\"]*)\" with paged response \"([^\"]*)\"$")
    public void a_service_metamodel_instance_is_returned_with_operation_with_paged_response(String operationName,
            String expectedIsPagedResponse) throws Throwable {
        validateLoadedServices(DEFAULT_NAME, DEFAULT_PACKAGE, null, null, null, null, false);
        Operation foundOperation = getOperationFromLoadedService(operationName);
        assertNotNull("Failed to find the expected operation!", foundOperation);
        boolean expectedIsPagedResponseAsBoolean = "enabled".equals(expectedIsPagedResponse);
        assertEquals("Unexpected isPagedResponse value!", expectedIsPagedResponseAsBoolean,
                foundOperation.getReturn().isPagedResponse());
    }

    @Then("^an error is thrown for \"([^\"]*)\" because you cannot have pagedResponse enabled and return type is void$")
    public void an_error_is_thrown_for_because_you_cannot_have_pagedResponse_enabled_and_return_type_is_void(
            String arg1) throws Throwable {
        assertNotNull("Expected to have encountered an error!", encounteredException);
    }

    @Then("^the operation has \"([^\"]*)\" parameters which now included as the last two parameters$")
    public void the_operation_has_parameters_which_now_included_as_parameters(List<String> expectedParameters)
            throws Throwable {
        validateLoadedServices(DEFAULT_NAME, DEFAULT_PACKAGE, null, null, null, null, false);
        Operation foundOperation = getOperationFromLoadedService(DEFAULT_OPERATION);
        List<Parameter> actualParameters = foundOperation.getParameters();
        assertEquals("The number of parameters didn't match!", expectedParameters.size(), actualParameters.size());

        for (int i = 0; i < expectedParameters.size(); i++) {
            String expectedParameter = expectedParameters.get(i);
            Parameter actualParameter = actualParameters.get(i);
            assertEquals(expectedParameter, actualParameter.getName());
        }
    }

    private void validateParameters(String operationName, String name, String packageValue,
            List<String> expectedParamNames, List<String> expectedParamValues, boolean expectedManyValue) {
        validateLoadedServices(name, packageValue, null, null, null, null, false);
        Operation foundOperation = getOperationFromLoadedService(operationName);
        List<Parameter> foundParameters = foundOperation.getParameters();
        assertEquals("Unexpected number of parameters encountered!", expectedParamNames.size(), foundParameters.size());

        int i = 0;
        Map<String, Parameter> foundParametersMap = getParametersAsMap(foundParameters);
        for (String expectedParamName : expectedParamNames) {
            Parameter foundParameter = foundParametersMap.get(expectedParamName);
            assertEquals("Unexpected parameter type encountered!", expectedParamValues.get(i),
                    foundParameter.getType());
            assertEquals("Unexpected many parameter many value!", expectedManyValue, foundParameter.isMany());
            i++;
        }
    }

    private void validateReturnType(String operationName, String name, String packageValue, String expectedReturnType,
            boolean expectedIsMany) {
        validateLoadedServices(name, packageValue, null, null, null, null, false);
        Operation foundOperation = getOperationFromLoadedService(operationName);
        Return foundReturn = foundOperation.getReturn();
        assertEquals("Unexpected return type found!", expectedReturnType, foundReturn.getType());
        assertEquals("Unexpected many return type value!", expectedIsMany, foundReturn.isMany());
    }

    private Map<String, Parameter> getParametersAsMap(List<Parameter> parameters) {
        Map<String, Parameter> paramMap = new HashMap<>();
        for (Parameter parameter : parameters) {
            paramMap.put(parameter.getName(), parameter);
        }

        return paramMap;
    }

    private Operation getOperationFromLoadedService(String operationName) {
        List<Operation> operations = loadedService.getOperations();

        Operation foundOperation = null;
        for (Operation operation : operations) {
            if (operationName.equals(operation.getName())) {
                foundOperation = operation;
            }
        }

        assertNotNull("Should have found an operation named '" + operationName + "'", foundOperation);
        return foundOperation;
    }

    private void validateLoadedServices(String name, String packageName, List<String> parameterNames,
            List<String> parameterValues, String returnType, String paramNamesWithMany, boolean isReturnTypeMany) {
        if (encounteredException != null) {
            throw encounteredException;
        }

        loadedService = metadataRepo.getServices(packageName).get(name);
        assertEquals("Unexpected service name!", name, loadedService.getName());
        assertEquals("Unexpected service package!", packageName, loadedService.getPackage());

    }

}
