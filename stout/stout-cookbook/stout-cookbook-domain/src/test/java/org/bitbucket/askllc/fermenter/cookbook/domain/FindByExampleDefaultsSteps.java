package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationReferenceExampleBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.ValidationReferencedObjectBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.SimpleDomainMaintenanceService;
import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.ValidationReferenceExampleMaintenanceService;
import org.bitbucket.fermenter.stout.authn.AuthenticationTestUtils;
import org.bitbucket.fermenter.stout.service.ValueServiceResponse;
import org.bitbucket.fermenter.stout.test.MessageTestUtils;
import org.bitbucket.fermenter.stout.util.QueryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class FindByExampleDefaultsSteps {

    @Inject
    private ValidationReferenceExampleMaintenanceService validationReferenceExampleMaintenanceService;

    @Inject
    private SimpleDomainMaintenanceService simpleDomainMaintenanceService;

    private ValidationReferencedObjectBO referenceInCommon;

    private List<ValidationReferenceExampleBO> expectedExamples;

    private ValueServiceResponse<Collection<ValidationReferenceExampleBO>> response;

    private List<SimpleDomainBO> expectedSimpleDomains;

    private Collection<SimpleDomainBO> actualSimpleDomains;
    
    @Before("@findByExampleDefaults")
    public void setUp() {
        AuthenticationTestUtils.login("testUser");
    }
    
    @After("@findByExampleDefaults")
    public void tearDown() {
        
        if (CollectionUtils.isNotEmpty(expectedExamples)) {
            for (ValidationReferenceExampleBO example : expectedExamples) {
                example.delete();
            }
        }
        if (CollectionUtils.isNotEmpty(expectedSimpleDomains)) {
            for (SimpleDomainBO simpleDomain : expectedSimpleDomains) {
                simpleDomain.delete();
            }
        }
        if (referenceInCommon != null) {
            referenceInCommon.delete();
        }
        
        AuthenticationTestUtils.logout();
    }

    @Given("^entities that have something in common$")
    public void entities_that_have_something_in_common() throws Throwable {
        referenceInCommon = createReferenceInCommon();
        int random = RandomUtils.nextInt(2, 10);
        expectedExamples = createEntitiesWithCommonReference(random, referenceInCommon);
    }

    @Given("^a large number of entities that have something in common$")
    public void a_large_number_of_entities_that_have_something_in_common() throws Throwable {
        referenceInCommon = createReferenceInCommon();
        int queryLimit = QueryUtils.QUERY_LIMIT + 1;
        expectedExamples = createEntitiesWithCommonReference(queryLimit, referenceInCommon);
    }

    @Given("^some number of entities exist$")
    public void a_large_number_of_entities_exist() throws Throwable {
        int random = RandomUtils.nextInt(2, 20);
        expectedSimpleDomains = TestUtils.createAndPersistRandomSimpleDomains(random);
    }

    @When("^a search is done using an example of whatever is in common$")
    public void a_search_is_done_using_an_example_of_whatever_is_in_common() throws Throwable {
        ValidationReferencedObjectBO commonStringOnly = new ValidationReferencedObjectBO();
        commonStringOnly.setSomeDataField(referenceInCommon.getSomeDataField());
        ValidationReferenceExampleBO probe = new ValidationReferenceExampleBO();
        probe.setRequiredReference(commonStringOnly);
        response = validationReferenceExampleMaintenanceService.findByExampleContains(probe);
    }

    @When("^a search is done without specifying an example$")
    public void a_search_is_done_without_specifying_an_example() throws Throwable {
        ValueServiceResponse<Collection<SimpleDomainBO>> response = simpleDomainMaintenanceService
                .findByExampleContains(null);
        MessageTestUtils.assertNoErrorMessages(response);
        actualSimpleDomains = response.getValue();
    }

    @Then("^the results will be sorted by ascending primary key by default$")
    public void the_results_will_be_sorted_by_ascending_primary_key_by_default() throws Throwable {
        MessageTestUtils.assertNoErrorMessages(response);
        Collection<ValidationReferenceExampleBO> actualExamples = response.getValue();
        assertEquals("Number of results returned by find by example did not match expected", expectedExamples.size(),
                actualExamples.size());
        List<UUID> ids = actualExamples.stream().map(ValidationReferenceExampleBO::getKey).collect(Collectors.toList());
        for (int i = 0; i < ids.size() - 1; i++) {
            String first = ids.get(i).toString();
            String second = ids.get(i + 1).toString();
            assertTrue("UUID was not in ascending order", first.compareTo(second) < 0);
        }
    }

    @Then("^the search will start on page (\\d+) by default$")
    public void the_search_will_start_on_page_by_default(int pageNumber) throws Throwable {
        assertEquals("Default page was not 0", QueryUtils.DEFAULT_PAGE, pageNumber);
    }

    @Then("^the search will limit the number of search results by default$")
    public void the_search_will_limit_the_number_of_search_results_by_default() throws Throwable {
        assertEquals(QueryUtils.QUERY_LIMIT, response.getValue().size());
    }

    @Then("^the search will do a find all by default$")
    public void the_search_will_do_a_find_all_by_default() throws Throwable {
        assertEquals("Find by example with no probe did not return all as expected", expectedSimpleDomains.size(),
                actualSimpleDomains.size());
    }

    private ValidationReferencedObjectBO createReferenceInCommon() {
        ValidationReferencedObjectBO reference = new ValidationReferencedObjectBO();
        reference.setSomeDataField(RandomStringUtils.randomAlphanumeric(10));
        return reference.save();
    }

    private List<ValidationReferenceExampleBO> createEntitiesWithCommonReference(int numEntitiesToCreate,
            ValidationReferencedObjectBO referenceInCommon) {
        List<ValidationReferenceExampleBO> examples = new ArrayList<>(numEntitiesToCreate);
        for (int iter = 0; iter < numEntitiesToCreate; iter++) {
            ValidationReferenceExampleBO example = new ValidationReferenceExampleBO();
            example.setSomeDataField(RandomStringUtils.randomAlphanumeric(20));
            example.setRequiredReference(referenceInCommon);
            examples.add(example.save());
        }
        return examples;
    }

}
