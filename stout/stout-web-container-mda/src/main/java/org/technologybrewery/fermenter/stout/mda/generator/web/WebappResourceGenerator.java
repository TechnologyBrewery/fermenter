package org.technologybrewery.fermenter.stout.mda.generator.web;

import org.technologybrewery.fermenter.stout.mda.generator.resource.ModelAgnosticResourceGenerator;

public class WebappResourceGenerator extends ModelAgnosticResourceGenerator {

    @Override
    protected String getOutputSubFolder() {
        return "webapp/";
    }

}
