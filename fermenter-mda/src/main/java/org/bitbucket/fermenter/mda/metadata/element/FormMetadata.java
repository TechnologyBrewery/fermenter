package org.bitbucket.fermenter.mda.metadata.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;

/**
 * Java representation of system input form metadata
 *
 * @author sandrews
 *
 */
public class FormMetadata extends MetadataElement implements Form {

	private String 	name;
	private String applicationName;
	private Map entities = new TreeMap();
	private Set orderedEntityNames = new TreeSet();
	private List formOnlyFields = new ArrayList();

	public FormMetadata() {
		super();
	}
	
	public void validate() {
		for (Iterator i=entities.values().iterator(); i.hasNext();) {
			FormEntityMetadata fem = (FormEntityMetadata) i.next();
			resolveTypes(fem);
			fem.validate();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public void addEntity(FormEntityMetadata ed) {
		entities.put( ed.getName(), ed );
		orderedEntityNames.add(ed.getName());
	}
	
	public void addFormOnlyField(FormFieldMetadata field) {
		formOnlyFields.add(field);
	}
	
	public Collection getFormOnlyFields() {
		return Collections.unmodifiableList(formOnlyFields);
	}

	public Set getEntityNames() {
		return orderedEntityNames;
	}

	public Collection getEntities() {
		return entities.values();
	}

	public Collection getFields(String entityName) {
		return (Collection) entities.get( entityName );
	}

	/**
	 * Set the form entity types from the associated entity metadata
	 * @param ed
	 */
	private void resolveTypes(FormEntityMetadata ed) {
		Entity entityDescriptor = null;
			
			entityDescriptor = (Entity)MetadataRepository.getInstance().getAllEntities().get(ed.getType());
			
		if (entityDescriptor == null) {
			throw new IllegalArgumentException("Could not find entity '" + ed.getType() + "' from project '"  + "'");
		}
		
		Field field = null;
		for( Iterator fields = ed.getFields().iterator(); fields.hasNext();  ) {
			field= (Field) fields.next();
			String fn = field.getName();
			Field f = entityDescriptor.getField( fn );
			
			// attempt to map to an id field
			if (f == null) {
				f = entityDescriptor.getIdField(fn);
				if (f != null && (f.getGenerator() == null || !f.getGenerator().equals("assigned"))) {
					throw new IllegalArgumentException("Cannot map to an id field unless the generator is 'assigned'");
				}
			}
			
			// Handle composite fields
			if (f == null) {
				f = ed.getField(field.getName());
			}
			
			// Couldn't find the field/id field
			if (f == null) {
				throw new IllegalArgumentException("Could not find field '" + fn + "' for entity '" + ed.getName() + "'");
			}
			
			
			// Composite fields are already set up
			if (StringUtils.isBlank(((FormFieldMetadata)field).getCompositeName())) {
				copyProperties(f, ((FormFieldMetadata)field));
				
				if (!f.isExternal()) {
					((FormFieldMetadata)field).setProject( ed.getProject() );	
				} else {
					((FormFieldMetadata)field).setProject( f.getProject() );
				}
			}
		}
	}
	
	private void copyProperties(Field src, FormFieldMetadata dest) {
		// For some reason BeanUtils doesn't copy everything
		dest.setType(src.getType());
		dest.setLabel(src.getLabel());
		dest.setRequired(src.getRequired());
		dest.setMinLength(src.getMinLength());
		dest.setMinValue(src.getMinValue());
		dest.setMaxLength(src.getMaxLength());
		dest.setMaxValue(src.getMaxValue());
		dest.setScale(src.getScale());
	}

}
