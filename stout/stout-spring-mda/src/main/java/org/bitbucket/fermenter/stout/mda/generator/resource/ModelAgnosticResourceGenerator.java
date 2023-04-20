package org.technologybrewery.fermenter.stout.mda.generator.resource;

import org.technologybrewery.fermenter.mda.generator.AbstractModelAgnosticGenerator;
import org.technologybrewery.fermenter.stout.mda.java.JavaGeneratorUtil;

public class ModelAgnosticResourceGenerator extends AbstractModelAgnosticGenerator {

    @Override
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_RESOURCES;
    }

}
