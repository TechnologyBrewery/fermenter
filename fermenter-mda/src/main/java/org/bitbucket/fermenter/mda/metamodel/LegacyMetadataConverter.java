package org.bitbucket.fermenter.mda.metamodel;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.element.EnumElement;
import org.bitbucket.fermenter.mda.metamodel.element.EnumerationElement;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TEMPORARY class that encapsulates converting legacy metadata files into json.
 */
public class LegacyMetadataConverter {

    private static final Log log = LogFactory.getLog(LegacyMetadataConverter.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    private int convertedFileCount;

    /**
     * Performs all conversion activities.
     * 
     * @param applicationName
     *            the legacy applicationName
     * @param basePackage
     *            the base package name
     * @param sourceMain
     *            location of metadata
     */
    public void convert(String applicationName, String basePackage, File sourceMain) {
        log.info("");
        log.info("########################################################################");
        log.info("Running model conversion - remove old files and leverage new models or risk losing changes!");

        try {
            convertLegacyEnumerations(applicationName, basePackage, sourceMain);

        } catch (Exception e) {
            throw new GenerationException("Could not convert legacy metadata!", e);
        }

        log.info("Coverted " + convertedFileCount + " file(s)");
        log.info("########################################################################");
        log.info("");
    }

    private void convertLegacyEnumerations(String applicationName, String basePackage, File sourceMain)
            throws IOException {
        // convert legacy metadata to new metadata:
        org.bitbucket.fermenter.mda.metadata.MetadataRepository legacyMetadataRepo = ModelInstanceRepositoryManager
                .getMetadataRepostory(org.bitbucket.fermenter.mda.metadata.MetadataRepository.class);
        Map<String, org.bitbucket.fermenter.mda.metadata.element.Enumeration> legacyEnumerations = legacyMetadataRepo
                .getAllEnumerations(applicationName);
        for (org.bitbucket.fermenter.mda.metadata.element.Enumeration legacyEnumeration : legacyEnumerations.values()) {
            EnumerationElement newEnumeration = new EnumerationElement();
            newEnumeration.setName(legacyEnumeration.getName());
            newEnumeration.setPackage(basePackage);

            for (Object untypedEnum : legacyEnumeration.getEnumList()) {
                org.bitbucket.fermenter.mda.metadata.element.Enum legacyEnum = (org.bitbucket.fermenter.mda.metadata.element.Enum) untypedEnum;
                EnumElement newEnum = new EnumElement();
                newEnum.setName(legacyEnum.getName());
                newEnumeration.addEnums(newEnum);
            }

            File newEnumerationFile = new File(sourceMain,
                    "resources/enumerations/" + newEnumeration.getName() + ".json");
            log.info("\tConverting enumeration '" + newEnumeration.getName() + "' to location "
                    + newEnumerationFile.getAbsolutePath());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(newEnumerationFile, newEnumeration);
            convertedFileCount++;

        }

    }

}
