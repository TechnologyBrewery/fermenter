package org.bitbucket.fermenter.ale.mda.generator.angular;

import org.bitbucket.fermenter.mda.metamodel.element.BaseReturnDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Return;

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
        Boolean isMany = false;
        if(wrapped.isMany() != null) {
            isMany = wrapped.isMany();
        }
        return isMany;
    }
    
    public String getSignature() {
        String signature;
        if(isMany()) {
            signature = "Array<"+getAngularType()+">";
        } else {
            signature = getAngularType();
        }
        return signature;
    }
    
    public String getSignatureBase() {
        String signature;
        if(isMany()) {
            signature = "Array<"+getAngularTypeBase()+">";
        } else {
            signature = getAngularTypeBase();
        }
        return signature;
    }
}
