package org.bitbucket.fermenter.stout.mda;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.metadata.AbstractMetadataRepository;
import org.bitbucket.fermenter.mda.metadata.FormatMetadataManager;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Format;
import org.bitbucket.fermenter.mda.metadata.element.Pattern;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Enumeration;

public class JavaField implements Field {
	
    private static Log log = LogFactory.getLog(Field.class);
    private static String DEFAULT_SCALE = "5";
    
	private Field field;
	private String importName;
	
	/**
	 * Create a new instance of {@link Field} with the correct functionality set 
	 * to generate Java code.
	 * @param fieldToDecorate The {@link Field} to decorate
	 */
	public JavaField(Field fieldToDecorate) {
		if (fieldToDecorate == null) {
			throw new IllegalArgumentException("JavaFields must be instantiated with a non-null field!");
		}
		field = fieldToDecorate;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getColumn() {
		return field.getColumn();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getGenerator() {
		return field.getGenerator();
	}
	
    /**
     * {@inheritDoc}
     */
    public String getUppercasedGenerator() {
        return field.getGenerator().toUpperCase();
    }	

	/**
	 * {@inheritDoc}
	 */
	public String getLabel() {
		return field.getLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return field.getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getDocumentation() {
		return field.getDocumentation();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		return field.getType();
	}

	public String getCapitalizedName() {
		return StringUtils.capitalize(getName());
	}

	public String getUppercasedName() {
		return field.getName().toUpperCase();
	}

	public String getUppercasedType() {
		return field.getType().toUpperCase();
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isSimpleType() {
		return field.isSimpleType();
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean isEnumerationType() {
		return field.isEnumerationType();
	}

	/**
	 * {@inheritDoc}
	 */
	public Enumeration getEnumeration() {
		Enumeration e = field.getEnumeration();
		return (e != null) ? new JavaEnumeration(e) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMaxLength() {
		return field.getMaxLength();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasMaxLength() {
		return field.hasMaxLength();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMinLength() {
		return field.getMinLength();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasMinLength() {
		return field.hasMinLength();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMaxValue() {
		return field.getMaxValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasMaxValue() {
		return field.hasMaxValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getMinValue() {
		return field.getMinValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasMinValue() {
		return field.hasMinValue();
	}

	public String getRequired() {
		return field.getRequired();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRequired() {
		return field.isRequired();
	}

    /**
     * {@inheritDoc}
     */
    public boolean isTransient() {
        return field.isTransient();
    }
 
	/**
	 * {@inheritDoc}
	 */
	public String getSourceName() {
		return field.getSourceName();
	}
	
	//java-specific generation methods:
	
	public String getJavaType() {
		AbstractMetadataRepository metadataRepository = 
                ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		return JavaElementUtils.getJavaType(metadataRepository.getApplicationName(), getType());
	}
	
	public boolean isEntity() {
		MetadataRepository metadataRepository = 
                ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		return metadataRepository.getEntity(getProject(), getType() ) != null;
	}
	
	public String getImport() {
		if (importName == null ) {
			if (isExternal()) {
				importName = JavaElementUtils.getJavaImportType(getProject(), getType());
			} else {
				AbstractMetadataRepository metadataRepository = 
		                ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class);
				importName = JavaElementUtils.getJavaImportType(metadataRepository.getApplicationName(), getType());	
			}
		}
		
		return importName;
	}
	
	Field getFieldObject() {
		return field;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getScale() {
	    if (field.hasScale()) {
	        return field.getScale();
	    }else {
	        return DEFAULT_SCALE;
	    }

	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasScale() {
		return field.hasScale();
	}
	
    /**
     * {@inheritDoc}
     */
    public boolean hasGenerator() {
        if (log.isDebugEnabled()) {
            log.debug("Field " +  field.getName() + " has a generator value of " + field.getGenerator());
        }
        return StringUtils.isNotBlank(field.getGenerator());
    }	

	/**
	 * {@inheritDoc}
	 */
	public String getProject() {
		return field.getProject();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isExternal() {
		return field.isExternal();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getFormat() {
		return field.getFormat();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasFormat() {
		return field.hasFormat();
	}
	
	public String getPatterns() {
		Format format = FormatMetadataManager.getInstance().getFormat(getFormat());
			
		StringBuilder sb = new StringBuilder();
		int current = 0;
		int length = format.getPatterns().size();
		sb.append("\"");
		for (Pattern pattern : format.getPatterns()) {
		    current++;
			sb.append(StringEscapeUtils.escapeJava(pattern.getText()));
			
			if (current < length) {
				sb.append("|");
			}
		}
		sb.append("\"");
		
		return sb.toString();
		
	}

	public boolean isGeospatialType() {
		return isSimpleType() && getType().startsWith("geospatial_");
	}
	
	/**
     * {@inheritDoc}
     */
    @Override
    public Object getDefaultValue() {
        return field.getDefaultValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDefaultValue() {
        return (getDefaultValue() != null);
    }
}
