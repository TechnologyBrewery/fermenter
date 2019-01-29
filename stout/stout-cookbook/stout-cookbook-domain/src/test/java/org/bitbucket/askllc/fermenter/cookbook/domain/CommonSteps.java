package org.bitbucket.askllc.fermenter.cookbook.domain;

import org.bitbucket.fermenter.stout.messages.MessageManagerInitializationDelegate;
import org.bitbucket.fermenter.stout.mock.MockRequestScope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import cucumber.api.java.After;
import javax.inject.Inject;

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
