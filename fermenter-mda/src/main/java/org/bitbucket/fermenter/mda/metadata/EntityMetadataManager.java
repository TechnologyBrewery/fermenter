package org.bitbucket.fermenter.mda.metadata;


import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.element.CompositeInstanceMetadata;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.EntityMetadata;
import org.bitbucket.fermenter.mda.metadata.element.FieldMetadata;
import org.bitbucket.fermenter.mda.metadata.element.ParentMetadata;
import org.bitbucket.fermenter.mda.metadata.element.QueryMetadata;
import org.bitbucket.fermenter.mda.metadata.element.ReferenceMetadata;
import org.bitbucket.fermenter.mda.metadata.element.RelationMetadata;

/**
 * Responsible for managing the list of entity metadata elements in the system.
 * 
 * @author sandrews
 *
 */
@Deprecated
class EntityMetadataManager extends MetadataManager {
	

    private static EntityMetadataManager INSTANCE = null;

	private static Log log = LogFactory.getLog( EntityMetadataManager.class );
	
	public static EntityMetadataManager getInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new EntityMetadataManager();
		}
		
		return INSTANCE;
	}
	
	private EntityMetadataManager() {
	}
	
	protected String getMetadataLocation() {
		return "entities";
	}
	
	/**
	 * Answer the metadata for a specified entity
	 * @param entityName
	 * @return
	 */
	public static Entity getEntityMetadata(String applicationName, String entityName) {
		Map entityMap = getInstance().getMetadataMap( applicationName );
		return (entityMap != null) ? (Entity)entityMap.get( entityName ) : null;
	}
	
	/**
	 * Answer all of the entity metadata
	 * 
	 * @return
	 */
	public static Map<String, Entity> getEntities(String applicationName) {
		Map<String, Entity> entityMap = getInstance().getMetadataMap( applicationName );		
		return (entityMap != null) ? entityMap : Collections.EMPTY_MAP;
	}
	
	/**
	 * Returns the metadata element by name from any application that is loaded
	 * @param name The name by which to retrieve
	 * @return The <tt>Entity</tt> instance for <tt>name</tt> or null
	 */
	public static Entity getEntityMetadata(String entityName) {
		Map entityMap = getInstance().getCompleteMetadataMap();
		return (entityMap != null) ? (Entity)entityMap.get(entityName) : null;
	}
	
	public static boolean isLoaded(String applicationName) {
		return ( getInstance().getMetadataMap( applicationName ) == null ) ? true : false;
	}

	/**
	 * Configure the digester to parse the entity metadata
	 */
	protected void initialize(Digester digester) {
	    final String[] ENTITY_PROPERTIES = new String[] {"transient"};
	    
	    // Root entity metadata
		digester.addObjectCreate( 	"entity", 				EntityMetadata.class.getName() );
		digester.addCallMethod(  "entity/namespace",     "setNamespace"       , 0 );
		digester.addCallMethod( 	"entity/name", 			"setName"			, 0 );
		digester.addCallMethod( 	"entity/documentation", "setDocumentation"	, 0 );
        parseParent(digester);
		digester.addCallMethod( 	"entity/table", 		"setTable"			, 0 );
		digester.addCallMethod( 	"entity/lockStrategy", 	"setLockStrategy"	, 0 );
		digester.addSetProperties(  "entity", ENTITY_PROPERTIES, ENTITY_PROPERTIES);
		
	// Non-identifying field metadata
		parseFields( digester );
		
	// Identifying field metadata
		parseIdFields( digester );
		
	// Composite metadata
		parseComposites( digester );
		
	// Relation metadata
		parseRelations( digester );

	// Reference metadata
		parseReferences( digester );
        
	// Query metadata
		parseQueries( digester );
               
		digester.addSetNext( "entity", "addEntity", EntityMetadata.class.getName() );
		
		if (log.isDebugEnabled()) {
			log.debug("Initialization complete");
		}
	}
	
	public void addEntity(EntityMetadata ed) {
		ed.setApplicationName(currentApplication);
		addMetadataElement( ed.getName(), ed );
	}
	
	private void parseParent(Digester digester) {
	    digester.addObjectCreate(   "entity/parent"                     , ParentMetadata.class.getName());
        digester.addCallMethod  (   "entity/parent/type"                , "setType"         , 0 );
        digester.addCallMethod  (   "entity/parent/inheritanceStrategy" , "setInheritanceStrategy", 0 );
        digester.addSetNext     (   "entity/parent"                     , "setParent"       , ParentMetadata.class.getName());
	}
	
	private void parseFields(Digester digester) {
		digester.addObjectCreate( 	"entity/fields/field"			, 	FieldMetadata.class.getName() );
		digester.addCallMethod	( 	"entity/fields/field/name"		, 	"setName"		, 	0 );
		digester.addCallMethod	( 	"entity/fields/field/documentation", "setDocumentation", 0 );
		digester.addCallMethod	( 	"entity/fields/field/type"		, 	"setType"		, 	0 );
		digester.addCallMethod	( 	"entity/fields/field/label"		, 	"setLabel"		, 	0 );
		digester.addCallMethod	( 	"entity/fields/field/column"	, 	"setColumn"		, 	0 );
		digester.addCallMethod	( 	"entity/fields/field/required"	,	"setRequired"	, 	0 );
		digester.addCallMethod	( 	"entity/fields/field/transient"	,	"setTransient"	, 	0 );
		digester.addCallMethod	( 	"entity/fields/field/maxLength"	, 	"setMaxLength"	, 	0 );
		digester.addCallMethod	( 	"entity/fields/field/minLength"	,	"setMinLength"	, 	0 );
		digester.addCallMethod	( 	"entity/fields/field/maxValue"	, 	"setMaxValue"	, 	0 );
		digester.addCallMethod	( 	"entity/fields/field/minValue"	, 	"setMinValue"	, 	0 );
		digester.addCallMethod	( 	"entity/fields/field/scale"	    , 	"setScale"	    , 	0 );
		digester.addCallMethod	( 	"entity/fields/field/project"	, 	"setProject"	, 	0 );
		digester.addCallMethod	( 	"entity/fields/field/format"	, 	"setFormat"	    , 	0 );
		digester.addCallMethod  (   "entity/fields/field/defaultValue", "setDefaultValue",  0 );
		digester.addSetNext		(	"entity/fields/field"			, 	"addField"		,	FieldMetadata.class.getName() );
	}
	
	private void parseIdFields(Digester digester) {
		digester.addObjectCreate( "entity/id/field"				,	FieldMetadata.class.getName() 					);
		digester.addCallMethod	( "entity/id/field/name"		,	"setName"		, 0 					);
		digester.addCallMethod	( "entity/id/field/type"		,	"setType"		, 0 					);
		digester.addCallMethod	( "entity/id/field/label"		,	"setLabel"		, 0 					);
		digester.addCallMethod	( "entity/id/field/column"		,	"setColumn"		, 0 					);
		digester.addCallMethod	( "entity/id/field/required"	,	"setRequired"	, 0 					);
		digester.addCallMethod	( "entity/id/field/maxLength"	,	"setMaxLength"	, 0 					);
		digester.addCallMethod	( "entity/id/field/minLength"	, 	"setMinLength"	, 0 					);
		digester.addCallMethod	( "entity/id/field/maxValue"	, 	"setMaxValue"	, 0 					);
		digester.addCallMethod	( "entity/id/field/minValue"	, 	"setMinValue"	, 0 					);						
		digester.addCallMethod	( "entity/id/field/generator"	, 	"setGenerator"	, 0 					);
		digester.addSetNext		( "entity/id/field"				,	"addIdField"	, FieldMetadata.class.getName() );		
	}
	
	private void parseComposites(Digester digester) {
		digester.addObjectCreate( 	"entity/composites/composite"		, 	CompositeInstanceMetadata.class.getName() 					);
		digester.addCallMethod	( 	"entity/composites/composite/type"	,	"setType"		,		0 					);
		digester.addCallMethod	( 	"entity/composites/composite/prefix", 	"setPrefix"		, 		0 					);
		digester.addCallMethod	( 	"entity/composites/composite/project", 	"setProject"		, 		0 					);
		digester.addCallMethod	( 	"entity/composites/composite/name", 	"setName"		, 		0 					);
		digester.addCallMethod	( 	"entity/composites/composite/label", 	"setLabel"		, 		0 					);
		digester.addSetNext		(	"entity/composites/composite"		, 	"addComposite"	, CompositeInstanceMetadata.class.getName() );
	}

	private void parseRelations(Digester digester) {
		digester.addObjectCreate( "entity/associations/relation"				,	RelationMetadata.class.getName() 							);
		digester.addCallMethod	( "entity/associations/relation/documentation"	,	"setDocumentation"	,	0 							);
		digester.addCallMethod	( "entity/associations/relation/type"			,	"setType"			,	0 							);
	    digester.addCallMethod  ( "entity/associations/relation/fk"             ,   "addFkOverride"     ,   3                           );
	    digester.addCallParam   ( "entity/associations/relation/fk/source"     ,   0                                                   );
	    digester.addCallParam   ( "entity/associations/relation/fk/name"       ,   1                                                   );
	    digester.addCallParam   ( "entity/associations/relation/fk/column"     ,   2                                                   );
		digester.addCallMethod	( "entity/associations/relation/multiplicity"	,	"setMultiplicity"	,	0 							);
		digester.addCallMethod	( "entity/associations/relation/fetchMode"	,		"setFetchMode"		,	0 							);
		digester.addSetNext		( "entity/associations/relation"				,	"addRelation"		, 	RelationMetadata.class.getName() 	);
	}
	
	private void parseReferences(Digester digester) {
		digester.addObjectCreate(	"entity/associations/reference"				,	ReferenceMetadata.class.getName() 				);
		digester.addCallMethod	( 	"entity/associations/reference/type"		, 	"setType"		, 	0 							);
		digester.addCallMethod	( 	"entity/associations/reference/project"		, 	"setProject"	, 	0 							);
		digester.addCallMethod	( 	"entity/associations/reference/name"		, 	"setName"		, 	0 							);
		digester.addCallMethod	( 	"entity/associations/reference/documentation", 	"setDocumentation", 	0 							);
		digester.addCallMethod	( 	"entity/associations/reference/required"	,	"setRequired"	, 	0 							);
		digester.addCallMethod	( 	"entity/associations/reference/fk"			, 	"addFkOverride"	,	3 							);
		digester.addCallParam	( 	"entity/associations/reference/fk/source"	,	0 												);
		digester.addCallParam	( 	"entity/associations/reference/fk/name"		, 	1 												);
		digester.addCallParam	( 	"entity/associations/reference/fk/column"	, 	2 												);
		digester.addSetNext		( 	"entity/associations/reference"				,	"addReference"	, 	ReferenceMetadata.class.getName()	);
	}
	
	private void parseQueries(Digester digester) {
        digester.addObjectCreate(	"entity/queries/query"							,	QueryMetadata.class.getName() 					 );
        digester.addCallMethod	( 	"entity/queries/query/name"						, 	"setName", 			0 							 );
        digester.addCallMethod	( 	"entity/queries/query/documentation"			, 	"setDocumentation", 0 							 );
        digester.addObjectCreate( 	"entity/queries/query/criteria/criterion"		, 	FieldMetadata.class.getName() 					 );
        digester.addCallMethod	( 	"entity/queries/query/criteria/criterion/name"	,	"setName", 			0 							 );
        digester.addCallMethod	( 	"entity/queries/query/criteria/criterion/documentation", "setDocumentation", 0 						 );
        digester.addCallMethod	( 	"entity/queries/query/criteria/criterion/type"	, 	"setType", 			0 							 );
        digester.addSetNext		( 	"entity/queries/query/criteria/criterion"		, 	"addCriterion", 	FieldMetadata.class.getName());
        digester.addCallMethod  (   "entity/queries/query/pagination"               ,   "setPagination",    0                            );
        digester.addCallMethod	( 	"entity/queries/query/statement"				, 	"setStatement", 	0 						     );
        digester.addSetNext		( 	"entity/queries/query"							, 	"addQuery", 		QueryMetadata.class.getName());
	}
	
	
	/**
	 * Iterate over loaded domains and register each relation on its parent. 
	 * This enables bi-directional referencing of relations.  It is also 
	 * important to note that while referring to a parent from a child is 
	 * very similar to a reference, it is typically not similar enough to
	 * assume that it will be implemented in this fashion.  Seperating them 
	 * into their own collection ensures that they can be dealt with as 
	 * appropriate for the target implementation.
	 */
	protected void postLoadMetadata() {
		
		super.postLoadMetadata();

		EntityMetadata entity;
		EntityMetadata childEntity;
		RelationMetadata relation;
		CompositeInstanceMetadata composite;
		String relationType;
		Map relationMap;
		Map compositeMap;
		Iterator relationValueInterator;
		Iterator compositeValueInterator;
		Map entityMap = getMetadataMap( currentApplication );
		Map compositeMetadataMap = CompositeMetadataManager.getInstance().getCompleteMetadataMap();
		Iterator entityMapIterator = entityMap.values().iterator();		
		while (entityMapIterator.hasNext()) {
			entity = (EntityMetadata) entityMapIterator.next();
			relationMap = entity.getRelations();
			relationValueInterator = (relationMap != null) 
				? relationMap.values().iterator() : Collections.EMPTY_LIST.iterator();
			while (relationValueInterator.hasNext()) {
				relation = (RelationMetadata)relationValueInterator.next();
				relationType = relation.getType();
				//TODO: check 1-M and 1-1 only:
			
				childEntity = (EntityMetadata)entityMap.get(relationType);
				if (childEntity !=null) {
					childEntity.addInverseRelation(entity);
				} else {
					throw new GenerationException("Could not find a relation to entity: "+relationType);
				}
							
			}
			
			//link composite instances to their base object:
			compositeMap = entity.getComposites();
			compositeValueInterator = (compositeMap != null) 
				? compositeMap.values().iterator() : Collections.EMPTY_LIST.iterator();
			while (compositeValueInterator.hasNext()) {
				composite = (CompositeInstanceMetadata)compositeValueInterator.next();
				composite.linkToBaseComposite(compositeMetadataMap);
				
			}
		}
	}

}
