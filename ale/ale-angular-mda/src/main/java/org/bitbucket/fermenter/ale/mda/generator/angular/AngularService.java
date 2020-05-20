package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bitbucket.fermenter.mda.metamodel.element.BaseServiceDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Operation;
import org.bitbucket.fermenter.mda.metamodel.element.Service;

public class AngularService extends BaseServiceDecorator implements Service, AngularNamedElement {


    public AngularService(Service service) {
        super(service);
    }
        
    public List<AngularOperation> getAngularOperations() {
        List<AngularOperation> wrappedOperations = new ArrayList<>();
        for (Operation operation : wrapped.getOperations()) {
            AngularOperation wrappedOperation = new AngularOperation(operation);
            wrappedOperations.add(wrappedOperation);
        }
        
        return wrappedOperations;
    }

    public List<AngularImport> getImports() {
        Map<String, AngularImport> imports = new HashMap<>();
        for(AngularOperation operation : getAngularOperations()) {
            List<AngularImport> importsFromOperation = operation.getImports();
            for(AngularImport importFromOperation : importsFromOperation) {
                if(!imports.containsKey(importFromOperation.getType())) {
                    imports.put(importFromOperation.getType(), importFromOperation);
                }
            }
        }
        return new ArrayList<>(imports.values());
    }
}
