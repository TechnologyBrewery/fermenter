package org.bitbucket.fermenter.mda.metadata.element;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

@Deprecated
public class RelationMetadata extends MetadataElement implements Relation {

	private String documentation;
	private String type;
	private String multiplicity;
	private String fetchMode;
	private String table;
	private Map<String, Field> fkOverrides = new HashMap<>();

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Relation#getDocumentation()
	 */
	public String getDocumentation() {
		return documentation;
	}
	
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Relation#getMultiplicity()
	 */
	public String getMultiplicity() {
		return multiplicity;
	}
	
	/**
	 * @param multiplicity The multiplicity to set.
	 */
	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getFetchMode() {
		return fetchMode;
	}

	/**
	 * Sets the fetch mode on the this relationship (e.g., eager, lazy).
	 * @param fetchMode how to fetch this relationship
	 */
	public void setFetchMode(String fetchMode) {
		this.fetchMode = fetchMode;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Relation#getType()
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
     * Store a collection of foreign key override values
     * @param source Field name in the referenced entity
     * @param name Field name in the referencing entity
     * @param column Column name in the referencing entity
     */
    public void addFkOverride(String source, String name, String column) {
        FieldMetadata f = new FieldMetadata();
        f.setName( name );
        f.setColumn( column );
        fkOverrides.put( source, f );
    }
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Relation#getLabel()
	 */
	public String getLabel() {
		return StringUtils.uncapitalize( type );
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Relation#getTable()
	 */
	public String getTable() {
		if( table == null ) {
		    MetadataRepository metadataRepository = 
                    ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class);
			table = metadataRepository.getEntity( type ).getTable();
		}
		
		return table;
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Relation#getChildRelations()
	 */
	public Collection getChildRelations() {
	    MetadataRepository metadataRepository = 
                ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		return metadataRepository.getEntity( type ).getRelations().values();	
	}
	
    /**
     * @see org.bitbucket.fermenter.mda.metadata.Relation#getKeys()
     */
    public Collection<Field> getKeys(String parentEntityName) {
        MetadataRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(MetadataRepository.class);
        Entity ed = metadataRepository.getEntity(parentEntityName);

        // Apply overrides
        Collection<Field> keyValues = new ArrayList<>();
        for (Field id : ed.getIdFields().values()) {
            ForeignKeyFieldMetadata newId = new ForeignKeyFieldMetadata();
            Field override = (Field) fkOverrides.get(id.getName());
            newId.setType(id.getType());
            newId.setSourceName(StringUtils.capitalize(id.getName()));
            newId.setName((fkOverrides.size() == 0) ? id.getName() : override.getName());
            newId.setColumn((fkOverrides.size() == 0) ? id.getColumn() : override.getColumn());
            newId.setParentColumn(id.getColumn());
            newId.setMaxLength(id.getMaxLength());
            newId.setMinLength(id.getMinLength());
            keyValues.add(newId);
        }

        return keyValues;
    }
    
    public Field getForeignKeyOverride() { 
    	return (fkOverrides != null) ? fkOverrides.values().iterator().next() : null;
    }
	
	/**
	 * Executed to ensure that valid combinations of metadata have been loaded.
	 */
	public void validate() {
	}
	
}
