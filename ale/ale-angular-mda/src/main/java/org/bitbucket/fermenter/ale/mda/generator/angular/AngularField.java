package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.PackageManager;
import org.bitbucket.fermenter.mda.metadata.FormatMetadataManager;
import org.bitbucket.fermenter.mda.metadata.element.Format;
import org.bitbucket.fermenter.mda.metadata.element.Pattern;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.MetamodelType;
import org.bitbucket.fermenter.mda.metamodel.element.Validation;

import com.google.common.base.CaseFormat;

public class AngularField implements Field {

    private static Log log = LogFactory.getLog(AngularField.class);
    private static Integer DEFAULT_SCALE = 5;
    private Field field;

    public AngularField(Field fieldToDecorate) {
        if (fieldToDecorate == null) {
            throw new IllegalArgumentException("Fields must be instantiated with a non-null field!");
        }
        field = fieldToDecorate;
    }

    public String getAngularType() {
        String type = AngularGeneratorUtil.TYPE_NOT_FOUND;
        if(field != null) {
            type = AngularGeneratorUtil.getAngularType(field.getType());
        }
        return type;
    }

    public boolean isEntity() {
        return MetamodelType.ENTITY.equals(MetamodelType.getMetamodelType(getPackage(), getType()));
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
    public String getColumn() {
        return field.getColumn();
    }

    /**
     * {@inheritDoc}
     */
    public Generator getGenerator() {
        return field.getGenerator();
    }

    /**
     * {@inheritDoc}
     */
    public String getUppercasedGenerator() {
        return field.getGenerator().name().toUpperCase();
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

    /**
     * {@inheritDoc}
     */
    public Boolean isTransient() {
        return field.isTransient();
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
    public Boolean isEnumeration() {
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(DefaultModelInstanceRepository.class);
        boolean enumeration = metadataRepository.getEnumeration(field.getType()) != null;
        if (enumeration) 
                System.out.print("Enumeration ----> "+ field.getType() );
        
        return enumeration;
        
    }
    
    public String getTypeLowerHyphen() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, getType());
    }

    public Integer getMaxLength() {
        return (getValidation() != null) ? getValidation().getMaxLength() : null;
    }

    public boolean hasMaxLength() {
        return getMaxLength() != null;
    }

    public Integer getMinLength() {
        return (getValidation() != null) ? getValidation().getMinLength() : null;
    }

    public boolean hasMinLength() {
        return getMinLength() != null;
    }

    public String getMaxValue() {
        return (getValidation() != null) ? getValidation().getMaxValue() : null;
    }

    public boolean hasMaxValue() {
        return getMaxValue() != null;
    }

    public String getMinValue() {
        return (getValidation() != null) ? getValidation().getMinValue() : null;
    }

    public boolean hasMinValue() {
        return getMinValue() != null;
    }

    public Integer getScale() {
        return (hasScale()) ? getValidation().getScale() : DEFAULT_SCALE;
    }

    public boolean hasScale() {
        return getValidation() != null && getValidation().getScale() != null;
    }

    public boolean hasFormat() {
        return getValidation() != null && getValidation().getFormat() != null;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean isRequired() {
        return field.isRequired() != null && field.isRequired();
    }

    Field getFieldObject() {
        return field;
    }
    /**
     * {@inheritDoc}
     */
    public boolean hasGenerator() {
        if (log.isDebugEnabled()) {
            log.debug("Field " + field.getName() + " has a generator value of " + field.getGenerator());
        }
        return field.getGenerator() != null;
    }
    
    public boolean isExternal() {
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(DefaultModelInstanceRepository.class);
        String currentProject = metadataRepository.getArtifactId();
        String basePackage = PackageManager.getBasePackage(currentProject);
        Map<String, Entity> referenceEntities = metadataRepository.getEntities(getPackage());
        Entity referenceEntity = referenceEntities.get(getType());

        String currentPackage = referenceEntity.getPackage();
        return !StringUtils.isBlank(currentPackage) && !currentPackage.equals(basePackage);
    }


    public String getPatterns() {
        Format format = FormatMetadataManager.getInstance().getFormat(field.getValidation().getFormat());

        StringBuilder sb = new StringBuilder(100);
        for (Iterator<Pattern> i = format.getPatterns().iterator(); i.hasNext();) {
            Pattern pattern = i.next();

            sb.append("\"");
            sb.append(StringEscapeUtils.escapeJava(pattern.getText()));
            sb.append("\"");

            if (i.hasNext()) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

  

    @Override
    public String getFileName() {
        return field.getFileName();
    }

    @Override
    public void validate() {
       field.validate();
    }

    @Override
    public String getPackage() {
        return field.getPackage();
    }

    @Override
    public Validation getValidation() {
        return field.getValidation();
    }

    @Override
    public String getDefaultValue() {
        return field.getDefaultValue();
    }

    public boolean hasDefaultValue() {
        return field.getDefaultValue() != null;
    }
}
