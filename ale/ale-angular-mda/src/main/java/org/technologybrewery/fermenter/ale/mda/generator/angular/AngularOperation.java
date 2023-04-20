package org.technologybrewery.fermenter.ale.mda.generator.angular;

import java.util.ArrayList;
import java.util.List;

import org.technologybrewery.fermenter.mda.metamodel.element.BaseOperationDecorator;
import org.technologybrewery.fermenter.mda.metamodel.element.Operation;
import org.technologybrewery.fermenter.mda.metamodel.element.Parameter;
import org.technologybrewery.fermenter.mda.metamodel.element.Return;
import org.technologybrewery.fermenter.mda.metamodel.element.Transaction;

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

    public String getAngularMethodSignature() {
        String name = getNameLowerCamel();
        String parameters = getAngularParameterSignature();
        String returnType = getAngularReturn().getSignature();

        if (!isGETRestCall()) {
            returnType = "FermenterResponse<" + returnType + ">";
        }
        return name + "(" + parameters + "): Observable<" + returnType + ">";
    }

    public String getAngularParameterSignature() {
        String signature = "";
        for (AngularParameter parameter : getAngularParameters()) {
            String angularType = parameter.getAngularType();
            if (parameter.isMany()) {
                angularType = "Array<" + angularType + ">";
            }
            signature += ", " + parameter.getName() + ": " + angularType;
        }

        signature += ", skipGlobalErrorHandler = false";
        // drop the first comma space
        if (signature.length() > 2) {
            signature = signature.substring(2);
        }
        return signature;
    }

    private AngularReturn getAngularReturn() {
        return new AngularReturn(wrapped.getReturn());
    }

    @Override
    public Return getReturn() {
        return getAngularReturn();
    }

    public List<AngularImport> getImports() {
        List<AngularImport> imports = new ArrayList<>();

        for (Parameter parameter : getParameters()) {
            if (!AngularGeneratorUtil.isBaseType(parameter.getType())) {
                imports.add(new AngularImport(parameter.getType(), parameter.getPackage()));
            }
        }

        if (!isResponseTypeVoid() && !AngularGeneratorUtil.isBaseType(getReturn().getType())) {
            imports.add(new AngularImport(getReturn().getType(), getReturn().getPackage()));
        }

        return imports;
    }

    public boolean usesEnumerations() {
        boolean usesEnumerations = false;
        for (AngularImport angularImport : getImports()) {
            if (angularImport.isEnumeration()) {
                usesEnumerations = true;
            }
        }
        return usesEnumerations;
    }

    public boolean usesPaging() {
        boolean usesPaging = false;
        for (AngularImport angularImport : getImports()) {
            if ("PageWrapper".equals(angularImport.getType())) {
                usesPaging = true;
            }
        }
        return usesPaging;
    }

    public boolean isResponseTypeVoid() {
        return "void".equalsIgnoreCase(getReturn().getType()) ? Boolean.TRUE : Boolean.FALSE;
    }

    public boolean hasUrlParams() {
        boolean hasUrlParams = false;
        if (isGETRestCall() && !getParameters().isEmpty()) {
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

    public boolean isGETRestCall() {
        return Transaction.SUPPORTS.toString().equals(getTransactionAttribute());
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
        String newLine = "\n    * ";
        String methodDocumenation = wrapped.getDocumentation() == null ? "NO METHOD DOCUMENTATION PROVIDED"
                : wrapped.getDocumentation();

        StringBuilder parametersDocumentation = new StringBuilder();
        for (AngularParameter parameter : getAngularParameters()) {
            String parameterDocumentation = parameter.getAngularDocumentation();
            if(parameterDocumentation != null) {
                parametersDocumentation.append(newLine + parameterDocumentation);                
            }
        }

        return methodDocumenation + parametersDocumentation;
    }
}
