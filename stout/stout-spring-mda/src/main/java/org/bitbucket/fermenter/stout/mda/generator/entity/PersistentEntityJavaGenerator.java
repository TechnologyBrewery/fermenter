package org.bitbucket.fermenter.stout.mda.generator.entity;

/**
 * Generates only those entities that are persistent (i.e., transient = false).
 */
public class PersistentEntityJavaGenerator extends EntityJavaGenerator {

    @Override
    protected boolean generatePersistentEntitiesOnly() {
        return true;
    }

}
