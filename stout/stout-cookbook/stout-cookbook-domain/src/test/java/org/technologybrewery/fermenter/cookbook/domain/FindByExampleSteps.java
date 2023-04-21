package org.technologybrewery.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.technologybrewery.fermenter.cookbook.domain.enumeration.SimpleDomainEnumeration;
import org.technologybrewery.fermenter.stout.messages.Message;
import org.technologybrewery.fermenter.stout.messages.MessageManager;
import org.technologybrewery.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.technologybrewery.fermenter.stout.messages.Messages;
import org.technologybrewery.fermenter.stout.page.PageWrapper;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class FindByExampleSteps {

    private static final Integer PAGE_DEFAULT = 0;

    private static final Integer SIZE_DEFAULT = 50;

    private static final Sort SORT_DEFAULT = Sort.by(Sort.Direction.ASC, "name");

    private long numOfSimpleDomainsCreated;
    private SimpleDomainBO probe;
    private Sort sort;

    private PageWrapper<SimpleDomainBO> findByExampleResults;

    @After("@findByExample")
    public void cleanupMsgMgr() throws Exception {
        MessageManagerInitializationDelegate.cleanupMessageManager();
        SimpleDomainBO.deleteAll();
        sort = null;
        probe = null;
    }

    @Given("^the following simple domain objects:$")
    public void the_following_simple_domain_objects(List<SimpleDomainBOInput> simpleDomainInputs) throws Throwable {
        for (SimpleDomainBOInput simpleDomainInput : simpleDomainInputs) {
            SimpleDomainBO simpleDomain = new SimpleDomainBO();
            if (!StringUtils.isBlank(simpleDomainInput.name)) {
                simpleDomain.setName(simpleDomainInput.name);
            }
            if (!StringUtils.isBlank(simpleDomainInput.type)) {
                simpleDomain.setType(simpleDomainInput.type);
            }
            if (simpleDomainInput.theLong1 != null) {
                simpleDomain.setTheLong1(new Long(simpleDomainInput.theLong1));
            }
            if (!StringUtils.isBlank(simpleDomainInput.anEnumeratedValue)) {
                SimpleDomainEnumeration anEnumeratedValue = SimpleDomainEnumeration
                        .valueOfIgnoresCase(simpleDomainInput.anEnumeratedValue);
                simpleDomain.setAnEnumeratedValue(anEnumeratedValue);
            }
            if (!StringUtils.isBlank(simpleDomainInput.standardBoolean)) {
                simpleDomain.setStandardBoolean(new Boolean(simpleDomainInput.standardBoolean));
            }
            simpleDomain.save();
        }
    }

    @Given("^the probe has a name of \"([^\"]*)\" and type of \"([^\"]*)\" and long of \"([^\"]*)\" and enum of \"([^\"]*)\" and boolean of \"([^\"]*)\"$")
    public void the_probe_has_a_name_of_and_type_of_and_long_of_and_enum_of_and_boolean_of(String probeName,
            String probeType, String probeLong, String probeEnum, String probeBoolean) throws Throwable {
        probe = new SimpleDomainBO();
        if (!StringUtils.isBlank(probeName)) {
            probe.setName(probeName);
        }
        if (!StringUtils.isBlank(probeType)) {
            probe.setType(probeType);
        }
        if (!StringUtils.isBlank(probeLong)) {
            probe.setTheLong1(new Long(probeLong));
        }
        if (!StringUtils.isBlank(probeEnum)) {
            SimpleDomainEnumeration anEnumeratedValue = SimpleDomainEnumeration.valueOfIgnoresCase(probeEnum);
            probe.setAnEnumeratedValue(anEnumeratedValue);
        }
        if (!StringUtils.isBlank(probeBoolean)) {
            probe.setStandardBoolean(new Boolean(probeBoolean));
        }
    }

    @Given("^an empty probe$")
    public void an_empty_probe() throws Throwable {
        probe = new SimpleDomainBO();
    }

    @Given("^a null probe$")
    public void a_null_probe() throws Throwable {
        probe = null;
    }

    @Given("^a sort by \"([^\"]*)\" column$")
    public void a_sort_by_column(String sortColumn) throws Throwable {
        if (sort == null) {
            sort = Sort.by(Sort.Direction.ASC, sortColumn);
        } else {
            sort = sort.and(Sort.by(Sort.Direction.ASC, sortColumn));
        }
    }

    @Given("^simple domains exist in the system$")
    public void simple_domains_exist_in_the_system() throws Throwable {
        long numSimpleDomainsToCreate = RandomUtils.nextLong(2, 10);

        for (int i = 0; i < numSimpleDomainsToCreate; i++) {
            SimpleDomainBO simpleDomain = new SimpleDomainBO();
            simpleDomain.save();
        }
        numOfSimpleDomainsCreated = numSimpleDomainsToCreate;
    }

    @When("^I query for simple domains with no examples provided$")
    public void i_query_for_simple_domains_with_no_examples_provided() throws Throwable {
        probe = new SimpleDomainBO();
        findByExampleResults = SimpleDomainBO.findByExample(probe, false, PAGE_DEFAULT, SIZE_DEFAULT, SORT_DEFAULT);
    }

    @When("^I find by the example$")
    public void i_find_by_the_example() throws Throwable {
        Sort sortToUse = SORT_DEFAULT;
        if (sort != null) {
            sortToUse = sort;
        }
        findByExampleResults = SimpleDomainBO.findByExample(probe, false, PAGE_DEFAULT, SIZE_DEFAULT, sortToUse);
    }

    @When("^I find by the example with a null sort$")
    public void i_find_by_the_example_with_a_null_sort() throws Throwable {
        findByExampleResults = SimpleDomainBO.findByExample(probe, false, PAGE_DEFAULT, SIZE_DEFAULT, null);
    }

    @When("^I find by example using contains matching$")
    public void i_find_by_example_using_contains_matching() throws Throwable {
        findByExampleResults = SimpleDomainBO.findByExample(probe, true, PAGE_DEFAULT, SIZE_DEFAULT, SORT_DEFAULT);
    }

    @Then("^I should get the following results back \"([^\"]*)\" where the attributes contains the search ignoring case$")
    public void i_should_get_the_following_results_back_where_the_attributes_contains_the_search_ignoring_case(
            List<String> names) throws Throwable {
        assertEquals(names.size(), findByExampleResults.getContent().size());
        for (String name : names) {
            boolean foundInResults = false;
            for (SimpleDomainBO simpleDomain : findByExampleResults.getContent()) {
                if (simpleDomain.getName().equalsIgnoreCase(name)) {
                    foundInResults = true;
                    break;
                }
            }
            assertTrue("Couldn't find " + name + " in results", foundInResults);
        }
    }
    
    @And("^I should get \"([^\"]*)\" results because null probe defaults to all$")
    @Then("^I should get \"([^\"]*)\" results that match the probe inputs$")
    public void i_should_get_results(long countOfResults) throws Throwable {
        assertEquals(countOfResults, findByExampleResults.getTotalResults().intValue());
    }

    @Then("^I should get an error message saying \"([^\"]*)\"$")
    public void i_should_get_an_error_message_saying(String expectedMessage) throws Throwable {
        Messages messages = MessageManager.getMessages();
        Collection<Message> errorMessages = messages.getErrors();
        Message firstMessage = errorMessages.iterator().next();
        String foundMessage = firstMessage.getDisplayText();
        assertEquals(expectedMessage, foundMessage);
    }

    @Then("^all simple domains are returned$")
    public void all_simple_domains_are_returned() throws Throwable {
        long numOfSimpleDomainsReturned = findByExampleResults.getTotalResults();
        assertEquals("Expected all simple domains to be returned", numOfSimpleDomainsCreated,
                numOfSimpleDomainsReturned);
    }

    @Then("^I should get all simple domains sorted by name:$")
    public void i_should_get_all_simple_domains_sorted_by_name(List<SimpleDomainBO> expectedResults) throws Throwable {
        compareActualToExpectedQueryByExampleResults(expectedResults);
    }

    @Then("^I get all simple domains sorted by name then long:$")
    public void i_get_all_simple_domains_sorted_by_name_then_long(List<SimpleDomainBO> expectedResults)
            throws Throwable {
        compareActualToExpectedQueryByExampleResults(expectedResults);
    }

    private void compareActualToExpectedQueryByExampleResults(List<SimpleDomainBO> expectedResults) {
        for (int i = 0; i < expectedResults.size(); i++) {
            SimpleDomainBO expectedSimpleDomain = expectedResults.get(i);
            SimpleDomainBO actualSimpleDomain = findByExampleResults.getContent().get(i);
            assertEquals(expectedSimpleDomain.getTheLong1(), actualSimpleDomain.getTheLong1());
        }
    }

}

class SimpleDomainBOInput {
    String name;
    String type;
    Long theLong1;
    String anEnumeratedValue;
    String standardBoolean;
}
