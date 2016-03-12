package org.bitbucket.fermenter.mda.metadata.element;


import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;

public class RelationMetadata extends MetadataElement implements Relation {

	private String documentation;
	private String type;
	private String multiplicity;
	private String table;

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
                    MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
			table = metadataRepository.getEntity( type ).getTable();
		}
		
		return table;
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Relation#getChildRelations()
	 */
	public Collection getChildRelations() {
	    MetadataRepository metadataRepository = 
                MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		return metadataRepository.getEntity( type ).getRelations().values();	
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Relation#getKeys()
	 */
	public Collection getKeys() {
	    MetadataRepository metadataRepository = 
                MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		Entity ed = metadataRepository.getEntity(type);
		Map idFieldMap = ed.getIdFields();
		Collection keyValues = idFieldMap.values();	
		return keyValues;
	}
	
	/**
	 * Executed to ensure that valid combinations of metadata have been loaded.
	 */
	public void validate() {
	}	
	
}
