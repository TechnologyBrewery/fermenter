package org.bitbucket.fermenter.brett;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;

/**
 * Base abstract class that encapsulates common Python generation functionality.
 */
public abstract class AbstractPythonGenerator extends AbstractGenerator {

    /**
     * Context variable that describes the name of the Python package that aligns with the corresponding
     * Poetry project's name and will be used for publishing the package to PyPI
     */
    protected static final String PACKAGE_NAME = "packageName";

    /**
     * Normalized version of the Poetry package name, which may be safely used in import statements and
     * reflects the name of the top-level package folder at which this project is located
     */
    protected static final String PACKAGE_FOLDER_NAME = "packageFolderName";

    /**
     * Populates the given {@link VelocityContext} (creating a new one if the given parameter is {@code null})
     * with Python specific attributes that are commonly used in Python-specific templates.
     *
     * @param velocityContext
     * @param generationContext
     * @return
     */
    protected VelocityContext populateCommonPythonContext(VelocityContext velocityContext, GenerationContext generationContext) {
        if (velocityContext == null) {
            velocityContext = getNewVelocityContext(generationContext);
        }

        velocityContext.put(PACKAGE_NAME, generationContext.getArtifactId());
        velocityContext.put(PACKAGE_FOLDER_NAME, generationContext.getBasePackage());

        return velocityContext;
    }
}
