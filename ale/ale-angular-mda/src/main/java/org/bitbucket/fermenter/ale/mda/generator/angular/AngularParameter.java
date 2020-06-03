package org.bitbucket.fermenter.ale.mda.generator.angular;

import org.bitbucket.fermenter.mda.metamodel.element.BaseParameterDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Parameter;

public class AngularParameter extends BaseParameterDecorator {

    public AngularParameter(Parameter parameterToDecorate) {
        super(parameterToDecorate);
    }

    public boolean isEntity() {
        return !AngularGeneratorUtil.isBaseType(getType());
    }

    public String getAngularType() {
        return AngularGeneratorUtil.getAngularType(getType());
    }

    @Override
    public Boolean isMany() {
        return wrapped.isMany() != null && wrapped.isMany();
    }

    public String getAngularDocumentation() {
        if (wrapped.getDocumentation() != null) {
            return "@param " + getName() + " " + wrapped.getDocumentation();
        } else {
            return null;
        }
    }

}
