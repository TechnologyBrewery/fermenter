package org.bitbucket.fermenter.stout.mda.generator.resource;

import org.bitbucket.fermenter.mda.generator.AbstractModelAgnosticGenerator;
import org.bitbucket.fermenter.stout.mda.java.JavaGeneratorUtil;

public class ModelAgnosticJavaGenerator extends AbstractModelAgnosticGenerator {

    @Override
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
    }

}
