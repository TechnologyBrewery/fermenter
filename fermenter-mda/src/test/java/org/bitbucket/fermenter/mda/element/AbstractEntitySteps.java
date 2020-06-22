package org.bitbucket.fermenter.mda.element;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceUrl;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;
import org.bitbucket.fermenter.mda.metamodel.element.EntityElement;
import org.bitbucket.fermenter.mda.util.MessageTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractEntitySteps {

    private static final Logger logger = LoggerFactory.getLogger(EntitySteps.class);

    protected ObjectMapper objectMapper = new ObjectMapper();
    protected File entitiesDirectory = new File("target/temp-metadata", "entities");    
    protected MessageTracker messageTracker = MessageTracker.getInstance();

    protected String currentBasePackage;
    protected File entityFile;  
    protected GenerationException encounteredException;
    protected DefaultModelInstanceRepository metadataRepo;
    
    protected void cleanUp() throws IOException {
        messageTracker.clear();
        currentBasePackage = null;
        
        if (entitiesDirectory.exists()) {
            FileUtils.forceDelete(entitiesDirectory);
        }
    }
 
    protected EntityElement createEntityElement(EntityElement entity)
            throws IOException, JsonGenerationException, JsonMappingException, JsonProcessingException {
        
        entitiesDirectory.mkdirs();
        entityFile = new File(entitiesDirectory, entity.getName() + ".json");
        objectMapper.writeValue(entityFile, entity);
        logger.debug(objectMapper.writeValueAsString(entity));
        assertTrue("Entities not written to file!", entityFile.exists());

        currentBasePackage = entity.getPackage();

        return entity;
    }

    protected EntityElement createBaseEntity(String name, String packageName, String documentation) {
        EntityElement entity = new EntityElement();
        if (StringUtils.isNotBlank(name)) {
            entity.setName(name);
        }
        entity.setPackage(packageName);
        entity.setDocumentation(documentation);
        return entity;
    }

    protected void readEntities() {
        encounteredException = null;
    
    	try {
    		ModelRepositoryConfiguration config = new ModelRepositoryConfiguration();
    		config.setArtifactId("fermenter-mda");
    		config.setBasePackage(currentBasePackage);
    		Map<String, ModelInstanceUrl> metadataUrlMap = config.getMetamodelInstanceLocations();
    		metadataUrlMap.put("fermenter-mda",
    				new ModelInstanceUrl("fermenter-mda", entitiesDirectory.getParentFile().toURI().toString()));
    
    		metadataRepo = new DefaultModelInstanceRepository(config);
    		ModelInstanceRepositoryManager.setRepository(metadataRepo);
    		metadataRepo.load();
    		metadataRepo.validate();
    
    	} catch (GenerationException e) {
    		encounteredException = e;
    	}
    }

}
