package org.tigris.atlas.mda.element.objectivec;

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
import org.tigris.atlas.mda.objectivec.ObjectiveCTypeManager;

public class ObjectiveCService implements Service {

	private Service service;
	private Map<String, Operation> decoratedOperationMap;
	private Set imports;

	public ObjectiveCService(Service serviceToDecorate) {
		if (serviceToDecorate == null) {
			throw new IllegalArgumentException("ObjectiveCService must be instatiated with a non-null service!");
		}
		service = serviceToDecorate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return ObjectiveCTypeManager.getObjectiveCClassPrefix() + service.getName();
	}

	public String getWrappedName() {
		return service.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDocumentation() {
		return service.getDocumentation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	@Override
	public Map<String, Operation> getOperations() {
		if (decoratedOperationMap == null) {
			Map<String, Operation> serviceOperationMap = service.getOperations();
			if ((serviceOperationMap == null) || (serviceOperationMap.size() == 0)) {
				decoratedOperationMap = Collections.emptyMap();

			} else {
				decoratedOperationMap = new HashMap<String, Operation>((int)(serviceOperationMap.size() * 1.25  + 1));
				for (Operation o : serviceOperationMap.values()) {
					decoratedOperationMap.put(o.getName(), new ObjectiveCOperation(o));

				}

			}
		}

		return decoratedOperationMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Operation getOperation(String name) {
		Map<String, Operation> decoratedMap = getOperations();
		return decoratedMap.get(name);
	}

	public Set getOperationImports() {
		Set importSet = new HashSet();

		ObjectiveCOperation operation;
		Map operationCollection = getOperations();
		Iterator operationIterator = operationCollection.values().iterator();
		while (operationIterator.hasNext()) {
			operation = (ObjectiveCOperation)operationIterator.next();
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
