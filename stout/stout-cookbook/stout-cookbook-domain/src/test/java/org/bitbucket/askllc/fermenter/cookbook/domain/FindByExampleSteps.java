package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.askllc.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.bitbucket.askllc.fermenter.cookbook.domain.enumeration.SimpleDomainEnumeration;
import org.bitbucket.fermenter.stout.messages.Message;
import org.bitbucket.fermenter.stout.messages.MessageManager;
import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.messages.Messages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@ContextConfiguration({ "classpath:stout-cookbook-domain-application-context.xml",
        "classpath:h2-spring-ds-context.xml" })
@Transactional
public class FindByExampleSteps {

    private static final Integer PAGE_DEFAULT = 0;

    private static final Integer SIZE_DEFAULT = 50;

    private static final Sort SORT_DEFAULT = new Sort(Sort.Direction.ASC, "name");

    private SimpleDomainBO probe;
    private Sort sort;

    private Page<SimpleDomainBO> findByExampleResults;

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
            sort = new Sort(Sort.Direction.ASC, sortColumn);
        } else {
            sort = sort.and(new Sort(Sort.Direction.ASC, sortColumn));
        }
    }

    @When("^I find by the example$")
    public void i_find_by_the_example() throws Throwable {
        Sort sortToUse = SORT_DEFAULT;
        if (sort != null) {
            sortToUse = sort;
        }
        findByExampleResults = SimpleDomainBO.findByExample(probe, PAGE_DEFAULT, SIZE_DEFAULT, sortToUse);
    }

    @When("^I find by the example with a null sort$")
    public void i_find_by_the_example_with_a_null_sort() throws Throwable {
        findByExampleResults = SimpleDomainBO.findByExample(probe, PAGE_DEFAULT, SIZE_DEFAULT, null);
    }

    @Then("^I should get \"([^\"]*)\" results$")
    public void i_should_get_results(long countOfResults) throws Throwable {
        assertEquals(countOfResults, findByExampleResults.getTotalElements());
    }

    @Then("^I should get the following results:$")
    public void i_should_get_the_following_results(List<SimpleDomainBO> expectedResults) throws Throwable {
        for (int i = 0; i < expectedResults.size(); i++) {
            SimpleDomainBO expectedSimpleDomain = expectedResults.get(i);
            SimpleDomainBO actualSimpleDomain = findByExampleResults.getContent().get(i);
            assertEquals(expectedSimpleDomain.getTheLong1(), actualSimpleDomain.getTheLong1());
        }
    }

    @Then("^I should get an error message saying \"([^\"]*)\"$")
    public void i_should_get_an_error_message_saying(String expectedMessage) throws Throwable {
        Messages messages = MessageManager.getMessages();
        Collection<Message> errorMessages = messages.getErrorMessages();
        Message firstMessage = errorMessages.iterator().next();
        assertEquals(expectedMessage, firstMessage.getProperties().iterator().next());
    }

}

class SimpleDomainBOInput {
    String name;
    String type;
    Long theLong1;
    String anEnumeratedValue;
    String standardBoolean;
}