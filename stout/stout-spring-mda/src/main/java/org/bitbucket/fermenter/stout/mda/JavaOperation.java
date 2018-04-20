package org.bitbucket.fermenter.stout.mda;

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
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaOperation implements Operation {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaOperation.class);

    private static final String BUSINESS_OBJECT = "BO";
    public static final String PROPAGATION_REQUIRED = "REQUIRED";
    public static final String PROPAGATION_REQUIRES_NEW = "REQUIRES_NEW";
    public static final String PROPAGATION_MANDATORY = "MANDATORY";
    public static final String PROPAGATION_NOT_SUPPORTED = "NOT_SUPPORTED";
    public static final String PROPAGATION_SUPPORTS = "SUPPORTS";
    public static final String PROPAGATION_NEVER = "NEVER";

    private MetadataRepository metadataRepository = ModelInstanceRepositoryManager
            .getMetadataRepostory(MetadataRepository.class);

    private Operation operation;
    private List<Parameter> decoratedParameterList;

    private String signature;
    private String signatureWithBO;
    private String parameterNames;
    private Boolean isResponseTypeVoid;

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
        // make sure the is a trailing period for proper javadoc formatting:
        String documentation = operation.getDocumentation();
        if ((StringUtils.isNotBlank(documentation)) && (documentation.endsWith("."))) {
            documentation += ".";
        }

        return documentation;
    }

    /**
     * {@inheritDoc}
     */
    public String getReturnType() {
        return operation.getReturnType();
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
            if ((operationParameterList == null) || (operationParameterList.isEmpty())) {
                decoratedParameterList = Collections.emptyList();

            } else {
                decoratedParameterList = new ArrayList<>((int) (operationParameterList.size()));
                for (Parameter p : operationParameterList) {
                    decoratedParameterList.add(new JavaParameter(p));

                }

            }
        }

        return decoratedParameterList;
    }

    /**
     * Returns a list where the names of parameters have been modified to have commas at the end to separate them from
     * the next parameter, as needed. This functionality is aimed at allowing easy support for constructs such as
     * parameter-level annotations.
     * 
     * @return
     */
    public List<Parameter> getParametersWithCommas() {
        List<Parameter> parameterList = getParameters();
        Iterator<Parameter> i = parameterList.iterator();
        while (i.hasNext()) {
            JavaParameter p = (JavaParameter) i.next();
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
            isResponseTypeVoid = (JavaElementUtils.VOID.equalsIgnoreCase(getReturnType())) ? Boolean.TRUE
                    : Boolean.FALSE;

        }

        return isResponseTypeVoid;
    }

    public String getSignature() {
        if (signature == null) {
            signature = JavaElementUtils.createSignatureParameters(getParameters());
        }

        return signature;
    }

    /**
     * Creates the signature with needed jax-rs parameter descriptors included.
     * 
     * @return jax-rs compliant signature
     */
    public String getSignatureParametersWithJaxRS() {
        return getSignatureParametersWithParameterAnnotations("QueryParam");
    }

    /**
     * Creates the signature with needed feign parameter descriptors included.
     * 
     * @return feign compliant signature
     */
    public String getSignatureParametersWithFeign() {
        return getSignatureParametersWithParameterAnnotations("Param");
    }

    /**
     * Creates the signature with passed parameter annotation descriptors included.
     * 
     * @param annotationParam
     *            the annotation to use
     * @return compliant signature
     */
    public String getSignatureParametersWithParameterAnnotations(String annotationParam) {
        StringBuilder params = new StringBuilder();
        int entityParameterCount = 0;
        List<Parameter> parameterList = getParameters();
        if (parameterList != null) {
            for (Iterator<Parameter> i = parameterList.iterator(); i.hasNext();) {
                JavaParameter param = (JavaParameter) i.next();

                if (!param.isEntity()) {
                    params.append("@").append(annotationParam).append("(\"").append(param.getName()).append("\") ");

                } else {
                    entityParameterCount++;
                }

                if (param.isMany()) {
                    params.append(JavaElementUtils.PARAM_COLLECTION_TYPE + "<").append(param.getJavaType());
                    params.append(">");

                } else {
                    params.append(param.getJavaType());

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
                    + "Use a list of params or single container/transient entity instead!");
        }

        return params.toString();
    }

    /**
     * Creates the rest-style path for the operation.
     * 
     * @return rest style path
     */
    public String getRestStylePath() {
        StringBuilder path = new StringBuilder();
        path.append(getLowercaseName());

        int entityParameterCount = 0;
        List<Parameter> parameterList = getParameters();
        if (parameterList != null) {
            if (!parameterList.isEmpty()) {
                path.append("?");
            }

            boolean isFirst = Boolean.TRUE;
            for (Iterator<Parameter> i = parameterList.iterator(); i.hasNext();) {
                JavaParameter param = (JavaParameter) i.next();
                if (!param.isEntity()) {
                    if (!isFirst) {
                        path.append("&");
                    }
                    path.append(param.getName()).append("=");
                    path.append("{").append(param.getName()).append("}");
                    entityParameterCount++;
                    isFirst = Boolean.FALSE;
                }
            }
        }

        if (entityParameterCount > 1) {
            LOGGER.error("Cannot have a JAX-RS enabled operation with multiple entity parameters! "
                    + "Use a list of params or single container/transient entity instead!");
        }

        return path.toString();
    }

    public String getParameterNames() {
        if (parameterNames == null) {
            StringBuilder buff = new StringBuilder();
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
        Set<String> imports = new HashSet<>();

        JavaParameter parameter;
        Collection<Parameter> parameterCollection = getParameters();
        for (Parameter p : parameterCollection) {
            parameter = (JavaParameter) p;
            if (parameter.isMany()) {
                imports.add(List.class.getName());
            }

            String importValue = parameter.getImport();
            // java.lang is imported by default, so filter them out:
            if (!importValue.startsWith("java.lang.")) {
                imports.add(importValue);
            }

        }

        return imports;
    }

    public Set<String> getImports() {
        Set<String> importSet = new HashSet<>();
        importSet.addAll(getParameterImports());
        if (isReturnTypeCollection()) {
            importSet.add(Collection.class.getName());
        }

        // how return types are handled is very messy in general - will cleanup when we update the metamodel
        if (!isResponseTypeVoid()) {
            String currentAppName = metadataRepository.getApplicationName();
            String returnImport = JavaElementUtils.getJavaImportType(currentAppName, getReturnTypeForLookup());
            importSet.add(returnImport);
        }

        return importSet;
    }

    private String getReturnTypeForLookup() {
        return getReturnType() == null ? getReturnManyType() : getReturnType();
    }

    /**
     * Returns the original type of return object, as specified in metadata, in its Java form
     * 
     * @return The Java version of the wrapped return type
     */
    public String getWrappedReturnType() {
        return JavaElementUtils.getJavaType(metadataRepository.getApplicationName(), getReturnTypeForLookup());
    }

    public boolean isReturnTypeEntity() {
        return metadataRepository.getEntity(getReturnType()) != null;
    }

    public boolean isReturnTypeCollection() {
        return !StringUtils.isBlank(getReturnManyType());
    }

    @Override
    public String getResponseEncoding() {
        return operation.getResponseEncoding();
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
            log.error("Unknown transaction attribute '" + txAttr + "' encountered!  Defaulting to "
                    + PROPAGATION_REQUIRED);
            returnValue = PROPAGATION_REQUIRED;
        }

        return returnValue;

    }

    /**
     * Returns whether or not the operation requires the presence of an existing or new transaction.
     * 
     * @return
     */
    public boolean isTransactionNeeded() {
        String transactionAttribute = getTransactionAttribute();
        return PROPAGATION_REQUIRED.equals(transactionAttribute)
                || PROPAGATION_REQUIRES_NEW.equals(transactionAttribute)
                || PROPAGATION_MANDATORY.equals(transactionAttribute);
    }

    public Boolean isResponseTypeCrossProject() {
        boolean isResponseTypeCrossProject = false;
        if (isReturnTypeEntity()) {
            String currentApplicationName = metadataRepository.getApplicationName();
            String entityProject = metadataRepository.getAllEntities().get(getReturnType()).getApplicationName();
            isResponseTypeCrossProject = (currentApplicationName.equals(entityProject));
        }

        return isResponseTypeCrossProject;
    }

    /**
     * Returns the operation signature with any entity parameters as Business Object java types.
     * 
     * @return signature with BO information
     */
    public String getSignatureWithBO() {
        if (signatureWithBO == null) {
            signatureWithBO = JavaElementUtils.createSignatureParameters(getParameters(), BUSINESS_OBJECT,
                    BUSINESS_OBJECT);
        }

        return signatureWithBO;
    }

    /**
     * Returns whether or not this operation has parameters associated with it.
     * 
     * @return true if there are parameters, false otherwise
     */
    public boolean hasParameters() {
        return ((decoratedParameterList != null) && (!decoratedParameterList.isEmpty()));
    }

    /**
     * Returns whether or not there is at least one parameter that is an entity.
     * 
     * @return true if there are entity parameters, false otherwise
     */
    public boolean hasEntityParameters() {
        boolean hasEntityParameters = false;

        if (hasParameters()) {
            for (Parameter parameter : decoratedParameterList) {
                JavaParameter javaParameter = (JavaParameter) parameter;
                if (javaParameter.isEntity()) {
                    hasEntityParameters = true;
                    break;
                }
            }
        }

        return hasEntityParameters;
    }

}
