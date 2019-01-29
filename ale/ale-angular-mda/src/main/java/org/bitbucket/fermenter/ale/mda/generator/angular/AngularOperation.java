package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.fermenter.mda.metamodel.element.BaseOperationDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Operation;
import org.bitbucket.fermenter.mda.metamodel.element.Parameter;
import org.bitbucket.fermenter.mda.metamodel.element.Return;
import org.bitbucket.fermenter.mda.metamodel.element.Transaction;

public class AngularOperation extends BaseOperationDecorator implements AngularNamedElement {

    public AngularOperation(Operation operationToDecorate) {
        super(operationToDecorate);
    }

    public List<AngularParameter> getAngularParameters() {
        List<AngularParameter> angularParams = new ArrayList<>();
        for (Parameter param : getParameters()) {
            angularParams.add(new AngularParameter(param));
        }
        return angularParams;
    }

    public String getSignatureParametersForAngular() {
        String signature = "";
        for (AngularParameter parameter : getAngularParameters()) {
            String angularType = parameter.getAngularType();
            if (parameter.isMany()) {
                angularType = "Array<" + angularType + ">";
            }
            signature += ", " + parameter.getName() + ": " + angularType;
        }
        // drop the first comma space
        if (signature.length() > 2) {
            signature = signature.substring(2);
        }
        return signature;
    }

    @Override
    public Return getReturn() {
        return new AngularReturn(wrapped.getReturn());
    }

    public List<AngularImport> getImports() {
        List<AngularImport> imports = new ArrayList<>();

        for (Parameter parameter : getParameters()) {
            if (!AngularGeneratorUtil.isBaseType(parameter.getType())) {
                imports.add(new AngularImport(parameter.getType(), parameter.getPackage()));
            }
        }

        if (!isResponseTypeVoid()) {
            if (!AngularGeneratorUtil.isBaseType(getReturn().getType())) {
                imports.add(new AngularImport(getReturn().getType(), getReturn().getPackage()));
            }
        }

        return imports;
    }
    
    public boolean usesEnumerations() {
        boolean usesEnumerations = false;
        for(AngularImport angularImport : getImports()) {
            if(angularImport.isEnumeration()) {
                usesEnumerations = true;
            }
        }
        return usesEnumerations;
    }

    public boolean isResponseTypeVoid() {
        return "void".equalsIgnoreCase(getReturn().getType()) ? Boolean.TRUE : Boolean.FALSE;
    }

    public boolean hasUrlParams() {
        boolean hasUrlParams = false;
        if (Transaction.SUPPORTS.equals(getTransactionAttribute()) && !getParameters().isEmpty()) {
            hasUrlParams = true;
        } else {
            for (AngularParameter param : getAngularParameters()) {
                if (!param.isEntity()) {
                    hasUrlParams = true;
                }
            }
        }
        return hasUrlParams;
    }

    public String getPostBodyParameterName() {
        String postBodyParameterName = "null";
        for (AngularParameter param : getAngularParameters()) {
            if (param.isEntity()) {
                postBodyParameterName = param.getName();
            }
        }
        return postBodyParameterName;
    }
    
    @Override
    public String getDocumentation() {
        return wrapped.getDocumentation() == null ? "NO DOCUMENTATION PROVIDED" : wrapped.getDocumentation();
    }
}
