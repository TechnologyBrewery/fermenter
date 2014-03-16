package org.tigris.atlas.mda.element.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Entity;
import org.tigris.atlas.mda.metadata.element.Operation;
import org.tigris.atlas.mda.metadata.element.Parameter;

public class JavaOperation implements Operation {

	private static final String BUSINESS_OBJECT = "BO";
	public static final String PROPAGATION_REQUIRED = "REQUIRED";
	public static final String PROPAGATION_REQUIRES_NEW = "REQUIRES_NEW";
	public static final String PROPAGATION_MANDATORY = "MANDATORY";
	public static final String PROPAGATION_NOT_SUPPORTED = "NOT_SUPPORTED";
	public static final String PROPAGATION_SUPPORTS = "SUPPORTS";
	public static final String PROPAGATION_NEVER= "NEVER";

	private Operation operation;
	private List<Parameter> decoratedParameterList;
	
	private String signature;
	private String signatureWithBO;
	private String parameterNames;
	private Boolean isResponseTypeVoid;
	private String uncapitalizedReturnType;

	private static Log log = LogFactory.getLog(JavaOperation.class);


	public JavaOperation(Operation operationToDecorate) {
		if (operationToDecorate == null) {
			throw new IllegalArgumentException("JavaOperation must be instatiated with a non-null operation!");
		}
		operation = operationToDecorate;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return operation.getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getDocumentation() {
		return operation.getDocumentation();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getReturnType() {
		return operation.getReturnType();
	}
	
	public String getUncapitalizedReturnType() {
		if (uncapitalizedReturnType == null) {
			String returnType = getReturnType();
			if (returnType == null) {
				returnType = getReturnManyType();
			}
			uncapitalizedReturnType = StringUtils.uncapitalize(returnType);
		}
		
		return uncapitalizedReturnType;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLowercaseName() {
		return operation.getLowercaseName();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Parameter> getParameters() {
		if (decoratedParameterList == null) {
			List<Parameter> operationParameterList = operation.getParameters();
			if ((operationParameterList == null) || (operationParameterList.size() == 0)) {
				decoratedParameterList = Collections.emptyList();

			} else {
				decoratedParameterList = new ArrayList<Parameter>((int)(operationParameterList.size()));
				for (Parameter p : operationParameterList) {
					decoratedParameterList.add(new JavaParameter(p));

				}

			}
		}
		
		return decoratedParameterList;
	}
	
	/**
	 * Returns a list where the names of parameters have been modified
	 * to have commas at the end to separate them from the next parameter,
	 * as needed.  This functionality is aimed at allowing easy support for
	 * constructs such as parameter-level annotations.
	 * @return
	 */
	public List<Parameter> getParametersWithCommas() {
		List<Parameter> parameterList = getParameters();
		Iterator<Parameter> i = parameterList.iterator();
		while (i.hasNext()) {
			JavaParameter p = (JavaParameter)i.next();
			if (i.hasNext()) {
				p.setSignatureSuffix(",");
			}							
		}			
		
		return parameterList;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getReturnManyType() {
		return operation.getReturnManyType();
	}
	
	public Boolean isResponseTypeVoid() {
		if (isResponseTypeVoid == null) {			
			isResponseTypeVoid = (JavaElementUtils.VOID.equalsIgnoreCase(getReturnType())) ? Boolean.TRUE : Boolean.FALSE;
			
		}

		return isResponseTypeVoid;
	}	

	public String getSignature() {
		if (signature == null) {
			signature = JavaElementUtils.createSignatureParameters(getParameters());
		}

		return signature;
	}

	public String getParameterNames() {
		if (parameterNames == null) {
			StringBuffer buff = new StringBuffer(100);
			for (Iterator<Parameter> i = getParameters().iterator(); i.hasNext();) {
				Parameter param = i.next();
				buff.append(param.getName());
				if (i.hasNext()) {
					buff.append(", ");
				}
			}
			parameterNames = buff.toString();
		}

		return parameterNames;
	}

	public Set<String> getParameterImports() {
		Set<String> imports = new HashSet<String>();

		JavaParameter parameter;
		Collection<Parameter> parameterCollection = getParameters();
		for (Parameter p : parameterCollection) {
			parameter = (JavaParameter)p;
			if (parameter.isMany()) {
				imports.add(Collection.class.getName());
			}
			imports.add(parameter.getImport());
		}

		return imports;
	}

	public Set<String> getImports() {
		Set<String> importSet = new HashSet<String>();
		importSet.addAll( getParameterImports() );

		return importSet;
	}

	/**
	 * Returns the original type of return object, as specified in metadata, in its
	 * Java form
	 * @return The Java version of the wrapped return type
	 */
	public String getWrappedReturnType() {
		return JavaElementUtils.getJavaType(MetadataRepository.getInstance().getApplicationName(), getType());
	}

	private String getType() {
		return getReturnType()==null ? getReturnManyType() : getReturnType();
	}

	public boolean isReturnTypeEntity() {
		return MetadataRepository.getInstance().getEntity( getReturnType() ) != null;
	}
	
	public boolean isReturnManyTypeEntity() {
		return MetadataRepository.getInstance().getEntity( getReturnManyType() ) != null;
	}
	
	
	public boolean isReturnTypeCollection() {
		return !StringUtils.isBlank(getReturnManyType());
	} 

	/**
	 * {@inheritDoc}
	 */
	public String getViewType() {
		return operation.getViewType();
	}

	/**
	 * Returns whether or not this operation is available to remote clients
	 * @return <tt>Boolean</tt>
	 */
	public Boolean isRemote() {
		String viewType = getViewType();
		return new Boolean((Operation.VIEW_TYPE_BOTH.equals(viewType) || Operation.VIEW_TYPE_REMOTE.equals(viewType)));
	}

	/**
	 * Returns whether or not this operation is available to local clients
	 * @return <tt>Boolean</tt>
	 */
	public Boolean isLocal() {
		String viewType = getViewType();
		return new Boolean((Operation.VIEW_TYPE_BOTH.equals(viewType) || Operation.VIEW_TYPE_LOCAL.equals(viewType)));
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTransmissionMethod() {
		return operation.getTransmissionMethod();
	}

	/**
	 * Returns whether or not this operation is synchronous
	 * @return <tt>Boolean</tt>
	 */
	public Boolean isSynchronous() {
		String transmissionMethod = getTransmissionMethod();
		return new Boolean((Operation.TRANSMISSION_METHOD_SYNC.equals(transmissionMethod)));
	}

	/**
	 * Returns whether or not this operation is asynchronous
	 * @return <tt>Boolean</tt>
	 */
	public Boolean isAsynchronous() {
		String transmissionMethod = getTransmissionMethod();
		return new Boolean((Operation.TRANSMISSION_METHOD_ASYNC.equals(transmissionMethod)));
	}

	public String getUncapitalizedName() {
		return StringUtils.uncapitalize(getName());
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTransactionAttribute() {
		String returnValue = null;
		String txAttr = operation.getTransactionAttribute();

		if (TRANSACTION_REQUIRED.equals(txAttr)) {
			returnValue = PROPAGATION_REQUIRED;
		} else if (TRANSACTION_REQUIRES_NEW.equals(txAttr)) {
			returnValue = PROPAGATION_REQUIRES_NEW;
		} else if (TRANSACTION_MANDATORY.equals(txAttr)) {
			returnValue = PROPAGATION_MANDATORY;
		} else if (TRANSACTION_SUPPORTS.equals(txAttr)) {
			returnValue = PROPAGATION_SUPPORTS;
		} else if (TRANSACTION_NOT_SUPPORTED.equals(txAttr)) {
			returnValue = PROPAGATION_NOT_SUPPORTED;
		} else if (TRANSACTION_NEVER.equals(txAttr)) {
			returnValue = PROPAGATION_NEVER;
		} else {
			log.error("Unknown transaction attribute '" + txAttr +"' encountered!  Defaulting to "
				+ PROPAGATION_REQUIRED);
			returnValue = PROPAGATION_REQUIRED;
		}

		return returnValue;

	}
	
	public Boolean isResponseTypeCrossProject() {
		boolean isResponseTypeCrossProject = false;
		if (isReturnTypeEntity()) {
			MetadataRepository repo = MetadataRepository.getInstance();
			String currentApplicationName = repo.getApplicationName();
			String entityProject = ((Entity)repo.getAllEntities().get(getReturnType())).getApplicationName();
			isResponseTypeCrossProject = (currentApplicationName.equals(entityProject));
		}
		
		return new Boolean(isResponseTypeCrossProject);
	}
	
	/**
	 * Adds a "BO" to the end of the return type if it is an entity.
	 * @return return type string, possibly modified
	 */
	public String getReturnTypeAsBO() {
		StringBuilder sb = new StringBuilder();
		sb.append(operation.getReturnType());
		if (isReturnTypeEntity()) {
			sb.append(BUSINESS_OBJECT);
		}
		return sb.toString();
	}
	
	/**
	 * Adds a "BO" to the end of the return many type if it is an entity.
	 * @return return type string, possibly modified
	 */
	public String getReturnManyTypeAsBO() {
		StringBuilder sb = new StringBuilder();
		sb.append(operation.getReturnManyType());
		if (isReturnManyTypeEntity()) {
			sb.append(BUSINESS_OBJECT);
		}
		return sb.toString();
	}
	
	/**
	 * Returns the operation signature with any entity parameters as Business Object java types.
	 * @return signature with BO information
	 */
	public String getSignatureWithBO() {
		if (signatureWithBO == null) {
			signatureWithBO = JavaElementUtils.createSignatureParameters(getParameters(), BUSINESS_OBJECT, BUSINESS_OBJECT);
		}

		return signatureWithBO;
	}
	
}
