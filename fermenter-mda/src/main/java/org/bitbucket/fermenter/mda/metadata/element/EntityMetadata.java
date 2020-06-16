package org.bitbucket.fermenter.mda.metadata.element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.PackageManager;
import org.bitbucket.fermenter.mda.metadata.MetadataManager;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Parent.InheritanceStrategy;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

@Deprecated
public class EntityMetadata extends MetadataElement implements Entity {

	private static final String TRANSIENT_ENTITY = "transient entity '";

	private static Log LOG = LogFactory.getLog(EntityMetadata.class);

	private String namespace;
	private String name;
	private String documentation;
	private String applicationName;

	private String table;

	private Parent parent;
	private String lockStrategy;
	private boolean transientEntity;

	private Map<String, Field> fields;
	private Map<String, Field> idFields;
	private Map composites;
	private Map<String, Relation> relations;
	private Map inverseRelations;
	private Map<String, Reference> references;
	private Map queries;

	public String getNamespace() {

		if (namespace == null) {
			return "";
		} else {
			return namespace;
		}
	}

	/**
	 * Sets the namespace of the current entity. If a namespace is not given (method
	 * was provided null or ""), the base package will be used.
	 * 
	 * @param namespace
	 */
	public void setNamespace(String namespace) {

		if (namespace != null && !namespace.isEmpty()) {
			this.namespace = namespace;
		} else {
			this.namespace = PackageManager.getBasePackage(applicationName);
		}
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string The logical entity name
	 */
	public void setName(String string) {
		name = string;
	}

	/*
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getDocumentation()
	 */
	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * @return Returns the applicationName.
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName The applicationName to set.
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getTable()
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @param table The table to set.
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, Field> getFields() {
		if (fields == null) {
			fields = new HashMap<>();
		}

		return fields;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getField(java.lang.String)
	 */
	public Field getField(String name) {
		return getFields().get(name);
	}

	public void addField(Field field) {
		getFields().put(field.getName(), field);
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, Field> getIdFields() {
		if (idFields == null) {
			idFields = new HashMap<>();
		}

		return idFields;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getIdField(java.lang.String)
	 */
	public Field getIdField(String name) {
		return getIdFields().get(name);
	}

	public void addIdField(Field field) {
		String generator = field.getGenerator();
		if (StringUtils.isNotBlank(generator) && generator.equals("assigned")) {
			((FieldMetadata) field).setRequired("true");
		}

		getIdFields().put(field.getName(), field);
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getComposites()
	 */
	public Map getComposites() {
		if (composites == null) {
			composites = new HashMap();
		}

		return composites;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getComposite(java.lang.String)
	 */
	public Composite getComposite(String name) {
		return (Composite) getComposites().get(name);
	}

	public void addComposite(Composite composite) {
		getComposites().put(composite.getName(), composite);
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getRelations()
	 */
	public Map<String, Relation> getRelations() {
		if (relations == null) {
			relations = new HashMap<>();
		}

		return relations;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getRelation(java.lang.String)
	 */
	public Relation getRelation(String type) {
		return (Relation) getRelations().get(type);
	}

	public void addRelation(Relation relation) {
		getRelations().put(relation.getType(), relation);
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getInverseRelations()
	 */
	public Map getInverseRelations() {
		if (inverseRelations == null) {
			inverseRelations = new HashMap();
		}

		return inverseRelations;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getInverseRelation(java.lang.String)
	 */
	public Relation getInverseRelation(String type) {
		return (Relation) getInverseRelations().get(type);
	}

	public void addInverseRelation(Entity reverseRelation) {
		getInverseRelations().put(reverseRelation.getName(), reverseRelation);
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getReferences()
	 */
	public Map<String, Reference> getReferences() {
		if (references == null) {
			references = new HashMap<>();
		}

		return references;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getReference(java.lang.String)
	 */
	public Reference getReference(String type) {
		return (Reference) getReferences().get(type);
	}

	public void addReference(Reference reference) {
		getReferences().put(reference.getName(), reference);
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getParent()
	 */
	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getQueries()
	 * @deprecated
	 */
	public Map getQueries() {
		if (queries == null) {
			queries = new HashMap();
		}

		return queries;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getQuery(java.lang.String)
	 * @deprecated
	 */
	public Query getQuery(String name) {
		return (Query) getQueries().get(name);
	}

	/**
	 * @param query
	 * @deprecated
	 */
	public void addQuery(Query query) {
		getQueries().put(query.getName(), query);
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getLockStrategy()
	 */
	public String getLockStrategy() {
		if (lockStrategy == null) {
			lockStrategy = LOCK_STATEGY_OPTIMISTIC;
		}
		return lockStrategy;
	}

	public void setLockStrategy(String lockStrategy) {
		this.lockStrategy = LOCK_STATEGY_NONE.equals(lockStrategy) ? LOCK_STATEGY_NONE : LOCK_STATEGY_OPTIMISTIC;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#useOptimisticLocking()
	 */
	public boolean useOptimisticLocking() {
		return LOCK_STATEGY_OPTIMISTIC.equals(getLockStrategy());
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTransient(boolean transientEntity) {
		this.transientEntity = transientEntity;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isTransient() {
		return transientEntity;
	}

	/**
	 * Performs validation on this instance and its children.
	 */
	public void validate() {

		// validates the parent entity, if provided
		Parent parent = getParent();
		if (parent != null) {
			((MetadataElement) parent).validate();
		}

		// id fields:
		MetadataManager.validateElements(getIdFields().values());
		// ID fields can't be enumerated types
		for (Iterator i = getIdFields().values().iterator(); i.hasNext();) {
			Field id = (Field) i.next();
			if (id.isEnumerationType().booleanValue()) {
				throw new IllegalArgumentException("id fields cannot have an enumerated type");
			}
		}

		checkForPersistentAndTransientViolations();

		// fields:
		MetadataManager.validateElements(getFields().values());

		// references:
		MetadataManager.validateElements(getReferences().values());

		// relations:
		MetadataManager.validateElements(getRelations().values());

		// composites:
		MetadataManager.validateElements(getComposites().values());

		// queriess:
		MetadataManager.validateElements(getQueries().values());

	}

	public void checkForPersistentAndTransientViolations() {
		if (!isTransient()) {
			boolean transientIssuesFound = false;

			if (isNonPersistentParentEntity()) {
				for (Field f : getFields().values()) {
					if (checkPersistentFieldForColumn(f)) {
						transientIssuesFound = true;
					}
				}
				// XXX at the moment due some complexities around how we use generics, ID
				// elements can *not* be defined
				// on a non-persistent parent entity. these ID fields must be defined in the
				// corresponding concrete
				// child entities.
				if (!getIdFields().isEmpty()) {
					transientIssuesFound = true;
					LOG.error("non-persistent parent entity '" + getName() + "' cannot have any id fields specified");
				}

				for (Field f : getIdFields().values()) {
					if (checkPersistentFieldForColumn(f)) {
						transientIssuesFound = true;
					}
				}
			} else {
				// is there a table?
				if (StringUtils.isBlank(getTable())) {
					transientIssuesFound = true;
					LOG.error("persistent entity '" + getName() + "' requires a table to be specified!");
				}

				if (getIdFields().size() == 0) {
					transientIssuesFound = true;
					LOG.error("persistent entity '" + getName() + "' requires an id field to be specified!");
				}

				for (Field f : getIdFields().values()) {
					if (checkPersistentFieldForColumn(f)) {
						transientIssuesFound = true;
					}
				}

				for (Field f : getFields().values()) {
					if (checkPersistentFieldForColumn(f)) {
						transientIssuesFound = true;
					}
					
					checkTransientFieldForColumn(f);
				}
			}

			if (transientIssuesFound) {
				throw new IllegalArgumentException("Persistent information missing from entity '" + getName() + "'!");
			}

		} else if (isTransient()) {
			if (StringUtils.isNotBlank(getTable())) {
				LOG.warn(TRANSIENT_ENTITY + getName() + "' specifies a table value which will be ignored");
			}

			if (getIdFields().size() > 0) {
				LOG.error(TRANSIENT_ENTITY + getName() + "' should not specify an id field");
			}

			for (Field f : getIdFields().values()) {
				checkTransientFieldForColumn(f);
			}

			for (Field f : getFields().values()) {
				checkTransientFieldForColumn(f);
			}

		}
	}

	protected boolean checkPersistentFieldForColumn(Field f) {
		boolean persistentFieldIssuesFound = false;
		if (!f.isTransient() && StringUtils.isBlank(f.getColumn())) {
			persistentFieldIssuesFound = true;
			LOG.error("persistent entity " + getName() + "." + f.getName() + "' requires a column to be specified!");
		}
		return persistentFieldIssuesFound;
	}

	protected void checkTransientFieldForColumn(Field f) {
		if (f.isTransient() && !StringUtils.isBlank(f.getColumn())) {
			LOG.error(TRANSIENT_ENTITY + getName() + "." + f.getName() + "' specifies a column which will be ignored");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNonPersistentParentEntity() {

		Map<String, Entity> allEntities = ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class)
				.getAllEntities();
		for (Entity entity : allEntities.values()) {
			Parent parent = entity.getParent();
			if (parent != null && getName().equals(parent.getType())
					&& InheritanceStrategy.MAPPED_SUPERCLASS.equals(parent.getInheritanceStrategy())) {

				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isChildOfNonPersistentParentEntity() {
		return (getParent() == null) ? false
				: Parent.InheritanceStrategy.MAPPED_SUPERCLASS.equals(getParent().getInheritanceStrategy());
	}
}
