package org.bitbucket.fermenter.mda.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceUrl;
import org.bitbucket.fermenter.mda.metamodel.ModelRepositoryConfiguration;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Entity.LockStrategy;
import org.bitbucket.fermenter.mda.metamodel.element.EntityElement;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.FieldElement;
import org.bitbucket.fermenter.mda.metamodel.element.Parent;
import org.bitbucket.fermenter.mda.metamodel.element.Parent.InheritanceStrategy;
import org.bitbucket.fermenter.mda.metamodel.element.ParentElement;
import org.bitbucket.fermenter.mda.metamodel.element.Reference;
import org.bitbucket.fermenter.mda.metamodel.element.ReferenceElement;
import org.bitbucket.fermenter.mda.metamodel.element.Relation;
import org.bitbucket.fermenter.mda.metamodel.element.Relation.FetchMode;
import org.bitbucket.fermenter.mda.metamodel.element.Relation.Multiplicity;
import org.bitbucket.fermenter.mda.metamodel.element.RelationElement;
import org.bitbucket.fermenter.mda.metamodel.element.Validation;
import org.bitbucket.fermenter.mda.metamodel.element.ValidationElement;
import org.bitbucket.fermenter.mda.util.MessageTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class EntitySteps {

	private static final Logger logger = LoggerFactory.getLogger(EntitySteps.class);

	private ObjectMapper objectMapper = new ObjectMapper();
	private MessageTracker messageTracker = MessageTracker.getInstance();
	private File entitiesDirectory = new File("target/temp-metadata", "entities");

	private String currentBasePackage;

	private File entityFile;
	private Entity loadedEntity;
	protected GenerationException encounteredException;
	protected DefaultModelInstanceRepository metadataRepo;

	@After("@entity")
	public void cleanUp() {
		loadedEntity = null;

		messageTracker.clear();

		currentBasePackage = null;

	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\"$")
	public void an_entity_named_in(String name, String packageName) throws Throwable {
		EntityElement entity = createBaseEntity(name, packageName, null);
		createEntityElement(entity);
	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\" with documentation \"([^\"]*)\"$")
	public void an_entity_named_in(String name, String packageName, String documentation) throws Throwable {
		EntityElement entity = createBaseEntity(name, packageName, documentation);
		createEntityElement(entity);
	}

	@Given("^an entity name \"([^\"]*)\" in \"([^\"]*)\" with parent \"([^\"]*)\" and inheritance strategy \"([^\"]*)\"$")
	public void an_entity_name_in_with_parent_and_inheritance_strategy(String name, String packageName, String parent,
			String inheritanceStrategy) throws Throwable {

		EntityElement entity = createBaseEntity(name, packageName, null);
		ParentElement parentElement = new ParentElement();
		parentElement.setPackage(packageName);
		parentElement.setType(parent);
		if (StringUtils.isNotBlank(inheritanceStrategy)) {
			parentElement.setInheritanceStrategy(inheritanceStrategy);
		}
		entity.setParent(parentElement);
		createEntityElement(entity);
	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\" with table \"([^\"]*)\"$")
	public void an_entity_named_in_with_table(String name, String packageName, String table) throws Throwable {
		EntityElement entity = createBaseEntity(name, packageName, null);
		entity.setTable(table);
		createEntityElement(entity);
	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\" with transient flag \"([^\"]*)\"$")
	public void an_entity_named_in_with_transient_flag(String name, String packageName, Boolean transientEntity)
			throws Throwable {
		EntityElement entity = createBaseEntity(name, packageName, null);
		entity.setTransient(transientEntity);
		createEntityElement(entity);
	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\" with an indentifier:$")
	public void an_entity_named_in_with_an_indentifier(String name, String packageName, List<FieldInput> identifier)
			throws Throwable {

		FieldInput id = identifier.iterator().next();
		createEntityWithIdentifier(name, packageName, id);
	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\" with a field:$")
	public void an_entity_named_in_with_a_field(String name, String packageName, List<FieldInput> fields)
			throws Throwable {
		FieldInput field = fields.iterator().next();

		EntityElement entity = createBaseEntity(name, packageName, null);
		FieldElement entityField = createField(field);
		entity.addField(entityField);

		// add defaults:
		FieldElement defaultId = createDefaultIdentifier();
		entity.setIdentifier(defaultId);

		createEntityElement(entity);
	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\" with a reference:$")
	public void an_entity_named_in_with_a_reference(String name, String packageName, List<ReferenceInput> references)
			throws Throwable {
		ReferenceInput reference = references.iterator().next();

		EntityElement entity = createBaseEntity(name, packageName, null);

		ReferenceElement entityReference = new ReferenceElement();
		entityReference.setName(reference.referenceName);
		entityReference.setPackage(reference.referencePackage);
		entityReference.setType(reference.type);
		entityReference.setLocalColumn(reference.localColumn);
		entityReference.setDocumentation(reference.documentation);
		entityReference.setRequired(reference.required);

		entity.addReference(entityReference);

		createEntityElement(entity);
	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\" with a relation:$")
	public void an_entity_named_in_with_a_relation(String name, String packageName, List<RelationInput> relations)
			throws Throwable {
		RelationInput relation = relations.iterator().next();

		createEntityWithRelation(name, packageName, relation);
	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\" with a valid relation that does not specify multiplicity$")
	public void an_entity_named_in_with_a_valid_relation_that_does_not_specify_multiplicity(String name,
			String packageName) throws Throwable {
		String referencedEntityName = StringUtils.capitalize(RandomStringUtils.randomAlphabetic(10));
		String referencedEntityPackage = "foo.default.multiplicity";
		createEntityWithDefaultIdentifier(referencedEntityName, referencedEntityPackage);

		RelationInput relationInput = new RelationInput();
		relationInput.type = referencedEntityName;
		relationInput.relationPackage = referencedEntityPackage;
		relationInput.documentation = RandomStringUtils.randomAlphanumeric(20);
		relationInput.fetchMode = FetchMode.LAZY.toString();
		createEntityWithRelation(name, packageName, relationInput);
	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\" with a valid relation that does not specify fetch mode$")
	public void an_entity_named_in_with_a_valid_relation_that_does_not_specify_fetch_mode(String name,
			String packageName) throws Throwable {
		String referencedEntityName = StringUtils.capitalize(RandomStringUtils.randomAlphabetic(10));
		String referencedEntityPackage = "foo.default.fetchmode";
		createEntityWithDefaultIdentifier(referencedEntityName, referencedEntityPackage);

		RelationInput relationInput = new RelationInput();
		relationInput.type = referencedEntityName;
		relationInput.relationPackage = referencedEntityPackage;
		relationInput.documentation = RandomStringUtils.randomAlphanumeric(20);
		relationInput.multiplicity = Multiplicity.MANY_TO_MANY.toString();
		createEntityWithRelation(name, packageName, relationInput);
	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\" with an invalid multiplicity \"([^\"]*)\"$")
	public void an_entity_named_in_with_an_invalid_multiplicity(String name, String packageName,
			String invalidMultiplicity) throws Throwable {
		String referencedEntityName = StringUtils.capitalize(RandomStringUtils.randomAlphabetic(10));
		String referencedEntityPackage = "foo.bad.multiplicity";
		createEntityWithDefaultIdentifier(referencedEntityName, referencedEntityPackage);

		RelationInput relationInput = new RelationInput();
		relationInput.type = referencedEntityName;
		relationInput.relationPackage = referencedEntityPackage;
		relationInput.documentation = RandomStringUtils.randomAlphanumeric(20);
		relationInput.fetchMode = FetchMode.LAZY.toString();
		relationInput.multiplicity = invalidMultiplicity;
		createEntityWithRelation(name, packageName, relationInput);
	}

	@Given("^an entity named \"([^\"]*)\" in \"([^\"]*)\" with an invalid fetch mode \"([^\"]*)\"$")
	public void an_entity_named_in_with_an_invalid_fetch_mode(String name, String packageName, String invalidFetchMode)
			throws Throwable {
		String referencedEntityName = StringUtils.capitalize(RandomStringUtils.randomAlphabetic(10));
		String referencedEntityPackage = "foo.bad.multiplicity";
		createEntityWithDefaultIdentifier(referencedEntityName, referencedEntityPackage);

		RelationInput relationInput = new RelationInput();
		relationInput.type = referencedEntityName;
		relationInput.relationPackage = referencedEntityPackage;
		relationInput.documentation = RandomStringUtils.randomAlphanumeric(20);
		relationInput.fetchMode = invalidFetchMode;
		relationInput.multiplicity = Multiplicity.ONE_TO_ONE.toString();
		createEntityWithRelation(name, packageName, relationInput);
	}

	private EntityElement createEntityWithIdentifier(String name, String packageName, FieldInput id)
			throws IOException, JsonGenerationException, JsonMappingException, JsonProcessingException {
		EntityElement entity = createBaseEntity(name, packageName, null);
		FieldElement idField = createIdentifier(id);
		entity.setIdentifier(idField);

		return createEntityElement(entity);
	}

	private EntityElement createEntityWithDefaultIdentifier(String name, String packageName)
			throws IOException, JsonGenerationException, JsonMappingException, JsonProcessingException {
		EntityElement entity = createBaseEntity(name, packageName, null);
		FieldInput id = new FieldInput();
		id.name = "id";
		id.column = "ID";
		id.type = "string";
		FieldElement idField = createIdentifier(id);
		entity.setIdentifier(idField);

		return createEntityElement(entity);
	}

	private FieldElement createIdentifier(FieldInput id) {
		FieldElement idField = new FieldElement();
		idField.setName(id.name);
		idField.setDocumentation(id.documentation);
		idField.setColumn(id.column);
		if (StringUtils.isNotBlank(id.generator)) {
			idField.setGenerator(id.generator);
		}
		ValidationElement type = new ValidationElement();
		String typeValue = StringUtils.isNotBlank(id.type) ? id.type : "string";
		type.setName(typeValue);
		idField.setType(type);
		return idField;
	}

	private FieldElement createDefaultIdentifier() {
		FieldElement idField = new FieldElement();
		idField.setName("id");
		idField.setDocumentation("Auto created to make a valid entity");
		idField.setColumn("ID");
		ValidationElement type = new ValidationElement();
		type.setName("String");
		idField.setType(type);
		return idField;
	}

	private FieldElement createField(FieldInput field) {
		FieldElement newField = new FieldElement();
		newField.setName(field.name);
		newField.setDocumentation(field.documentation);
		newField.setColumn(field.column);
		ValidationElement type = new ValidationElement();
		type.setName(field.type);
		newField.setType(type);
		return newField;
	}

	private EntityElement createEntityElement(EntityElement entity)
			throws IOException, JsonGenerationException, JsonMappingException, JsonProcessingException {

		entityFile = new File(entitiesDirectory, entity.getName() + ".json");
		objectMapper.writeValue(entityFile, entity);
		logger.debug(objectMapper.writeValueAsString(entity));
		assertTrue("Entities not written to file!", entityFile.exists());

		currentBasePackage = entity.getPackage();

		return entity;
	}

	private EntityElement createBaseEntity(String name, String packageName, String documentation) {
		EntityElement entity = new EntityElement();
		if (StringUtils.isNotBlank(name)) {
			entity.setName(name);
		}
		entity.setPackage(packageName);
		entity.setDocumentation(documentation);
		return entity;
	}

	private EntityElement createEntityWithRelation(String name, String packageName, RelationInput relation)
			throws IOException, JsonGenerationException, JsonMappingException, JsonProcessingException {
		EntityElement entity = createBaseEntity(name, packageName, null);

		RelationElement entityRelation = new RelationElement();
		entityRelation.setType(relation.type);
		entityRelation.setPackage(relation.relationPackage);
		entityRelation.setLocalColumn(relation.localColumn);
		entityRelation.setDocumentation(relation.documentation);
		entityRelation.setMultiplicity(relation.multiplicity);
		entityRelation.setFetchMode(relation.fetchMode);

		entity.addRelation(entityRelation);

		return createEntityElement(entity);
	}

	@When("^entities are read$")
	public void entities_are_read() throws Throwable {
		encounteredException = null;

		try {
			ModelRepositoryConfiguration config = new ModelRepositoryConfiguration();
			config.setCurrentApplicationName("fermenter-mda");
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

	@Then("^an entity metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with the documentation \"([^\"]*)\"$")
	public void an_entity_metamodel_instance_is_returned_for_the_name_in_with_the_documentation(String expectedName,
			String expectedPackage, String expectedDocumentation) throws Throwable {

		validateLoadedEntities(expectedName, expectedPackage);
		assertEquals("Unexpected documentation value!", expectedDocumentation, loadedEntity.getDocumentation());

	}

	@Then("^an entity metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with parent \"([^\"]*)\" and inheritance strategy \"([^\"]*)\"$")
	public void an_entity_metamodel_instance_is_returned_for_the_name_in_with_parent_and_inheritance_strategy(
			String expectedName, String expectedPackage, String expectedParent, String expectedInheritanceStrategy)
			throws Throwable {

		validateLoadedEntities(expectedName, expectedPackage);
		Parent parent = loadedEntity.getParent();
		assertEquals("unexpected parent value!", expectedParent, parent.getType());
		InheritanceStrategy foundInheritanceStrategy = InheritanceStrategy.fromString(expectedInheritanceStrategy);
		assertEquals("unexpected inheritance strategy value!", foundInheritanceStrategy,
				parent.getInheritanceStrategy());

	}

	@Then("^an entity metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with table \"([^\"]*)\"$")
	public void an_entity_metamodel_instance_is_returned_for_the_name_in_with_table(String expectedName,
			String expectedPackage, String expectedTable) throws Throwable {

		validateLoadedEntities(expectedName, expectedPackage);
		assertEquals("unexpected table value!", expectedTable, loadedEntity.getTable());

	}

	@Then("^an entity metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with a lock strategy of \"([^\"]*)\"$")
	public void an_entity_metamodel_instance_is_returned_for_the_name_in_with_a_lock_strategy_of(String expectedName,
			String expectedPackage, String expectedStrategy) throws Throwable {

		validateLoadedEntities(expectedName, expectedPackage);
		LockStrategy expectedLockStrategy = LockStrategy.fromString(expectedStrategy);
		assertEquals("unexpected lock strategy value!", expectedLockStrategy, loadedEntity.getLockStrategy());

	}

	@Then("^an entity metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with a transient flag of \"([^\"]*)\"$")
	public void an_entity_metamodel_instance_is_returned_for_the_name_in_with_a_transient_flag_of(String expectedName,
			String expectedPackage, Boolean expectedTransient) throws Throwable {

		validateLoadedEntities(expectedName, expectedPackage);
		assertEquals("unexpected lock strategy value!", expectedTransient, loadedEntity.isTransient());

	}

	@Then("^an entity metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with the following identifier:$")
	public void an_entity_metamodel_instance_is_returned_for_the_name_in_with_the_following_identifier(
			String expectedName, String expectedPackage, List<FieldInput> expectedIdentifier) throws Throwable {
		validateLoadedEntities(expectedName, expectedPackage);
		Field foundIdField = loadedEntity.getIdentifier();
		assertNotNull("No identifier found!", foundIdField);

		FieldInput expectedIdField = expectedIdentifier.iterator().next();
		assertEquals("Identifier field name did not match!", expectedIdField.name, foundIdField.getName());
		assertEquals("Identifier column name did not match!", expectedIdField.column, foundIdField.getColumn());
		assertEquals("Identifier documentation did not match!", expectedIdField.documentation,
				foundIdField.getDocumentation());

		Validation foundType = foundIdField.getValidation();
		assertNotNull("No identifier type found!", foundType);
		assertEquals("Identifier type name did not match!", expectedIdField.type, foundType.getName());

	}

	@Then("^an entity metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with the following field:$")
	public void an_entity_metamodel_instance_is_returned_for_the_name_in_with_the_following_field(String expectedName,
			String expectedPackage, List<FieldInput> expectedFields) throws Throwable {

		validateLoadedEntities(expectedName, expectedPackage);

		Field foundField = null;
		FieldInput expectedField = expectedFields.iterator().next();
		List<Field> loadedEntityFields = loadedEntity.getFields();
		for (Field loadedEntityField : loadedEntityFields) {
			if (loadedEntityField.getName().equals(expectedField.name)) {
				foundField = loadedEntityField;
				assertEquals("Field name did not match!", expectedField.name, foundField.getName());
				assertEquals("Column name did not match!", expectedField.column, foundField.getColumn());
				assertEquals("Documentation did not match!", expectedField.documentation,
						foundField.getDocumentation());
				break;
			}
		}

		Validation foundType = foundField.getValidation();
		assertNotNull("No field type found!", foundType);
		assertEquals("Field type name did not match!", expectedField.type, foundType.getName());
	}

	@Then("^an entity metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with the following reference:$")
	public void an_entity_metamodel_instance_is_returned_for_the_name_in_with_the_following_reference(
			String expectedName, String expectedPackage, List<ReferenceInput> expectedReferences) throws Throwable {

		validateLoadedEntities(expectedName, expectedPackage);
		Reference foundReference = loadedEntity.getReferences().iterator().next();
		assertNotNull("No reference found!", foundReference);

		ReferenceInput expectedReference = expectedReferences.iterator().next();
		assertEquals("Reference name did not match!", expectedReference.referenceName, foundReference.getName());
		assertEquals("Reference documentation did not match!", expectedReference.documentation,
				foundReference.getDocumentation());
		assertEquals("Reference type did not match!", expectedReference.type, foundReference.getType());
		assertEquals("Reference type package did not match!", expectedReference.referencePackage,
				foundReference.getPackage());
		assertEquals("Reference local column did not match!", expectedReference.localColumn,
				foundReference.getLocalColumn());
		assertEquals("Reference requiredness did not match!", expectedReference.required, foundReference.isRequired());

	}

	@Then("^an entity metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with the following relation:$")
	public void an_entity_metamodel_instance_is_returned_for_the_name_in_with_the_following_relation(
			String expectedName, String expectedPackage, List<RelationInput> expectedRelations) throws Throwable {
		validateLoadedEntities(expectedName, expectedPackage);
		Relation foundRelation = getAndAssertRelation();

		RelationInput expectedRelation = expectedRelations.iterator().next();
		assertEquals("Relation documentation did not match!", expectedRelation.documentation,
				foundRelation.getDocumentation());
		assertEquals("Relation type did not match!", expectedRelation.type, foundRelation.getType());
		assertEquals("Relation type package did not match!", expectedRelation.relationPackage,
				foundRelation.getPackage());
		assertEquals("Relation local column did not match!", expectedRelation.localColumn,
				foundRelation.getLocalColumn());
		assertEquals("Relation multiplicity did not match!", Multiplicity.fromString(expectedRelation.multiplicity),
				foundRelation.getMultiplicity());
		assertEquals("Relation fetchMode did not match!", FetchMode.fromString(expectedRelation.fetchMode),
				foundRelation.getFetchMode());
	}

	@Then("^an entity metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with the following relation that has one-to-many multiplicity$")
	public void an_entity_metamodel_instance_is_returned_for_the_name_in_with_the_following_relation_that_has_M_multiplicity(
			String expectedName, String expectedPackage) throws Throwable {
		validateLoadedEntities(expectedName, expectedPackage);

		Relation foundRelation = getAndAssertRelation();

		assertEquals("Relation multiplicity did not match!", Multiplicity.ONE_TO_MANY, foundRelation.getMultiplicity());
	}

	@Then("^an entity metamodel instance is returned for the name \"([^\"]*)\" in \"([^\"]*)\" with the following relation that has eager fetch mode$")
	public void an_entity_metamodel_instance_is_returned_for_the_name_in_with_the_following_relation_that_has_eager_fetch_mode(
			String expectedName, String expectedPackage) throws Throwable {
		validateLoadedEntities(expectedName, expectedPackage);

		Relation foundRelation = getAndAssertRelation();

		assertEquals("Relation fetch modes did not match!", FetchMode.EAGER, foundRelation.getFetchMode());
	}

	private Relation getAndAssertRelation() {
		Relation foundRelation = loadedEntity.getRelations().iterator().next();
		assertNotNull("No relation found!", foundRelation);
		return foundRelation;
	}

	private void validateLoadedEntities(String expectedName, String expectedPackage) {
		if (encounteredException != null) {
			throw encounteredException;
		}

		loadedEntity = metadataRepo.getEntities(expectedPackage).get(expectedName);
		assertEquals("Unexpected entity name!", expectedName, loadedEntity.getName());
		assertEquals("Unexpected entity package!", expectedPackage, loadedEntity.getPackage());

	}

	/**
	 * Uses to pass field-level information into test steps
	 */
	public class FieldInput {
		public String name;
		public String fieldPackage;
		public String documentation;
		public String type;
		public String column;
		public String generator;
		public Boolean required;
	}

	/**
	 * Uses to pass reference-level information into test steps
	 */
	public class ReferenceInput {
		public String name;
		public String referenceName;
		public String documentation;
		public String type;
		public String referencePackage;
		public Boolean required;
		public String localColumn;
	}

	/**
	 * Uses to pass relation-level information into test steps
	 */
	public class RelationInput {
		public String documentation;
		public String type;
		public String relationPackage;
		public String multiplicity;
		public String localColumn;
		public String fetchMode;
	}

}
