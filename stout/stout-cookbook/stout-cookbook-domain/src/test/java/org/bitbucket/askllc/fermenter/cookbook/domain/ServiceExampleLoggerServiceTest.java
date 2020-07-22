package org.bitbucket.askllc.fermenter.cookbook.domain;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.bitbucket.askllc.fermenter.cookbook.domain.service.rest.ServiceExampleLoggerService;
import org.bitbucket.fermenter.stout.authn.AuthenticationTestUtils;
import org.bitbucket.fermenter.stout.messages.AbstractMsgMgrAwareTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:stout-cookbook-domain-application-context.xml", "classpath:h2-spring-ds-context.xml" })
// enables each test method to transparently participate in any existing transactional context loaded via
// @ContextConfiguration application contexts and automatically rollbacks any transactions after each test method
@Transactional
// needed to allow Spring to instantiate request-scoped {@link Messages} that will be injected by generated code at the
// beginning of each business service operation into the {@link MessageManager}.
@WebAppConfiguration
/**
 * Tests generated code and developer integration points for building functionality not natively supported by Fermenter.
 * Additionally, this test classes demonstrates how spring-test may be leveraged to perform unit testing against an
 * embedded database.
 */
public class ServiceExampleLoggerServiceTest extends AbstractMsgMgrAwareTestSupport {

	@Inject
	private ServiceExampleLoggerService service;
	
    @Before
    public void setUp() {
        AuthenticationTestUtils.login("testUser");
    }
    
    @After
    public void tearDown() {
        AuthenticationTestUtils.logout();
    }
    
	@Test
	public void testServiceExampleEntity() throws Exception {
		assertNotNull("Service not propertly injected!", service);
		service.logServiceEntityExample();
	}

}
