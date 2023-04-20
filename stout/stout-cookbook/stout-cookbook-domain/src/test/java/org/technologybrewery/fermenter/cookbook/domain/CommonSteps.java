package org.technologybrewery.fermenter.cookbook.domain;

import javax.inject.Inject;

import org.technologybrewery.fermenter.stout.mock.MockRequestScope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cucumber.api.java.After;

@ContextConfiguration({ "classpath:application-test-context.xml", "classpath:h2-spring-ds-context.xml" })
@Transactional
public class CommonSteps {
    
    @Inject
    private MockRequestScope mockRequestScope;
    
    /**
     * Common After method run after every scenario to clear out the message
     * manager.
     * 
     * @throws Exception
     */
    @After
    public void clearOutMessageManager() throws Exception {
        mockRequestScope.cleanMessageManager();
    }

}
