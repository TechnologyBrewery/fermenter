package org.bitbucket.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.PackageManager;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Field;
import org.bitbucket.fermenter.mda.metamodel.element.Reference;

public class JavaReference implements Reference {

    private Reference reference;
    private List<Field> decoratedForeignKeyFieldList;

    /**
     * Create a new instance of <tt>Reference</tt> with the correct functionality set to generate Java code
     * 
     * @param referenceToDecorate
     *            The <tt>Reference</tt> to decorate
     */
    public JavaReference(Reference referenceToDecorate) {
        if (referenceToDecorate == null) {
            throw new IllegalArgumentException("JavaReferences must be instatiated with a non-null reference!");
        }
        reference = referenceToDecorate;
    }

    public String getType() {
        return reference.getType();
    }

    public String getName() {
        return reference.getName();
    }

    public String getDocumentation() {
        return reference.getDocumentation();
    }

    public String getCapitalizedName() {
        return StringUtils.capitalize(reference.getName());
    }

    public Boolean isRequired() {
        return reference.isRequired();
    }

    /**
     * {@inheritDoc}
     */
    public String getPackage() {
        return reference.getPackage();
    }

    /**
     * {@inheritDoc}
     */
    public String getFileName() {
        return reference.getFileName();
    }

    /**
     * {@inheritDoc}
     */
    public void validate() {
        reference.validate();
    }

    /**
     * {@inheritDoc}
     */
    public String getLocalColumn() {
        return reference.getLocalColumn();
    }

    public String getUncapitalizedType() {
        return StringUtils.uncapitalize(getType());
    }

    public List<Field> getForeignKeyFields() {
        if (decoratedForeignKeyFieldList == null) {
            List<Field> referenceForeignKeyFieldList = reference.getForeignKeyFields();
            if ((referenceForeignKeyFieldList == null) || (referenceForeignKeyFieldList.size() == 0)) {
                decoratedForeignKeyFieldList = Collections.emptyList();

            } else {
                Field f;
                decoratedForeignKeyFieldList = new ArrayList<>((int) (referenceForeignKeyFieldList.size()));
                Iterator<Field> i = referenceForeignKeyFieldList.iterator();
                while (i.hasNext()) {
                    f = (Field) i.next();
                    decoratedForeignKeyFieldList.add(new JavaField(f));
                }
            }
        }

        return decoratedForeignKeyFieldList;
    }

    public String getImport() {
        if (isExternal()) {
            return JavaElementUtils.getJavaImportByPackageAndType(getPackage(), getType(), false);
        } else {
            return JavaElementUtils.getJavaImportByPackageAndType("", getType());
        }
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

    public String getImportPrefix() {
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(DefaultModelInstanceRepository.class);

        Map<String, Entity> referenceEntities = metadataRepository.getEntities(getPackage());
        Entity referenceEntity = referenceEntities.get(getType());
        String currentPackage = referenceEntity.getPackage();
        return StringUtils.isBlank(currentPackage)
                ? PackageManager.getBasePackage(metadataRepository.getArtifactId())
                : currentPackage;
    }

    /**
     * 
     * 
     * @return
     */
    public Set getFkImports() {
        Set importSet = new HashSet();

        if (!isExternal()) {
            return importSet;
        }

        JavaField fk;
        Iterator fks = getForeignKeyFields().iterator();
        while (fks.hasNext()) {
            fk = (JavaField) fks.next();
            importSet.add(fk.getImport());
        }

        return importSet;
    }

    public String getFkCondition() {
        if (!isExternal()) {
            return null;
        }

        StringBuffer sb = new StringBuffer(100);
        JavaField fk;
        Iterator fks = getForeignKeyFields().iterator();
        while (fks.hasNext()) {
            fk = (JavaField) fks.next();
            sb.append("get");
            sb.append(getCapitalizedName());
            sb.append(fk.getCapitalizedName());
            sb.append("() == null");
            if (fks.hasNext()) {
                sb.append("&&");
            }
        }

        return sb.toString();
    }

}
