package org.bitbucket.fermenter.mda.element.objectivec;

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
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Operation;
import org.bitbucket.fermenter.mda.metadata.element.Parameter;
import org.bitbucket.fermenter.mda.metadata.element.Relation;
import org.bitbucket.fermenter.mda.objectivec.ObjectiveCTypeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectiveCOperation implements Operation {

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectiveCOperation.class);

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

	private static Log log = LogFactory.getLog(ObjectiveCOperation.class);


	public ObjectiveCOperation(Operation operationToDecorate) {
		if (operationToDecorate == null) {
			throw new IllegalArgumentException("ObjectiveCOperation must be instatiated with a non-null operation!");
		}
		operation = operationToDecorate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return ObjectiveCTypeManager.getObjectiveCClassPrefix() + operation.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDocumentation() {
		String documentation = operation.getDocumentation();
		if ((StringUtils.isNotBlank(documentation)) && (documentation.endsWith("."))) {
			 documentation += ".";
		}

		return documentation;
	}

	public boolean hasValueResponse() {
		String type = this.getResponseType();
		return (type != null && !type.equalsIgnoreCase(ObjectiveCElementUtils.VOID));
	}

	public String getResponseType() {
		String type = operation.getReturnType();
		if (type == null) {
			type = operation.getReturnManyType();
		}
		return type;
	}

	public String getObjectiveCResponseType() {
		String type = this.getReturnType();
		if (type == null) {
			type = this.getReturnManyType();
		}
		return type;
	}

	public String getUncapitalizedResponseType() {
		return StringUtils.uncapitalize(this.getResponseType());
	}

	public Entity getResponseEntity() {
		return hasValueResponse() ? ObjectiveCElementUtils.getObjectiveCEntity(MetadataRepository.getInstance().getApplicationName(), this.getResponseType()) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getReturnType() {
		return ObjectiveCElementUtils.getObjectiveCType(MetadataRepository.getInstance().getApplicationName(), operation.getReturnType());
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
	@Override
	public String getLowercaseName() {
		return operation.getLowercaseName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Parameter> getParameters() {
		if (decoratedParameterList == null) {
			List<Parameter> operationParameterList = operation.getParameters();
			if ((operationParameterList == null) || (operationParameterList.size() == 0)) {
				decoratedParameterList = Collections.emptyList();

			} else {
				decoratedParameterList = new ArrayList<Parameter>((operationParameterList.size()));
				for (Parameter p : operationParameterList) {
					decoratedParameterList.add(new ObjectiveCParameter(p));

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
			ObjectiveCParameter p = (ObjectiveCParameter)i.next();
			if (i.hasNext()) {
				p.setSignatureSuffix(",");
			}
		}

		return parameterList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getReturnManyType() {
		return ObjectiveCElementUtils.getObjectiveCType(MetadataRepository.getInstance().getApplicationName(), operation.getReturnManyType());
	}

	public Boolean isResponseTypeVoid() {
		if (isResponseTypeVoid == null) {
			isResponseTypeVoid = (ObjectiveCElementUtils.VOID.equalsIgnoreCase(getReturnType())) ? Boolean.TRUE : Boolean.FALSE;

		}

		return isResponseTypeVoid;
	}

	public String getSignature() {
		if (signature == null) {
			signature = ObjectiveCElementUtils.createSignatureParameters(getParameters());
		}

		return signature;
	}

	/**
	 * Creates the signature with needed jax-rs parameter descriptors included.
	 * @return jax-rs compliant signature
	 */
	public String getSignatureParametersWithJaxRS() {
		StringBuilder params = new StringBuilder();
		int entityParameterCount = 0;
		List<Parameter> parameterList = getParameters();
		if (parameterList != null) {
			for (Iterator<Parameter> i = parameterList.iterator(); i.hasNext();) {
				ObjectiveCParameter param = (ObjectiveCParameter)i.next();

				if (!param.isEntity()) {
					params.append("@QueryParam(\"").append(param.getName()).append("\") ");

				} else {
					entityParameterCount++;
				}

				if (param.isMany()) {
					params.append(ObjectiveCElementUtils.PARAM_COLLECTION_TYPE + "<").append(param.getObjectiveCType());
					params.append(">");

				} else {
					params.append(param.getObjectiveCType());

				}

				params.append(" ");
				params.append(param.getName());

				if (i.hasNext()) {
					params.append(", ");
				}
			}
		}

		if (entityParameterCount > 1) {
			LOGGER.error("Cannot have a JAX-RS enabled operation with multiple entity parameters! "
					+ "Use a list of params or single container entity instead!");
		}

		return params.toString();
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

		ObjectiveCParameter parameter;
		Collection<Parameter> parameterCollection = getParameters();
		for (Parameter p : parameterCollection) {
			parameter = (ObjectiveCParameter)p;
			if (parameter.isMany()) {
				imports.add(List.class.getName());
			}
			imports.add(parameter.getImport());
		}

		return imports;
	}

	public Set<String> getResponseEntityImports() {
		Set<String> imports = new HashSet<String>();

		Entity entity = getResponseEntity();
		if (entity != null) {
			String appName = MetadataRepository.getInstance().getApplicationName();

			imports.add(ObjectiveCElementUtils.getObjectiveCImport(appName, getObjectiveCResponseType()));

			Collection<Relation> relations = new ArrayList<Relation>();
			gatherAllRelations(relations, entity.getRelations().values());
			for (Relation r : relations) {
				ObjectiveCRelation objectiveCRelation = (ObjectiveCRelation)r;
				imports.add(ObjectiveCElementUtils.getObjectiveCImport(appName, objectiveCRelation.getType()));
			}
		}

		return imports;
	}

	private void gatherAllRelations(Collection<Relation> target, Collection<Relation> source) {
		target.addAll(source);
		for (Relation r : source) {
			gatherAllRelations(target, r.getChildRelations());
		}
	}

	public Set<String> getImports() {
		Set<String> importSet = new HashSet<String>();
		// This causes imports of NSDecimalNumber and NSString.  Comment it out for now.
		//importSet.addAll(getParameterImports());
		importSet.addAll(getResponseEntityImports());

		return importSet;
	}

	/**
	 * Returns the original type of return object, as specified in metadata, in its
	 * Objective-C form
	 * @return The Objective-C version of the wrapped return type
	 */
	public String getWrappedReturnType() {
		return ObjectiveCElementUtils.getObjectiveCType(MetadataRepository.getInstance().getApplicationName(), getType());
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
	@Override
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
	@Override
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
	@Override
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
	 * Returns the operation signature with any entity parameters as Business Object Objective-C types.
	 * @return signature with BO information
	 */
	public String getSignatureWithBO() {
		if (signatureWithBO == null) {
			signatureWithBO = ObjectiveCElementUtils.createSignatureParameters(getParameters(), BUSINESS_OBJECT, BUSINESS_OBJECT);
		}

		return signatureWithBO;
	}

	/**
	 * Returns whether or not this operation has parameters associated with it.
	 * @return true if there are parameters, false otherwise
	 */
	public boolean hasParameters() {
		return ((decoratedParameterList != null) && (decoratedParameterList.size() > 0));
	}

	/**
	 * Returns whether or not there is at least one parameter that is an entity.
	 * @return true if there are entity parameters, false otherwise
	 */
	public boolean hasEntityParameters() {
		boolean hasEntityParameters = false;

		if (hasParameters()) {
			for (Parameter parameter : decoratedParameterList) {
				ObjectiveCParameter objectiveCParameter = (ObjectiveCParameter)parameter;
				if (objectiveCParameter.isEntity()) {
					hasEntityParameters = true;
					break;
				}
			}
		}

		return hasEntityParameters;
	}

}
