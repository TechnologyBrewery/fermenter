package org.technologybrewery.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.technologybrewery.fermenter.cookbook.domain.service.rest.SimpleDomainMaintenanceService;
import org.technologybrewery.fermenter.cookbook.domain.service.rest.SimpleDomainManagerService;
import org.technologybrewery.fermenter.stout.authn.AuthenticationTestUtils;
import org.technologybrewery.fermenter.stout.page.PageWrapper;
import org.technologybrewery.fermenter.stout.page.json.FindByExampleCriteria;
import org.technologybrewery.fermenter.stout.service.ValueServiceResponse;
import org.technologybrewery.fermenter.stout.sort.SortWrapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class ServicePagingSupportSteps {

    private static final int NEGATIVE_PAGE_INDEX = -10;
    private static final Integer FIRST_PAGE = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final int LARGE_SIZE_TO_INCLUDE_ALL = 9999;
    private static final Integer NEGATIVE_PAGE_SIZE = -10;

    @Inject
    private SimpleDomainManagerService simpleDomainManagerService;

    @Inject
    private SimpleDomainMaintenanceService simpleDomainMaintenanceService;

    private PageWrapper<SimpleDomainBO> responseValue;
    private ValueServiceResponse<PageWrapper<SimpleDomainBO>> response;
    private int numberOfItemsInList;

    @Before("@pagingSupport")
    public void setup() {
        AuthenticationTestUtils.login("testUser");
    }
    
    @After("@pagingSupport")
    public void cleanup() {
        simpleDomainManagerService.deleteAllSimpleDomains();
        
        AuthenticationTestUtils.logout();
    }

    @Given("^a list of (\\d+) random items that are not stored in the database$")
    public void a_list_of_random_items_that_are_not_stored_in_the_database(int numberOfItemsInList) throws Throwable {
        this.numberOfItemsInList = numberOfItemsInList;
    }

    @Given("^there are (\\d+) simple domain BOs in the system$")
    public void there_are_simple_domain_BOs_in_the_system(int numberOfDomains) throws Throwable {
        TestUtils.createAndPersistRandomSimpleDomains(numberOfDomains);
    }

    @When("^I request page \"([^\"]*)\" of \"([^\"]*)\" number of simple domains$")
    public void i_request_page_of_number_of_simple_domains(Integer pageIndex, Integer pageSize) throws Throwable {
        response = simpleDomainManagerService.getPagedSimpleDomains(pageIndex, pageSize);
        responseValue = response.getValue();
    }

    @When("^I request a paged service without providing a page index$")
    public void i_request_a_paged_service_without_providing_a_page_index() throws Throwable {
        response = simpleDomainManagerService.getPagedSimpleDomains(null, DEFAULT_PAGE_SIZE);
        responseValue = response.getValue();
    }

    @When("^I request a paged service without providing a page size$")
    public void i_request_a_paged_service_without_providing_a_page_size() throws Throwable {
        response = simpleDomainManagerService.getPagedSimpleDomains(FIRST_PAGE, null);
        responseValue = response.getValue();
    }

    @When("^I request all BOs from a service that provides paged responses$")
    public void i_request_all_BOs_from_a_service_that_provides_paged_responses() throws Throwable {
        response = simpleDomainManagerService.getPagedSimpleDomains(FIRST_PAGE, LARGE_SIZE_TO_INCLUDE_ALL);
        responseValue = response.getValue();
    }

    @When("^I request all BOs from the find by example maintenance service$")
    public void i_request_all_BOs_from_the_find_by_example_maintenance_service() throws Throwable {

        SortWrapper sortWrapper = new SortWrapper("name");
        FindByExampleCriteria<SimpleDomainBO> findAllCriteria = new FindByExampleCriteria<>(FIRST_PAGE,
                LARGE_SIZE_TO_INCLUDE_ALL, sortWrapper);
        response = simpleDomainMaintenanceService.findByExample(findAllCriteria);
        responseValue = response.getValue();
    }

    @When("^I request a paged service for a negative page index$")
    public void i_request_a_paged_service_for_a_negative_page_index() throws Throwable {
        response = simpleDomainManagerService.getPagedSimpleDomains(NEGATIVE_PAGE_INDEX, DEFAULT_PAGE_SIZE);
    }

    @When("^I request a paged service for a negative page size$")
    public void i_request_a_paged_service_for_a_negative_page_size() throws Throwable {
        response = simpleDomainManagerService.getPagedSimpleDomains(FIRST_PAGE, NEGATIVE_PAGE_SIZE);
    }

    @When("^I ask for page (\\d+) of (\\d+) items$")
    public void i_ask_for_page_of_items(int pageIndex, int pageSize) throws Throwable {
        response = simpleDomainManagerService.getPagedResponseWithoutSpringPage(numberOfItemsInList, pageIndex, pageSize);
    }

    @Then("^I get back the first (\\d+) items in the list as a PageWrapper object without depending on spring paging$")
    public void i_get_back_the_first_items_in_the_list_without_needing_to_depend_on_spring_paging(int numberOfItemsExpected)
            throws Throwable {
        assertEquals(numberOfItemsExpected, response.getValue().getContent().size());
        assertEquals(numberOfItemsInList, response.getValue().getTotalResults().intValue());
    }

    @Then("^the paged response indicates \"([^\"]*)\" as the number of simple domains requested$")
    public void the_paged_response_indicates_as_the_number_of_simple_domains_requested(Integer expectedSize)
            throws Throwable {
        assertEquals(expectedSize, responseValue.getItemsPerPage());
    }

    @Then("^the paged response indicates \"([^\"]*)\" as the number of simple domains in the page content$")
    public void the_paged_response_indicates_as_the_number_of_simple_domains_in_the_page_content(
            Integer expectedNumberOfElements) throws Throwable {
        assertEquals(expectedNumberOfElements, responseValue.getNumberOfElements());
    }

    @Then("^the paged response indicates \"([^\"]*)\" as the page number$")
    public void the_paged_response_indicates_as_the_page_number(Integer expectedPageNumber) throws Throwable {
        assertEquals(expectedPageNumber, responseValue.getStartPage());
    }

    @Then("^the paged response indicates \"([^\"]*)\" as the size which indicates the number of simple domains requested$")
    public void the_paged_response_indicates_as_the_size_which_indicates_the_number_of_simple_domains_requested(
            Integer expectedSize) throws Throwable {
        assertEquals(expectedSize, responseValue.getItemsPerPage());
    }

    @Then("^the paged response indicates \"([^\"]*)\" as the numberOfElements which indicates the number of simple domains in the page content$")
    public void the_paged_response_indicates_as_the_numberOfElements_which_indicates_the_number_of_simple_domains_in_the_page_content(
            Integer expectedNumberOfElements) throws Throwable {
        assertEquals(expectedNumberOfElements, responseValue.getNumberOfElements());
    }

    @Then("^the paged response indicates \"([^\"]*)\" as the total number of pages available$")
    public void the_paged_response_indicates_as_the_total_number_of_pages_available(Integer expectedTotalPages)
            throws Throwable {
        assertEquals(expectedTotalPages, responseValue.getTotalPages());
    }

    @Then("^the paged response indicates \"([^\"]*)\" as the total number of BOs available$")
    public void the_paged_response_indicates_as_the_total_number_of_BOs_available(Long expectedTotalElements)
            throws Throwable {
        assertEquals(expectedTotalElements, responseValue.getTotalResults());
    }

    @Then("^the paged response indicates \"([^\"]*)\" if it is the first page available$")
    public void the_paged_response_indicates_if_it_is_the_first_page_available(Boolean expectedIsFirst)
            throws Throwable {
        assertEquals(expectedIsFirst, responseValue.isFirst());
    }

    @Then("^the paged response indicates \"([^\"]*)\" if it is the last page available$")
    public void the_paged_response_indicates_if_it_is_the_last_page_available(Boolean expectedIsLast) throws Throwable {
        assertEquals(expectedIsLast, responseValue.isLast());
    }

    @Then("^I get an error message back indicating that the page index is required$")
    public void i_an_error_message_back_indicating_that_the_page_index_is_required() throws Throwable {
        TestUtils.assertErrorMessagesInResponse(response, 1);
    }

    @Then("^I get an error message back indicating that the page size is required$")
    public void i_an_error_message_back_indicating_that_the_page_size_is_required() throws Throwable {
        TestUtils.assertErrorMessagesInResponse(response, 1);
    }

    @Then("^I get an error message back indicating that the page index must be a positive number$")
    public void i_get_an_error_message_back_indicating_that_the_page_index_must_be_a_positive_number()
            throws Throwable {
        TestUtils.assertErrorMessagesInResponse(response, 1);
    }

    @Then("^I get an error message back indicating that the page size must be a positive number$")
    public void i_get_an_error_message_back_indicating_that_the_page_size_must_be_a_positive_number() throws Throwable {
        TestUtils.assertErrorMessagesInResponse(response, 1);
    }

    @Then("^I get back a response that contains a page$")
    public void i_get_back_a_response_that_contains_a_page() throws Throwable {
        assertNotNull(responseValue);
        assertNotNull(responseValue.getContent());
    }

    @Then("^within the page content the list of simple domain BOs$")
    public void within_the_page_content_the_list_of_simple_domain_BOs() throws Throwable {
        assertFalse(responseValue.getContent().isEmpty());
    }

    @Then("^I get back a paged response with all the appropriate page statistics$")
    public void i_get_back_a_paged_response_with_all_the_appropriate_page_statistics() throws Throwable {
        assertNotNull(responseValue.getContent());
        assertNotNull(responseValue.getStartPage());
        assertTrue(responseValue.getNumberOfElements() > 0);
        assertTrue(responseValue.getItemsPerPage() > 0);
        assertTrue(responseValue.getNumberOfElements() > 0);
    }

}
