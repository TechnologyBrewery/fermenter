package org.bitbucket.fermenter.stout.mda.generator.entity;

import org.bitbucket.fermenter.stout.mda.java.JavaGeneratorUtil;

/**
 * Generator that processes a single file for each entity in the meta-model, regardless of whether the entity is
 * transient or persistent.
 */
public class EntityJavaGenerator extends AbstractJavaEntityGenerator {

    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
    }

    @Override
    protected boolean generatePersistentEntitiesOnly() {
        return false;
    }
}
