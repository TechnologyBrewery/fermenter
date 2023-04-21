package org.technologybrewery.fermenter.ale.mda.generator.angular;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.technologybrewery.fermenter.mda.PackageManager;
import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.technologybrewery.fermenter.mda.metamodel.element.BaseFieldDecorator;
import org.technologybrewery.fermenter.mda.metamodel.element.Entity;
import org.technologybrewery.fermenter.mda.metamodel.element.Field;

import java.util.Map;

public class AngularField extends BaseFieldDecorator implements Field, AngularNamedElement {

    public AngularField(Field wrapped) {
        super(wrapped);
    }

    public String getAngularType() {
        String type = AngularGeneratorUtil.TYPE_NOT_FOUND;
        if (wrapped != null) {
            type = AngularGeneratorUtil.getAngularType(wrapped.getType());
        }
        return type;
    }

    /**
     * {@inheritDoc}
     */
    public String getUppercasedGenerator() {
        return getGenerator().name().toUpperCase();
    }

    public String getCapitalizedName() {
        return StringUtils.capitalize(getName());
    }
    
    public String getUppercasedType() {
        return getType().toUpperCase();
    }

    /**
     * {@inheritDoc}
     */
    public Boolean isEnumeration() {
        return getModelInstanceRepository().getEnumeration(getPackage(), getType()) != null;
    }

    public String getTypeLowerHyphen() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, getType());
    }

    Field getFieldObject() {
        return wrapped;
    }

    public boolean isExternal() {
        DefaultModelInstanceRepository metadataRepository = getModelInstanceRepository();
        String currentProject = metadataRepository.getArtifactId();
        String basePackage = PackageManager.getBasePackage(currentProject);
        Map<String, Entity> referenceEntities = metadataRepository.getEntities(getPackage());
        Entity referenceEntity = referenceEntities.get(getType());

        String currentPackage = referenceEntity.getPackage();
        return !StringUtils.isBlank(currentPackage) && !currentPackage.equals(basePackage);
    }

    public String getPatterns() {
        StringBuilder sb = new StringBuilder(100);
        boolean isFirst = true;
        for (String format : getValidation().getFormats()) {
            if (!isFirst) {
                sb.append(", ");
            }

            sb.append("\"");
            sb.append(StringEscapeUtils.escapeJava(format));
            sb.append("\"");

            isFirst = false;
        }

        return sb.toString();
    }

    static DefaultModelInstanceRepository getModelInstanceRepository() {
        return ModelInstanceRepositoryManager
            .getMetamodelRepository(DefaultModelInstanceRepository.class);
    }

}
