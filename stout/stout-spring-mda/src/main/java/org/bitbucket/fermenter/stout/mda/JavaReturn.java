package org.bitbucket.fermenter.stout.mda;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.metamodel.element.BaseReturnDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Return;

/**
 * Decorates a {@link Return} with Java-specific capabilities.
 */
public class JavaReturn extends BaseReturnDecorator implements Return, JavaPackagedElement {

    /**
     * Creates a new Java-specific decorator.
     * 
     * @param returnToDecorate
     *            instance to decorate
     */
    public JavaReturn(Return returnToDecorate) {
        super(returnToDecorate);
    }

    /**
     * Default to UTF-8 if not specified.
     * 
     * {@inheritDoc}
     */
    @Override
    public String getResponseEncoding() {
        String responseEncoding = super.getResponseEncoding();
        return StringUtils.isBlank(responseEncoding) ? StandardCharsets.UTF_8.name() : responseEncoding;
    }

    /**
     * Returns the original type of return object, as specified in metadata, in its Java form.
     * 
     * @return The Java version of the return type
     */
    public String getJavaType() {
        return JavaElementUtils.getJavaShortNameByPackageAndType(getPackage(), getType());
    }

}
