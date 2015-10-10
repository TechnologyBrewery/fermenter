package org.bitbucket.fermenter.mda.metadata.element;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bitbucket.fermenter.mda.metadata.MetadataManager;

public class EntityMetadata extends MetadataElement implements Entity {

	private String name;
	private String documentation;
	private String applicationName;
	/**
	 * @deprecated shouldn't be here anymore
	 */
	private String superclass;
	private String table;
	/**
	 * @deprecated don't think this is used anymore... still need to check
	 */
    private String parent;
    private String lockStrategy;
	private Map    fields;
	private Map	   idFields;
	private Map	   composites;
	private Map    relations;
	private Map    inverseRelations;
	private Map    references;
    private Map    queries;    

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
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getFields()
	 */
	public Map getFields() {
		if( fields == null ) {
			fields = new HashMap();
		}
		
		return fields;
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getField(java.lang.String)
	 */
	public Field getField(String name) {
		return (Field) getFields().get( name );
	}
	
	public void addField(Field field) {
		getFields().put( field.getName(), field );
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getIdFields()
	 */
	public Map getIdFields() {
		if( idFields == null ) {
			idFields = new HashMap();
		}
		
		return idFields;
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getIdField(java.lang.String)
	 */
	public Field getIdField(String name) {
		return (Field) getIdFields().get( name );
	}
	
	public void addIdField(Field field) {
		if( field.getGenerator().equals( "assigned" ) ) {
			((FieldMetadata)field).setRequired( "true" );
		}
		else {
			((FieldMetadata)field).setRequired( "false" );
		}
		getIdFields().put( field.getName(), field );
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getComposites()
	 */
	public Map getComposites() {
		if( composites == null ) {
			composites = new HashMap();
		}
		
		return composites;
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getComposite(java.lang.String)
	 */
	public Composite getComposite(String name) {
		return (Composite) getComposites().get( name );
	}
	
	public void addComposite(Composite composite) {
		getComposites().put( composite.getName(), composite );
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getRelations()
	 */
	public Map getRelations() {
		if( relations == null ) {
			relations = new HashMap();
		}
		
		return relations;
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getRelation(java.lang.String)
	 */
	public Relation getRelation(String type) {
		return (Relation) getRelations().get( type );
	}
	
	public void addRelation(Relation relation) {
		getRelations().put( relation.getType(), relation );
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getInverseRelations()
	 */
	public Map getInverseRelations() {
		if( inverseRelations == null ) {
			inverseRelations = new HashMap();
		}
		
		return inverseRelations;
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getInverseRelation(java.lang.String)
	 */
	public Relation getInverseRelation(String type) {
		return (Relation) getInverseRelations().get( type );
	}
	
	public void addInverseRelation(Entity reverseRelation) {
		getInverseRelations().put( reverseRelation.getName(), reverseRelation );
	}	
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getReferences()
	 */
	public Map getReferences() {
		if( references == null ) {
			references = new HashMap();
		}
		
		return references;
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getReference(java.lang.String)
	 */
	public Reference getReference(String type) {
		return (Reference) getReferences().get( type );
	}
	
	public void addReference(Reference reference) {
		getReferences().put( reference.getName(), reference );
	}
	

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getSuperclass()
	 * @deprecated shouldn't be here anymore
	 */
	public String getSuperclass() {
		if( superclass == null ) {
			superclass = "Base";
		}
        
		return superclass;
	}
	
	/**
	 * @param superclass The superclass to set.
	 * @deprecated shouldn't be here anymore
	 */
	public void setSuperclass(String superclass) {
		this.superclass = superclass;
	}

    /**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getParent()
	 * @deprecated I don't think this is used anymore... need to check
	 */
    public String getParent() {
    	if( parent == null ) {
    		parent = "null";
    	}
    	
        return parent;
    }

    /**
     * @param parent The parent to set.
     * @deprecated I don't think this is used anymore... need to check
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

    
    /**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getQueries()
	 */
    public Map getQueries() {
        if( queries == null ) {
            queries = new HashMap();
        }
        
        return queries;
    }
    
    /**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Entity#getQuery(java.lang.String)
	 */
    public Query getQuery(String name) {
        return (Query) getQueries().get( name );
    }
    
    public void addQuery(Query query) {
        getQueries().put( query.getName(), query );
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
	 * Performs validation on this instance and its children
	 */
	public void validate() {
		//id fields:
		MetadataManager.validateElements(getIdFields().values());
		// ID fields can't be enumerated types
		for (Iterator i = getIdFields().values().iterator(); i.hasNext();) {
			Field id = (Field) i.next();
			if (id.isEnumerationType().booleanValue()) {
				throw new IllegalArgumentException("id fields cannot have an enumerated type");
			}
		}
		
		//fields:
		MetadataManager.validateElements(getFields().values());
		
		//references:
		MetadataManager.validateElements(getReferences().values());
		
		//relations:
		MetadataManager.validateElements(getRelations().values());	
		
		//composites:
		MetadataManager.validateElements(getComposites().values());	
		
		//queriess:
		MetadataManager.validateElements(getQueries().values());		
		
	}
}
