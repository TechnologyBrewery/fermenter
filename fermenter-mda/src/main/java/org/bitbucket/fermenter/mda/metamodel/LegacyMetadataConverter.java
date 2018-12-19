package org.bitbucket.fermenter.mda.metamodel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.PackageManager;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.EntityElement;
import org.bitbucket.fermenter.mda.metamodel.element.EnumElement;
import org.bitbucket.fermenter.mda.metamodel.element.EnumerationElement;
import org.bitbucket.fermenter.mda.metamodel.element.FieldElement;
import org.bitbucket.fermenter.mda.metamodel.element.OperationElement;
import org.bitbucket.fermenter.mda.metamodel.element.ParameterElement;
import org.bitbucket.fermenter.mda.metamodel.element.ParentElement;
import org.bitbucket.fermenter.mda.metamodel.element.ReferenceElement;
import org.bitbucket.fermenter.mda.metamodel.element.Relation.Multiplicity;
import org.bitbucket.fermenter.mda.metamodel.element.RelationElement;
import org.bitbucket.fermenter.mda.metamodel.element.ReturnElement;
import org.bitbucket.fermenter.mda.metamodel.element.ServiceElement;
import org.bitbucket.fermenter.mda.metamodel.element.Validation;
import org.bitbucket.fermenter.mda.metamodel.element.ValidationElement;

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
	 * @param applicationName the legacy applicationName
	 * @param basePackage     the base package name
	 * @param sourceMain      location of metadata
	 */
	public void convert(String applicationName, String basePackage, File sourceMain) {
		log.info("");
		log.info("########################################################################");
		log.info("Running model conversion - remove old files and leverage new models or risk losing changes!");

		try {
			convertLegacyEnumerations(applicationName, basePackage, sourceMain);
			convertLegacyServices(applicationName, basePackage, sourceMain);
			// TODO: uncomment when we are actually ready  for migration (post FER-116):
			//convertLegacyEntities(applicationName, basePackage, sourceMain);

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

	private void convertLegacyEntities(String applicationName, String basePackage, File sourceMain) throws IOException {
		// convert legacy metadata to new metadata:
		org.bitbucket.fermenter.mda.metadata.MetadataRepository legacyMetadataRepo = ModelInstanceRepositoryManager
				.getMetadataRepostory(org.bitbucket.fermenter.mda.metadata.MetadataRepository.class);
		Map<String, org.bitbucket.fermenter.mda.metadata.element.Entity> legacyEntities = legacyMetadataRepo
				.getAllEntities(applicationName);
		for (org.bitbucket.fermenter.mda.metadata.element.Entity legacyEntity : legacyEntities.values()) {
			EntityElement newEntity = new EntityElement();
			newEntity.setName(legacyEntity.getName());
			newEntity.setPackage(basePackage);
			newEntity.setDocumentation(legacyEntity.getDocumentation());
			newEntity.setTable(legacyEntity.getTable());

			if (getUndefaultedValue(legacyEntity, "lockStrategy") != null) {
				newEntity.setLockStrategy(legacyEntity.getLockStrategy());
			}

			if (legacyEntity.isTransient()) {
				newEntity.setTransient(true);
			}

			if (legacyEntity.getParent() != null) {
				ParentElement newParent = convertLegacyParent(legacyEntity.getParent(), basePackage);
				newEntity.setParent(newParent);
			}

			Collection<org.bitbucket.fermenter.mda.metadata.element.Field> idFields = legacyEntity.getIdFields()
					.values();
			if (CollectionUtils.isNotEmpty(idFields)) {
				org.bitbucket.fermenter.mda.metadata.element.Field legacyIdentifier = idFields.iterator().next();
				FieldElement newIdentifier = convertLegacyField(legacyIdentifier, basePackage);

				newEntity.setIdentifier(newIdentifier);

			}

			for (org.bitbucket.fermenter.mda.metadata.element.Field field : legacyEntity.getFields().values()) {
				FieldElement newField = convertLegacyField(field, basePackage);
				newEntity.addField(newField);
			}

			for (org.bitbucket.fermenter.mda.metadata.element.Reference reference : legacyEntity.getReferences().values()) {
				ReferenceElement newReference = convertLegacyReference(reference, basePackage);
				newEntity.addReference(newReference);
			}
			
			for (org.bitbucket.fermenter.mda.metadata.element.Relation relation : legacyEntity.getRelations().values()) {
				RelationElement newRelation = convertLegacyRelation(relation, basePackage);
				newEntity.addRelation(newRelation);
			}

			File newEntityFile = new File(sourceMain, "resources/entities/" + newEntity.getName() + ".json");
			log.info(
					"\tConverting entity '" + newEntity.getName() + "' to location " + newEntityFile.getAbsolutePath());
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(newEntityFile, newEntity);
			convertedFileCount++;

		}

	}

	private ParentElement convertLegacyParent(org.bitbucket.fermenter.mda.metadata.element.Parent legacyParent,
			String basePackage) {

		ParentElement newParent = new ParentElement();

		String type = legacyParent.getType();
		newParent.setType(type);
		String packageName = findPackage(type, basePackage);
		if (StringUtils.isNotBlank(packageName)) {
			newParent.setPackage(packageName);

		} else {
			newParent.setPackage(basePackage);

		}

		if (legacyParent.getInheritanceStrategy() != null) {
			String legacyStrategy = legacyParent.getInheritanceStrategy().toString();
			// MAPPED_SUBCLASS -> MAPPED-SUBCLASS:
			newParent.setInheritanceStrategy(legacyStrategy.replace("_", "-"));
		}

		return newParent;
	}

	private FieldElement convertLegacyField(org.bitbucket.fermenter.mda.metadata.element.Field legacyField,
			String basePackage) {

		FieldElement newField = new FieldElement();
		newField.setName(legacyField.getName());

		covertType(newField, legacyField.getType(), basePackage);
		
		ValidationElement newValidation = new ValidationElement();
		newValidation.setMaxValue(legacyField.getMaxValue());
		newValidation.setMinValue(legacyField.getMinValue());

		if (legacyField.hasMaxLength()) {
			newValidation.setMaxLength(Integer.valueOf(legacyField.getMaxLength()));
		}

		if (legacyField.hasMinLength()) {
			newValidation.setMinLength(Integer.valueOf(legacyField.getMinLength()));
		}

		if (legacyField.hasScale()) {
			newValidation.setScale(Integer.valueOf(legacyField.getScale()));
		}

		newValidation.setFormat(legacyField.getFormat());

		if (newValidation.hasValue()) {
			newField.setType(newValidation);
		}

		newField.setDocumentation(legacyField.getDocumentation());
		newField.setColumn(legacyField.getColumn());

		if (getUndefaultedValue(legacyField, "required") != null) {
			newField.setRequired(legacyField.isRequired());
		}

		if (StringUtils.isNotBlank(legacyField.getGenerator())) {
			newField.setGenerator(legacyField.getGenerator());
		}

		return newField;
	}

	private ReferenceElement convertLegacyReference(
			org.bitbucket.fermenter.mda.metadata.element.Reference legacyReference, String basePackage) {

		ReferenceElement newReference = new ReferenceElement();

		newReference.setName(legacyReference.getName());
		
		String type = legacyReference.getType();		
		String packageName = findPackage(type, basePackage);
		
		newReference.setType(type);
		newReference.setPackage(packageName);

		newReference.setDocumentation(legacyReference.getDocumentation());

		if (getUndefaultedValue(legacyReference, "required") != null) {
			newReference.setRequired(legacyReference.isRequired());
		}

		List<org.bitbucket.fermenter.mda.metadata.element.Field> legacyForeignKeys = legacyReference
				.getForeignKeyFields();
		org.bitbucket.fermenter.mda.metadata.element.Field legacyForeignKey = legacyForeignKeys.iterator().next();
		
		newReference.setLocalColumn(legacyForeignKey.getColumn());

		return newReference;
	}
	
	private RelationElement convertLegacyRelation(
			org.bitbucket.fermenter.mda.metadata.element.Relation legacyRelation, String basePackage) {

		RelationElement newRelation = new RelationElement();
		
		String type = legacyRelation.getType();		
		String packageName = findPackage(type, basePackage);
		
		newRelation.setType(type);
		newRelation.setPackage(packageName);

		newRelation.setDocumentation(legacyRelation.getDocumentation());

		// currently, you can't do a local key in relations, so not migrating that part
		
		if (StringUtils.isNotBlank(legacyRelation.getMultiplicity())) {
			newRelation.setMultiplicity(legacyRelation.getMultiplicity());
		}
		
		if (StringUtils.isNotBlank(legacyRelation.getFetchMode())) {
			newRelation.setFetchMode(legacyRelation.getFetchMode());
		}		

		return newRelation;
	}	

	private void covertType(FieldElement newField, String typeName, String basePackage) {
		newField.setType(typeName);
		if (basePackage != null && !Validation.BaseType.isSimpleType(typeName)) {
			newField.setPackage(basePackage);
		}

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
	 * Determine the package name so these are set automatically by the conversion.
	 * If an entity, set the package to the defined package or the default package
	 * if no specified package has been defined. If null, then no package is
	 * required. Cannot support enumerations because they aren't yet loaded.
	 * 
	 * @param type        the type for which to infer the package
	 * @param basePackage the base package to use for defaulting
	 * @return the package name or null if not an entity
	 */
	private String findPackage(String type, String basePackage) {
		String packageName = null;

		Map<String, Entity> allEntitis = legacyMetadataRepository.getAllEntities();
		Entity entity = (allEntitis != null) ? legacyMetadataRepository.getAllEntities().get(type) : null;
		if (entity != null) {
			packageName = entity.getNamespace();

			if (StringUtils.isBlank(packageName)) {
				packageName = basePackage;
			}
		}

		return packageName;
	}	

}
