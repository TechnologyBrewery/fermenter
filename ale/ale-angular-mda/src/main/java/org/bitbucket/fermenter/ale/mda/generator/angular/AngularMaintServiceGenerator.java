package org.bitbucket.fermenter.ale.mda.generator.angular;

public class AngularMaintServiceGenerator extends AbstractAngularEntityGenerator {

    @Override
    protected boolean generatePersistentEntitiesOnly() {
        return true;
    }
}
