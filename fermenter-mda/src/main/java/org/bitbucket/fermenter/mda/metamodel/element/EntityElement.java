package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

/**
 * Represents a entity contains at least one operation.
 */
@JsonPropertyOrder({ "package", "name" })
public class EntityElement extends NamespacedMetamodelElement implements Entity {

	@JsonInclude(Include.NON_NULL)
	protected String documentation;

	@JsonInclude(Include.NON_NULL)
	protected String table;

	@JsonInclude(Include.NON_NULL)
	protected LockStrategy lockStrategy;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty(value = "transient")
	protected Boolean transientEntity;

	@JsonInclude(Include.NON_NULL)
	protected Parent parent;

	@JsonInclude(Include.NON_NULL)
	protected Field identifier;

	@JsonInclude(Include.NON_EMPTY)
	protected List<Field> fields = new ArrayList<>();

	@JsonInclude(Include.NON_EMPTY)
	protected List<Reference> references = new ArrayList<>();

	@JsonInclude(Include.NON_EMPTY)
	protected List<Relation> relations = new ArrayList<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTable() {
		return table;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockStrategy getLockStrategy() {
		return lockStrategy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonInclude(Include.NON_NULL)
	public Boolean isTransient() {
		return transientEntity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Parent getParent() {
		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field getIdentifier() {
		return identifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Reference> getReferences() {
		return references;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Relation> getRelations() {
		return relations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validate() {
		if (StringUtils.isBlank(getName())) {
			messageTracker.addErrorMessage("A service has been specified without a name!");

		}

		if (lockStrategy == null) {
			lockStrategy = LockStrategy.OPTIMISTIC;
		}

		if (transientEntity == null) {
			transientEntity = Boolean.FALSE;
		}

		if (parent != null) {
			parent.validate();
		}

		for (Field field : fields) {
			field.validate();
		}

		for (Reference reference : references) {
			reference.validate();
		}

		for (Relation relation : relations) {
			relation.validate();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSchemaFileName() {
		return "fermenter-2-entity-schema.json";
	}

	/**
	 * Sets the documentation value.
	 * 
	 * @param documentation documentation text
	 */
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * Sets the table value.
	 * 
	 * @param table table text
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * Sets the lock strategy for this instance.
	 * 
	 * @param strategy strategy
	 */
	public void setLockStrategy(String strategy) {
		this.lockStrategy = LockStrategy.fromString(strategy);

		if (StringUtils.isNoneBlank(strategy) && this.lockStrategy == null) {
			messageTracker.addErrorMessage("Could not map lock strategy '" + strategy
					+ "' to one of the known lock strategy types! (" + LockStrategy.options() + ") ");
		}
	}

	/**
	 * Sets the transient value.
	 * 
	 * @param tranientEntity transient setting object
	 */
	public void setTransient(Boolean tranientEntity) {
		this.transientEntity = tranientEntity;
	}

	/**
	 * Sets the parent value.
	 * 
	 * @param parent parent object
	 */
	public void setParent(Parent parent) {
		this.parent = parent;
	}

	/**
	 * Sets the identifier value.
	 * 
	 * @param identifier identifier object
	 */
	public void setIdentifier(Field identifier) {
		this.identifier = identifier;
	}

	/**
	 * Adds a field to this entity.
	 * 
	 * @param field field to add
	 */
	public void addField(Field field) {
		fields.add(field);
	}

	/**
	 * Adds a reference to this entity.
	 * 
	 * @param reference reference to add
	 */
	public void addReference(Reference reference) {
		references.add(reference);
	}

	/**
	 * Adds a relation to this entity.
	 * 
	 * @param relation relation to add
	 */
	public void addRelation(Relation relation) {
		relations.add(relation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("package", getPackage()).add("name", name).toString();
	}

}