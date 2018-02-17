package org.bitbucket.fermenter.mda.element;

import org.bitbucket.fermenter.mda.util.MessageTracker;

import cucumber.api.java.Before;

public class CommonSteps {

    /**
     * Clears out the message tracker (a singleton) before each scenario.
     */
    @Before
    public void clearMessageTracker() {
        MessageTracker messageTracker = MessageTracker.getInstance();
        messageTracker.clear();
    }
    
}
