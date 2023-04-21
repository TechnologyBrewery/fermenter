package org.technologybrewery.fermenter.stout.mda;

import org.technologybrewery.fermenter.mda.metamodel.element.BaseParameterDecorator;
import org.technologybrewery.fermenter.mda.metamodel.element.MetamodelType;
import org.technologybrewery.fermenter.mda.metamodel.element.Parameter;

/**
 * Decorates a {@link Parameter} with Java-specific capabilities.
 */
public class JavaParameter extends BaseParameterDecorator implements Parameter, JavaPackagedElement {

    protected String javaImport;
    protected String javaType;
    protected String signatureName;
    protected String signatureSuffix;

    /**
     * {@inheritDoc}
     */
    public JavaParameter(Parameter parameterToDecorate) {
        super(parameterToDecorate);
    }

    /**
     * Returns the java "short" class name for this parameter (e.g., BigDecimal).
     * 
     * @return java short class name
     */
    public String getJavaType() {
        if (javaType == null) {
            javaType = JavaElementUtils.getJavaShortName(getImport());
        }
        return javaType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImport() {
        if (javaImport == null) {
            javaImport = JavaElementUtils.getJavaImportByPackageAndType(getPackage(), getType());
        }
        return javaImport;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentation() {
        return JavaElementUtils.formatForJavadoc(super.getDocumentation());
    }

    /**
     * Determines whether or not the parameter is an entity.
     * 
     * @return is entity?
     */
    public boolean isEntity() {
        return MetamodelType.ENTITY.equals(MetamodelType.getMetamodelType(getPackage(), getType()));
    }

    /**
     * Determines whether or not the parameter is an enumeration.
     * 
     * @return is enumeration?
     */
    public boolean isEnumeration() {
        return MetamodelType.ENUMERATION.equals(MetamodelType.getMetamodelType(getPackage(), getType()));
    }

    /**
     * The name of the method signature.
     * 
     * @return signature name
     */
    public String getSignatureName() {
        if (signatureName != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(getName()).append(getSignatureSuffix());
            signatureName = sb.toString();
        }

        return signatureName;
    }

    /**
     * A signature suffix (e.g., Impl).
     * 
     * @return Returns the signatureSuffix
     */
    public String getSignatureSuffix() {
        return (signatureSuffix != null) ? signatureSuffix : "";
    }

    /**
     * Sets the signature suffix (e.g., Impl).
     * 
     * @param signatureSuffix
     *            The signatureSuffix to set.
     */
    public void setSignatureSuffix(String signatureSuffix) {
        this.signatureSuffix = signatureSuffix;
    }

}
