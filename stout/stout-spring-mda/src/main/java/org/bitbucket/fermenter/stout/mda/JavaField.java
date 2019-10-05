package org.bitbucket.fermenter.stout.mda;


import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.bitbucket.fermenter.mda.metadata.AbstractMetadataRepository;
import org.bitbucket.fermenter.mda.metadata.FormatMetadataManager;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Format;
import org.bitbucket.fermenter.mda.metadata.element.Pattern;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.MetamodelType;
import org.bitbucket.fermenter.mda.metamodel.element.Validation;

public class JavaField implements Field {

    private static Log log = LogFactory.getLog(Field.class);
    private static Integer DEFAULT_SCALE = 5;

    private Field field;
    private String importName;

    /**
     * Create a new instance of {@link Field} with the correct functionality set to generate Java code.
     * 
     * @param fieldToDecorate
     *            The {@link Field} to decorate
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
    public Generator getGenerator() {
        return field.getGenerator();
    }

    /**
     * {@inheritDoc}
     */
    public String getUppercasedGenerator() {
        return field.getGenerator().toString().toUpperCase();
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
    public String getPackage() {
        return field.getPackage();
    }

    /**
     * {@inheritDoc}
     */
    public Validation getValidation() {
        return field.getValidation();
    }

    public Integer getMaxLength() {
        return (getValidation() != null) ? getValidation().getMaxLength() : null;
    }

    public boolean hasMaxLength() {
        return getMaxLength() != null ? true : false;
    }

    public Integer getMinLength() {
        return (getValidation() != null) ? getValidation().getMinLength() : null;
    }

    public boolean hasMinLength() {
        return getMinLength() != null ? true : false;
    }

    public String getMaxValue() {
        return (getValidation() != null) ? getValidation().getMaxValue() : null;
    }

    public boolean hasMaxValue() {
        return getMaxValue() != null ? true : false;
    }

    public String getMinValue() {
        return (getValidation() != null) ? getValidation().getMinValue() : null;
    }

    public boolean hasMinValue() {
        return getMinValue() != null ? true : false;
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
        return field.isRequired() != null ? field.isRequired() : false;
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
        return (field.getGenerator() != null) ? true : false;
    }

    /**
     * {@inheritDoc}
     */
    public String getDefaultValue() {
        return field.getDefaultValue();
    }

    public boolean hasDefaultValue() {
        return field.getDefaultValue() != null;
    }

    @Override
    public String getFileName() {
        return field.getFileName();
    }

    @Override
    public void validate() {
        field.validate();
    }

    // java-specific generation methods:

    public String getJavaType() {

        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(DefaultModelInstanceRepository.class);
       
        return JavaElementUtils.getJavaType(metadataRepository.getArtifactId(), getType());
    }

    public String getImport() {
        if (importName == null) {

            AbstractMetadataRepository metadataRepository = ModelInstanceRepositoryManager
                    .getMetadataRepostory(MetadataRepository.class);
            importName = JavaElementUtils.getJavaImportType(metadataRepository.getArtifactId(), getType());

        }

        return importName;
    }

    public boolean isEntity() {
        return MetamodelType.ENTITY.equals(MetamodelType.getMetamodelType(getPackage(), getType()));
    }

    // TODO FormatMetadataManager needs to to be replaced by metamodel when this is implemented
    public String getPatterns() {
        StringBuilder sb = new StringBuilder();
        if (hasFormat()) {
            Format format = FormatMetadataManager.getInstance().getFormat(field.getValidation().getFormat());
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
        }

        return sb.toString();

    }

}
