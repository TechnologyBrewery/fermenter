package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.Iterator;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.FormatMetadataManager;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Format;
import org.bitbucket.fermenter.mda.metadata.element.Pattern;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Enumeration;

public class AngularField implements Field {

    
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
        MetadataRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(MetadataRepository.class);
        return metadataRepository.getEntity(getProject(), getType()) != null;
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
    public Enumeration getEnumeration() {
        Enumeration e = field.getEnumeration();
        return e;
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
    public String getSourceName() {
        return field.getSourceName();
    }

    Field getFieldObject() {
        return field;
    }

    /**
     * {@inheritDoc}
     */
    public String getScale() {
        return field.getScale();
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

    public boolean isGeospatialType() {
        return isSimpleType() && getType().startsWith("geospatial_");
    }

}
