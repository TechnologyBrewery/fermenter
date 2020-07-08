package org.bitbucket.fermenter.mda.element;

import java.io.File;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.io.FileUtils;
import org.bitbucket.fermenter.mda.metamodel.MetamodelConfig;
import org.bitbucket.fermenter.mda.util.MessageTracker;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class CommonSteps {
    private static final MetamodelConfig config = KrauseningConfigFactory.create(MetamodelConfig.class);
    
    private File enumerationDirectory = new File("target/temp-metadata", config.getEnumerationsRelativePath());
    private File servicesDirectory = new File("target/temp-metadata", config.getServicesRelativePath());
    private File entitiesDirectory = new File("target/temp-metadata", config.getEntitiesRelativePath());
    private File typeDictionaryDirectory = new File("target/temp-metadata", config.getDictionaryTypesRelativePath());
    private File messageGroupDirectory = new File("target/temp-metadata", config.getMessageGroupsRelativePath());    

    /**
     * Clears out the message tracker (a singleton) before each scenario.
     */
    @Before
    public void clearMessageTracker() {
        MessageTracker messageTracker = MessageTracker.getInstance();
        messageTracker.clear();
    }
        
    
    @Before("@enumeration,@service,@entity,@typeDictionary,@messageGroup")
    public void commonSetUp() {
        // make all the directories so we don't get a bunch of warnings:
        enumerationDirectory.mkdirs();
        servicesDirectory.mkdirs();
        entitiesDirectory.mkdirs();
        typeDictionaryDirectory.mkdirs();
        messageGroupDirectory.mkdirs();
    }
    
    /**
     * Clears out the message tracker (a singleton) before each scenario.
     */
    @After("@enumeration,@service,@entity,@typeDictionary,@messageGroup") 
    public void commonCleanUp() throws Exception {
        FileUtils.deleteDirectory(enumerationDirectory);
        FileUtils.deleteDirectory(servicesDirectory);
        FileUtils.deleteDirectory(entitiesDirectory);
        FileUtils.deleteDirectory(typeDictionaryDirectory);
        FileUtils.deleteDirectory(messageGroupDirectory);
    }
    
}
