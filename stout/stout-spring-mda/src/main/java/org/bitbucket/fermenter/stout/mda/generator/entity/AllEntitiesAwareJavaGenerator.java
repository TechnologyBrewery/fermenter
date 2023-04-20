package org.technologybrewery.fermenter.stout.mda.generator.entity;

import org.technologybrewery.fermenter.mda.generator.entity.AbstractAllEntitiesAwareGenerator;
import org.technologybrewery.fermenter.stout.mda.java.JavaGeneratorUtil;

/**
 * Writes all entities in an ordered fashion (as specified via {@link AbstractAllEntitiesAwareGenerator}) to the java
 * output folder.
 */
public class AllEntitiesAwareJavaGenerator extends AbstractAllEntitiesAwareGenerator {

    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
    }

}
