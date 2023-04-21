package org.technologybrewery.fermenter.ale.mda.generator.angular;

import org.technologybrewery.fermenter.mda.metamodel.element.BaseReturnDecorator;
import org.technologybrewery.fermenter.mda.metamodel.element.Return;

public class AngularReturn extends BaseReturnDecorator implements Return {

    public AngularReturn(Return returnToDecorate) {
        super(returnToDecorate);
    }

    public String getAngularType() {
        return AngularGeneratorUtil.getAngularType(getType());
    }

    public String getAngularTypeBase() {
        String type;
        if (!isBaseType()) {
            type = getType() + "Base";
        } else {
            type = getAngularType();
        }
        return type;
    }

    public Boolean isBaseType() {
        return AngularGeneratorUtil.isBaseType(getType());
    }

    @Override
    public Boolean isMany() {
        return wrapped.isMany() != null && wrapped.isMany();
    }

    public String getSignature() {
        String signature;
        if (isMany() && !isPagedResponse()) {
            signature = "Array<" + getAngularType() + ">";
        } else if (isPagedResponse()) {
            signature = "PageWrapper<" + getAngularType() + ">";
        } else if (getType().equals("void")) {
            signature = "{}";
        } else {
            signature = getAngularType();
        }

        return signature;
    }

    public String getSignatureBase() {
        String signature;
        if (isMany()) {
            signature = "Array<" + getAngularTypeBase() + ">";
        } else {
            signature = getAngularTypeBase();
        }
        return signature;
    }
}
