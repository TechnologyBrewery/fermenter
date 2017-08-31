package org.bitbucket.fermenter.stout.mda.generator.web;

import org.bitbucket.fermenter.stout.mda.generator.resource.ModelAgnosticResourceGenerator;

public class WebappResourceGenerator extends ModelAgnosticResourceGenerator {

    @Override
    protected String getOutputSubFolder() {
        return "webapp/";
    }

}
