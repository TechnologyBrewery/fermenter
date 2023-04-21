package org.technologybrewery.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringEscapeUtils;
import org.technologybrewery.fermenter.mda.metamodel.element.BaseFieldDecorator;
import org.technologybrewery.fermenter.mda.metamodel.element.Field;

/**
 * Decorates a {@link Field} with convenience methods for Java code generation.
 */
public class JavaField extends BaseFieldDecorator implements Field, JavaNamedElement {

    private String importName;

    /**
     * {@inheritDoc}
     */
    public JavaField(Field wrapped) {
        super(wrapped);

    }

    /**
     * Returns the Field's generator name that has been upper cased.
     * 
     * @return upper cased generator name
     */
    public String getUppercasedGenerator() {
        return wrapped.getGenerator().toString().toUpperCase();
    }

    /**
     * Returns the Java short name for this field (e.g., String).
     * 
     * @return Java short name
     */
    public String getJavaType() {
        return JavaElementUtils.getJavaShortNameByPackageAndType(getPackage(), getType());
    }

    /**
     * Returns the Java import for this field (e.g., java.lang.String).
     * 
     * @return Java short name
     */
    public String getImport() {
        if (importName == null) {
            importName = JavaElementUtils.getJavaImportByPackageAndType(getPackage(), getType());
        }

        return importName;
    }

    /**
     * Escapes values in provided regex values (e.g., "\d" becomes "\\d").
     * 
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getFormats() {
        Collection<String> escapedFormats = new ArrayList<>();
        for (String rawFormat : super.getFormats()) {
            String escapedFormat = StringEscapeUtils.escapeJava(rawFormat);
            escapedFormats.add(escapedFormat);
        }
        return escapedFormats;
    }

    /**
     * Returns the patterns as a pre-formatted Java validation string.
     * 
     * @return pre-formatted Pattern or Pattern.List
     */
    public String getPatternsAsValidationString() {
        StringBuilder patternString = new StringBuilder();
        patternString.append("@Pattern(regexp=\"");

        boolean isFirst = true;
        for (String format : getFormats()) {
            if (!isFirst) {
                patternString.append("|");
            }
            patternString.append(format);
            isFirst = false;

        }

        patternString.append("\")");

        return patternString.toString();
    }

}
