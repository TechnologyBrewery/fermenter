package org.tigris.atlas.mda.element.java;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.tigris.atlas.mda.metadata.element.Operation;
import org.tigris.atlas.mda.metadata.element.Parameter;
import org.tigris.atlas.mda.metadata.element.Service;

public class JavaService implements Service {


	private Service service;
	private Map decoratedOperationMap;
	private Set imports;

	public JavaService(Service serviceToDecorate) {
		if (serviceToDecorate == null) {
			throw new IllegalArgumentException("JavaService must be instatiated with a non-null service!");
		}
		service = serviceToDecorate;
	}

	public String getName() {
		return service.getName();
	}
	
	public String getDocumentation() {
		return service.getDocumentation();
	}
	
	/**
	 * @see org.tigris.atlas.mda.metadata.element.Composite#getApplicationName()
	 */
	public String getApplicationName() {
		return service.getApplicationName();
	}
	
	public boolean hasManyParameters() {
		for (Iterator i = getOperations().values().iterator(); i.hasNext();) {
			Operation op = (Operation) i.next();
			for (Iterator j = op.getParameters().iterator(); j.hasNext();) {
				Parameter param = (Parameter) j.next();
				if (param.isMany()) {
					return true;
				}
			}
		}
		return false;
	}

	public Map getOperations() {
		if (decoratedOperationMap == null) {
			Map serviceOperationMap = service.getOperations();
			if ((serviceOperationMap == null) || (serviceOperationMap.size() == 0)) {
				decoratedOperationMap = Collections.EMPTY_MAP;

			} else {
				Operation o;
				decoratedOperationMap = new HashMap((int)(serviceOperationMap.size() * 1.25  + 1));
				Iterator i = serviceOperationMap.values().iterator();
				while (i.hasNext()) {
					o = (Operation)i.next();
					decoratedOperationMap.put(o.getName(), new JavaOperation(o));

				}

			}
		}

		return decoratedOperationMap;
	}

	public Operation getOperation(String name) {
		Map decoratedMap = getOperations();
		return (Operation)decoratedMap.get(name);
	}

	public Set getOperationImports() {
		Set importSet = new HashSet();

		JavaOperation operation;
		Map operationCollection = getOperations();
		Iterator operationIterator = operationCollection.values().iterator();
		while (operationIterator.hasNext()) {
			operation = (JavaOperation)operationIterator.next();
			importSet.addAll(operation.getImports());
		}

		return importSet;
	}

	public Set getImports() {
		if (imports == null) {
			imports = new TreeSet();
			imports.addAll(getOperationImports());
		}

		return imports;
	}

	public String getBaseJndiName(String basePackage) {
		return (basePackage != null) ? basePackage.replace('.', '/') : "";
	}
	
}
