package org.bitbucket.fermenter.mda.metamodel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.EnumElement;
import org.bitbucket.fermenter.mda.metamodel.element.EnumerationElement;
import org.bitbucket.fermenter.mda.metamodel.element.OperationElement;
import org.bitbucket.fermenter.mda.metamodel.element.ParameterElement;
import org.bitbucket.fermenter.mda.metamodel.element.ReturnElement;
import org.bitbucket.fermenter.mda.metamodel.element.ServiceElement;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TEMPORARY class that encapsulates converting legacy metadata files into json.
 */
public class LegacyMetadataConverter {

    private static final Log log = LogFactory.getLog(LegacyMetadataConverter.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static MetadataRepository legacyMetadataRepository = ModelInstanceRepositoryManager
            .getMetadataRepostory(MetadataRepository.class);

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
            convertLegacyServices(applicationName, basePackage, sourceMain);

        } catch (Exception e) {
            throw new GenerationException("Could not convert legacy metadata!", e);
        }

        log.info("Converted " + convertedFileCount + " file(s)");
        log.info("########################################################################");
        log.info("");
    }

    private void convertLegacyEnumerations(String applicationName, String basePackage, File sourceMain)
            throws IOException {
        // convert legacy metadata to new metadata:
        Map<String, org.bitbucket.fermenter.mda.metadata.element.Enumeration> legacyEnumerations = legacyMetadataRepository
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

    private void convertLegacyServices(String applicationName, String basePackage, File sourceMain) throws IOException {
        // convert legacy metadata to new metadata:
        org.bitbucket.fermenter.mda.metadata.MetadataRepository legacyMetadataRepo = ModelInstanceRepositoryManager
                .getMetadataRepostory(org.bitbucket.fermenter.mda.metadata.MetadataRepository.class);
        Map<String, org.bitbucket.fermenter.mda.metadata.element.Service> legacyServices = legacyMetadataRepo
                .getAllServices(applicationName);
        for (org.bitbucket.fermenter.mda.metadata.element.Service legacyService : legacyServices.values()) {
            ServiceElement newService = new ServiceElement();
            newService.setName(legacyService.getName());
            newService.setPackage(basePackage);
            newService.setDocumentation(legacyService.getDocumentation());

            for (org.bitbucket.fermenter.mda.metadata.element.Operation legacyOperation : legacyService.getOperations()
                    .values()) {
                OperationElement newOperation = convertLegacyOperation(legacyOperation, basePackage);

                newService.getOperations().add(newOperation);
            }

            File newServiceFile = new File(sourceMain, "resources/services/" + newService.getName() + ".json");
            log.info("\tConverting service '" + newService.getName() + "' to location "
                    + newServiceFile.getAbsolutePath());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(newServiceFile, newService);
            convertedFileCount++;

        }

    }

    private OperationElement convertLegacyOperation(
            org.bitbucket.fermenter.mda.metadata.element.Operation legacyOperation, String basePackage) {
        OperationElement newOperation = new OperationElement();
        newOperation.setName(legacyOperation.getName());
        newOperation.setDocumentation(legacyOperation.getDocumentation());
        if (getUndefaultedValue(legacyOperation, "transactionAttribute") != null) {
            newOperation.setTransactionAttribute(legacyOperation.getTransactionAttribute());
        }
        if (getUndefaultedValue(legacyOperation, "compressWithGzip") != null
                && legacyOperation.isCompressedWithGzip()) {
            newOperation.setCompressedWithGZip(true);
        }

        ReturnElement returnElement = convertLegacyReturn(legacyOperation, basePackage);
        newOperation.setReturn(returnElement);

        for (org.bitbucket.fermenter.mda.metadata.element.Parameter legacyParameter : legacyOperation.getParameters()) {
            ParameterElement newParameter = convertLegacyParameters(legacyParameter, basePackage);

            newOperation.getParameters().add(newParameter);
        }
        return newOperation;
    }

    private ReturnElement convertLegacyReturn(org.bitbucket.fermenter.mda.metadata.element.Operation legacyOperation,
            String basePackage) {
        ReturnElement returnElement = new ReturnElement();
        String returnType = null;
        if (getUndefaultedValue(legacyOperation, "responseEncoding") != null) {
            returnElement.setResponseEncoding(legacyOperation.getResponseEncoding());
        }
        if (getUndefaultedValue(legacyOperation, "returnManyType") != null
                && legacyOperation.getReturnManyType() != null) {
            returnType = legacyOperation.getReturnManyType();
            returnElement.setMany(true);

        } else {
            returnType = legacyOperation.getReturnType();

        }
        
        returnElement.setType(returnType);
        String returnPackage = findPackage(returnType, basePackage);
        if (returnPackage != null) {
            returnElement.setPackage(returnPackage);
        }
        
        return returnElement;
    }

    private ParameterElement convertLegacyParameters(
            org.bitbucket.fermenter.mda.metadata.element.Parameter legacyParameter, String basePackage) {
        ParameterElement newParameter = new ParameterElement();
        newParameter.setName(legacyParameter.getName());

        String parameterType = legacyParameter.getType();
        String parameterPackage = findPackage(parameterType, basePackage);
        if (parameterPackage != null) {
            newParameter.setPackage(parameterPackage);
        }

        newParameter.setType(parameterType);

        if (getUndefaultedValue(legacyParameter, "many") != null && legacyParameter.isMany()) {
            newParameter.setMany(true);
        }
        newParameter.setDocumentation(legacyParameter.getDocumentation());
        return newParameter;
    }

    private Object getUndefaultedValue(Object instance, String variable) {
        Object value = null;

        try {
            Field field = instance.getClass().getDeclaredField(variable);
            field.setAccessible(true);
            value = field.get(instance);

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new GenerationException("Coud not determine current value of field '" + variable + "'!", e);
        }

        return value;
    }

    /**
     * Determine the package name so these are set automatically by the conversion. If an entity, set the package to the
     * defined package or the default package if no specified package has been defined. If null, then no package is
     * required. Cannot support enumerations because they aren't yet loaded.
     * 
     * @param type
     *            the type for which to infer the package
     * @param basePackage
     *            the base package to use for defaulting
     * @return the package name or null if not an entity
     */
    private String findPackage(String type, String basePackage) {
        String packageName = null;

        Map<String, Entity> allEntitis = legacyMetadataRepository.getAllEntities();
        Entity entity = (allEntitis != null) ? legacyMetadataRepository.getAllEntities().get(type) : null;
        if (entity != null) {
            packageName = entity.getNamespace();

            if (packageName == null) {
                packageName = basePackage;
            }
        }

        return packageName;
    }

}
