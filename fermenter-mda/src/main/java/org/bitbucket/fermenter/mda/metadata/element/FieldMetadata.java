package org.bitbucket.fermenter.mda.metadata.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.metadata.FormatMetadataManager;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;

public class FieldMetadata extends MetadataElement implements Field {
	
	private String name;
	private String documentation;
	private String type;
	private String label;
	private String column;
	private String generator;
	private String maxLength;
	private String minLength;
	private String maxValue;
	private String minValue;
	private String scale;
	private String required;
	private String sourceName;
	private String project;
	private Boolean isSimpleType;	
	private Boolean isEnumerationType;
	private Enumeration enumeration;
	private String format;
	
	private static Log log = LogFactory.getLog(Field.class);
    
    private static final List<String> SIMPLE_TYPES_LIST;
    
    static {
        SIMPLE_TYPES_LIST = new ArrayList<>(Field.SIMPLE_TYPES.length);
        for (int i = 0; i < Field.SIMPLE_TYPES.length; i++) {
            SIMPLE_TYPES_LIST.add(Field.SIMPLE_TYPES[i]);
        }
    }

	/**
	 * {@inheritDoc}
	 */
	public String getColumn() {
		return column;
	}
	
	/**
	 * @param column The column to set.
	 */
	public void setColumn(String column) {
		this.column = column;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getScale() {
		return scale;
	}
	
	public void setScale(String scale) {
		this.scale = scale;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasScale() {
		return !StringUtils.isBlank(scale);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getGenerator() {
		if( generator == null ) {
			generator = ID_GENERATION_ASSIGNED;
		}
		
		return generator;
	}
	
	/**
	 * @param generator The generator to set.
	 */
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getLabel() {
		if( label == null ) {
			label = name;
		}
		
		return label;
	}
	
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getDocumentation() {
		return documentation;
	}
	
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
	 */
	public Boolean isSimpleType() {
		if (isSimpleType == null) {
			determineType();
		}
		return isSimpleType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isEnumerationType() {
		if (isEnumerationType == null) {
			determineType();
		}
		return isEnumerationType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Enumeration getEnumeration() {	
		return (isEnumerationType().booleanValue()) ? enumeration : null;
	}	
	
	private void determineType() {
		//determine which kind of type we are dealing with and store that info:
	    MetadataRepository metadataRepository = 
                MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		Object result = metadataRepository.getEnumeration(type);

		if (result != null) {
			isEnumerationType = Boolean.TRUE;
			isSimpleType = Boolean.FALSE;
			enumeration = (Enumeration)result;
		} else {
			isSimpleType = Boolean.TRUE;
			isEnumerationType = Boolean.FALSE;
		}
	}
	
	protected String getDefaultProject() {
	    MetadataRepository metadataRepository = 
                MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		return metadataRepository.getApplicationName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getMaxLength() {
		if ((maxLength == null) && (isEnumerationType().booleanValue())) {			
			String enumerationMaxLength = getEnumeration().getMaxLength();
			if (enumerationMaxLength != null) {
				maxLength = enumerationMaxLength;
				if (log.isDebugEnabled()) {
					log.debug("Defaulting field '" + name + "''s max length to '" + maxLength + 
						" based on the values of enumeration '" + getEnumeration().getName() + "'");
					
				}
			}
		}
		return maxLength;
	}
	
	/**
	 * @param maxSize The maxLength to set.
	 */
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasMaxLength() {
		return (getMaxLength() != null);
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public String getMinLength() {
		return minLength;
	}
	
	/**
	 * @param minLength The minLength to set.
	 */
	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasMinLength() {
		return (getMinLength() != null);
	}		
	
	/**
	 * {@inheritDoc}
	 */
	public String getMaxValue() {
		return maxValue;
	}
	
	/**
	 * @param maxValue The maxValue to set.
	 */
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasMaxValue() {
		return (getMaxValue() != null);
	}		
	
	/**
	 * {@inheritDoc}
	 */
	public String getMinValue() {
		return minValue;
	}
	
	/**
	 * @param minValue The minValue to set.
	 */
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasMinValue() {
		return (getMinValue() != null);
	}		
	
	/**
	 * {@inheritDoc}
	 */
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
	 * {@inheritDoc}
	 */
	public boolean isRequired() {
		if( required == null ) {
			return false;
		}
		else {
			return required.equalsIgnoreCase( Boolean.TRUE.toString() );
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getSourceName() {
		if( sourceName == null ) {
			sourceName = StringUtils.capitalize( name );
		}
		
		return sourceName;
	}
	
	/**
	 * @param overriddenName The overriddenName to set.
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasFormat() {
		return !StringUtils.isBlank(getFormat());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void validate() {
		validateType();
		validateLengthType();		
		validateValueType();
		validateFormat();
	}
	
	private void validateFormat() {
		if (!hasFormat()) {
			return;
		} else if (!TYPE_STRING.equals(getType())) {
			throw new IllegalStateException("Formats are only valid on fields of type string");
		}

		Format formatObj = FormatMetadataManager.getInstance().getFormat(getFormat());
		if (formatObj == null) {
			throw new IllegalArgumentException("Could not find formatter named: " + getFormat());
		}
	}
	
	private void validateType() {
		String typeToValidate = getType();	
		//check basic types:
		if (SIMPLE_TYPES_LIST.contains(typeToValidate)){
			if (isExternal()) {
				throw new IllegalStateException("Simple field '" + getName() + "' cannot specify an external project");
			}
		} else {
			//check enumeration type:
		    MetadataRepository metadataRepository = 
	                MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
			Enumeration e = metadataRepository.getEnumeration(typeToValidate);

			if (e == null) {
				// TODO - This needs to throw an exception or mark generation for failure
				// However, throwing one now will cause a problem when loading entity metadata from
				// a 'web' project, as the current application name from metadata manager will
				// return the 'web' project's name, and not the name of the project declaring the
				// field.  Need a way for fields to always know the project in which they were
				// defined, in addition to the current ability to have an external project
				log.warn("Field '" + getName() + "' must have a valid type!\n\tCurrent Type: *" + typeToValidate 
						+ "*\n\tValid Types: " + SIMPLE_TYPES_LIST);
			}
		}	
	}
	
	/**
	 * Checks value-to-type correlation.
	 */
	private void validateValueType() {
		String type = getType();
		if ((getMaxValue() != null) 
		|| ((getMinValue() != null))) {
			if ((TYPE_FLOAT.equals(type)) 
			|| (TYPE_LONG.equals(type))
			|| (TYPE_DOUBLE.equals(type))
			|| (TYPE_INTEGER.equals(type))
			|| (TYPE_BIG_DECIMAL.equals(type))
			|| (TYPE_SHORT.equals(type))) {
			} else {
				log.error("On field '" + getName() + "', minValue and maxValue can only be specified with types '" 
					+ TYPE_FLOAT + "', " + TYPE_LONG + "', " + TYPE_DOUBLE + "', "
					+ TYPE_INTEGER + "', " + TYPE_BIG_DECIMAL + "' or " + TYPE_SHORT + "'");
			}
		}
	}
	
	/**
	 * Checks length-to-type correlation.
	 */
	private void validateLengthType() {
		String type = getType();
		if ((maxLength != null) 
		|| ((minLength != null))) {
			if (!TYPE_STRING.equals(type)) {
				log.error("On field '" + getName() + "', minLength and maxLength can only be specified with type '" 
						+ TYPE_STRING + "'");
			}
		}
	}
	
	public String getProject() {
		return project;
	}
	
	public void setProject(String project) {
		this.project = project;
	}
	
	public boolean isExternal() {
		return !StringUtils.isBlank(getProject());
	}
}
