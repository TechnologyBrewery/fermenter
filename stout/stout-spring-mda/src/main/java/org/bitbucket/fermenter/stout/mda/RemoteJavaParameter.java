package org.technologybrewery.fermenter.stout.mda;

import org.technologybrewery.fermenter.mda.metamodel.element.Parameter;

/**
 * Decorates a {@link Parameter} with Java-specific capabilities that focus on treating the operation as a *remote* Java
 * class. For instance, one that references TransferObjects instead of BusinessObjects.
 */
public class RemoteJavaParameter extends JavaParameter {

    public RemoteJavaParameter(Parameter parameterToDecorate) {
        super(parameterToDecorate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImport() {
        return JavaElementUtils.getJavaImportByPackageAndType(getPackage(), getType(), false);

    }

}
