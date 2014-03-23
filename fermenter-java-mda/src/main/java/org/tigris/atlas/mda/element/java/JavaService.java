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
	private Map<String, Operation> decoratedOperationMap;
	private Set imports;

	public JavaService(Service serviceToDecorate) {
		if (serviceToDecorate == null) {
			throw new IllegalArgumentException("JavaService must be instatiated with a non-null service!");
		}
		service = serviceToDecorate;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return service.getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getDocumentation() {
		return service.getDocumentation();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getApplicationName() {
		return service.getApplicationName();
	}
	
	public boolean hasManyParameters() {
		for (Operation op : getOperations().values()) {
			for (Parameter param : op.getParameters()) {
				if (param.isMany()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, Operation> getOperations() {
		if (decoratedOperationMap == null) {
			Map<String, Operation> serviceOperationMap = service.getOperations();
			if ((serviceOperationMap == null) || (serviceOperationMap.size() == 0)) {
				decoratedOperationMap = Collections.emptyMap();

			} else {
				decoratedOperationMap = new HashMap<String, Operation>((int)(serviceOperationMap.size() * 1.25  + 1));
				for (Operation o : serviceOperationMap.values()) {
					decoratedOperationMap.put(o.getName(), new JavaOperation(o));

				}

			}
		}

		return decoratedOperationMap;
	}

	/**
	 * {@inheritDoc}
	 */
	public Operation getOperation(String name) {
		Map<String, Operation> decoratedMap = getOperations();
		return decoratedMap.get(name);
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
