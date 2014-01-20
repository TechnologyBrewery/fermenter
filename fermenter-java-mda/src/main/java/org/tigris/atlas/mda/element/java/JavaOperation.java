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
import org.tigris.atlas.mda.PackageManager;
import org.tigris.atlas.mda.metadata.MetadataRepository;
import org.tigris.atlas.mda.metadata.element.Entity;
import org.tigris.atlas.mda.metadata.element.Operation;
import org.tigris.atlas.mda.metadata.element.Parameter;
import org.tigris.atlas.service.ServiceResponse;

public class JavaOperation implements Operation {

	public static final String PROPAGATION_REQUIRED = "PROPAGATION_REQUIRED";
	public static final String PROPAGATION_REQUIRES_NEW = "PROPAGATION_REQUIRES_NEW";
	public static final String PROPAGATION_MANDATORY = "PROPAGATION_MANDATORY";
	public static final String PROPAGATION_NOT_SUPPORTED = "PROPAGATION_NOT_SUPPORTED";
	public static final String PROPAGATION_SUPPORTS = "PROPAGATION_SUPPORTS";
	public static final String PROPAGATION_NEVER= "PROPAGATION_NEVER";

	private Operation operation;
	private List decoratedParameterList;

	private String responseType;
	private String shortResponseType;
	private String uncapitalizedResponseType;
	private String responseImportType;
	private String responseTypeBasePackage;
	private String signature;
	private String parameterNames;
	private Boolean isResponseTypeVoid;
	private String uncapitalizedReturnType;

	private static final String SERVICE_RESPONSE = "ServiceResponse";
	private static final String COLLECTION_SERVICE_RESPONSE = "CollectionServiceResponse";
	private static final String COLLECTION_SERVICE_RESPONSE_SHORT = "CSR";

	private static Log log = LogFactory.getLog(JavaOperation.class);


	public JavaOperation(Operation operationToDecorate) {
		if (operationToDecorate == null) {
			throw new IllegalArgumentException("JavaOperation must be instatiated with a non-null operation!");
		}
		operation = operationToDecorate;
	}

	public String getName() {
		return operation.getName();
	}
	
	public String getDocumentation() {
		return operation.getDocumentation();
	}

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

	public String getLowercaseName() {
		return operation.getLowercaseName();
	}

	public List getParameters() {
		if (decoratedParameterList == null) {
			List operationParameterList = operation.getParameters();
			if ((operationParameterList == null) || (operationParameterList.size() == 0)) {
				decoratedParameterList = Collections.EMPTY_LIST;

			} else {
				Parameter p;
				decoratedParameterList = new ArrayList((int)(operationParameterList.size()));
				Iterator i = operationParameterList.iterator();
				while (i.hasNext()) {
					p = (Parameter)i.next();
					decoratedParameterList.add(new JavaParameter(p));

				}

			}
		}
		
		return decoratedParameterList;
	}
	
	/**
	 * Returns a list where the names of parameters have been modified
	 * to have commas at the end to seperate them from the next parameter,
	 * as needed.  This functionality is aimed at allowing easy support for
	 * constructs such as parameter-level annotations.
	 * @return
	 */
	public List getParametersWithCommas() {
		List parameterList = getParameters();
		Iterator i = parameterList.iterator();
		while (i.hasNext()) {
			JavaParameter p = (JavaParameter)i.next();
			String name = p.getName();
			if (i.hasNext()) {
				p.setSignatureSuffix(",");
			}							
		}			
		
		return parameterList;
	}


	public String getReturnManyType() {
		return operation.getReturnManyType();
	}

	//Java generation specific methods:

	/**
	 * @deprecated Use getResponseImportType() instead
	 */
	public String getJavaImportType() {
		return getResponseImportType();
	}

	/**
	 * @deprecated Use getResponseType() instead
	 */
	public String getJavaType() {
		return getResponseType();
	}

	public String getResponseType() {
		if (responseType == null) {
			responseType = JavaElementUtils.getJavaType(MetadataRepository.getInstance().getApplicationName(), getResponseImportType());
		}

		return responseType;
	}
	
	/**
	 * This method is used to return an abbreviated class name in the event that class names become too long.  This 
	 * case has been hit with XMLBeans, for example.
	 * @return
	 */
	public String getShortResponseType() {
		if (shortResponseType == null) {
			if (isReturnTypeCollection()) {
				StringBuffer sb = new StringBuffer(150);
				sb.append(StringUtils.capitalize(getReturnManyType())).append(COLLECTION_SERVICE_RESPONSE_SHORT);
				shortResponseType = sb.toString();
				
			} else {
				shortResponseType = getResponseType();
				
			}
		}
		
		return shortResponseType;
	}
	
	public Boolean isResponseTypeVoid() {
		if (isResponseTypeVoid == null) {			
			isResponseTypeVoid = ("void".equalsIgnoreCase(getReturnType())) ? Boolean.TRUE : Boolean.FALSE;
			
		}

		return isResponseTypeVoid;
	}	
	
	public String getUncapitalizedResponseType() {
		if (uncapitalizedResponseType == null) {
			String rt = getResponseType();
			uncapitalizedResponseType = (rt != null) ? StringUtils.uncapitalize(rt) : null;
		}
		return uncapitalizedResponseType;
	}		

	public String getResponseImportType() {
		if (responseImportType == null) {
			StringBuffer sb = new StringBuffer(150);
			if (isReturnTypeCollection()) {
				sb.append(StringUtils.capitalize(getReturnManyType())).append(COLLECTION_SERVICE_RESPONSE);
				responseImportType = JavaElementUtils.createFullyQualifiedName(sb.toString(), ".service.");	
			} else if (!isReturnTypeEntity()) {
				sb.append(ServiceResponse.class.getPackage().getName());
				sb.append(".");
				String javaType = JavaElementUtils.getJavaType(MetadataRepository.getInstance().getApplicationName(), getReturnType());
				sb.append(StringUtils.capitalize(javaType));
				sb.append(SERVICE_RESPONSE);
				responseImportType = sb.toString();
			} else {
				sb.append(StringUtils.capitalize(getReturnType())).append(SERVICE_RESPONSE);
				responseImportType = JavaElementUtils.createFullyQualifiedName(sb.toString(), ".service.");
			}
		}
		return responseImportType;
	}
	
	public String getResponseTypeBasePackage() {
		if (responseTypeBasePackage == null) {
			String applicationName;
			if (isResponseTypeCrossProject().booleanValue()) {
				applicationName = ((Entity)MetadataRepository.getInstance().getAllEntities().get(getReturnType())).getApplicationName();
			} else {
				applicationName = MetadataRepository.getInstance().getApplicationName();
			}			
			String packageName = PackageManager.getBasePackage(applicationName);
			responseTypeBasePackage = packageName;
		}
		return responseTypeBasePackage;
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
			for (Iterator i = getParameters().iterator(); i.hasNext();) {
				Parameter param = (Parameter) i.next();
				buff.append(param.getName());
				if (i.hasNext()) {
					buff.append(", ");
				}
			}
			parameterNames = buff.toString();
		}

		return parameterNames;
	}

	public Set getParameterImports() {
		Set importSet = new HashSet();

		JavaParameter parameter;
		Collection parameterCollection = getParameters();
		Iterator parameterIterator = parameterCollection.iterator();
		while (parameterIterator.hasNext()) {
			parameter = (JavaParameter)parameterIterator.next();
			if (parameter.isMany()) {
				importSet.add(Collection.class.getName());
			}
			importSet.add(parameter.getImport());
		}

		return importSet;
	}

	public Set getImports() {
		Set importSet = new HashSet();

		importSet.addAll( getParameterImports() );
		importSet.add(getResponseImportType());

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
	
	public boolean isReturnTypeCollection() {
		return !StringUtils.isBlank(getReturnManyType());
	} 

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
	
}
