package org.bitbucket.fermenter.mda.metadata.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.AbstractMetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

@Deprecated
public class ReferenceMetadata extends MetadataElement implements Reference {

	private String type;
	private String project;
	private String label;
	private String name;
	private String documentation;
	private String required;
	private List   foreignKeys;
	private Map    fkOverrides = new HashMap();

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Reference#getType()
	 */
	@Override
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
	 * @see org.bitbucket.fermenter.mda.metadata.Reference#getLabel()
	 */
	@Override
	public String getLabel() {
		if( label == null ) {
			label = StringUtils.capitalize(getName());
		}

		return label;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Reference#getName()
	 */
	@Override
	public String getName() {
		if( name == null ) {
			name = StringUtils.uncapitalize( type );
		}

		return name;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Reference#getDocumentation()
	 */
	@Override
	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Reference#getForeignKeyFields()
	 */
	@Override
	public List getForeignKeyFields() {
		if( foreignKeys == null ) {
			foreignKeys = new ArrayList();
			MetadataRepository metadataRepository = 
                    ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class);
			Entity ed = metadataRepository.getEntity(type);
			if (ed == null) {
				throw new NullPointerException("Reference to '" + type + "' not found!");
			}

			if( fkOverrides.size() == 0 ) {
				Iterator fks = ed.getIdFields().values().iterator();
				Field    fk  = null;
				while( fks.hasNext() ) {
					fk = (Field) fks.next();
					FieldMetadata newId    = new FieldMetadata();
					newId.setType( fk.getType() );
					newId.setName( fk.getName() );
					newId.setColumn( ed.getTable() + "_" + fk.getColumn() );
					newId.setMaxLength( fk.getMaxLength() );
					newId.setMinLength( fk.getMinLength() );
					foreignKeys.add( newId );
				}
			}
			else {
			// Apply overrides
				Iterator ids = ed.getIdFields().values().iterator();
				Field    id  = null;
				while( ids.hasNext() ) {
					id = (Field) ids.next();
					FieldMetadata newId    = new FieldMetadata();
					Field override = (Field) fkOverrides.get( id.getName() );
					newId.setType( id.getType() );
					newId.setSourceName( StringUtils.capitalize( id.getName() ) );
					newId.setName( override.getName() );
					newId.setColumn( override.getColumn() );
					newId.setMaxLength( id.getMaxLength() );
					newId.setMinLength( id.getMinLength() );
					foreignKeys.add( newId );
				}
			}
		}

		return foreignKeys;
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
	 * @see org.bitbucket.fermenter.mda.metadata.Reference#getRequired()
	 */
	@Override
	public String getRequired() {
		return required;
	}

	/**
	 * @param required The required to set.
	 */
	public void setRequired(String required) {
		this.required = required;
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Reference#isRequired()
	 */
	@Override
	public boolean isRequired() {
		if( required == null ) {
			return false;
		}
		else {
			return required.equalsIgnoreCase( Boolean.TRUE.toString() );
		}
	}

	/**
	 * Executed to ensure that valid combinations of metadata have been loaded.
	 */
	@Override
	public void validate() {
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.Reference#getProject()
	 */
	@Override
	public String getProject() {
	    AbstractMetadataRepository metadataRepository = 
                ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		return (StringUtils.isNotBlank(project)) ? project : metadataRepository.getApplicationName();
	}

	public void setProject(String project) {
		this.project = project;
	}

}
