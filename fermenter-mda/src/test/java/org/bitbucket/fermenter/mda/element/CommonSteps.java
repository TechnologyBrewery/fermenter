package org.bitbucket.fermenter.mda.element;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonSteps {
    
    private File enumerationDirectory = new File("target/temp-metadata", "enumerations");
    private File servicesDirectory = new File("target/temp-metadata", "services");
    private File entitiesDirectory = new File("target/temp-metadata", "entities");       

    /**
     * Clears out the message tracker (a singleton) before each scenario.
     */
    @Before
    public void clearMessageTracker() {
        MessageTracker messageTracker = MessageTracker.getInstance();
        messageTracker.clear();
    }
        
    
    @Before("@enumeration,@service,@entity")
    public void commonSetUp() {
        // make all the directories so we don't get a bunch of warnings:
        enumerationDirectory.mkdirs();
        servicesDirectory.mkdirs();
        entitiesDirectory.mkdirs();
    }
    
    /**
     * Clears out the message tracker (a singleton) before each scenario.
     */
    @After("@enumeration,@service,@entity") 
    public void commonCleanUp() throws Exception {
        FileUtils.deleteDirectory(enumerationDirectory);
        FileUtils.deleteDirectory(servicesDirectory);
        FileUtils.deleteDirectory(entitiesDirectory);
    }
    
}
