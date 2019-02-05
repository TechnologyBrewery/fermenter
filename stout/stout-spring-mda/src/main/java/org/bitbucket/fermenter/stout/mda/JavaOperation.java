package org.bitbucket.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.BaseOperationDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Operation;
import org.bitbucket.fermenter.mda.metamodel.element.Parameter;
import org.bitbucket.fermenter.mda.metamodel.element.Return;
import org.bitbucket.fermenter.mda.metamodel.element.Transaction;
import org.bitbucket.fermenter.stout.util.ToDateExpander;
import org.jboss.resteasy.annotations.GZIP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decorates a {@link Operation} with Java-specific capabilities.
 */
public class JavaOperation extends BaseOperationDecorator implements Operation, JavaNamedElement {

    private static final Logger logger = LoggerFactory.getLogger(JavaOperation.class);

    protected static final String BUSINESS_OBJECT = "BO";

    protected MetadataRepository metadataRepository = ModelInstanceRepositoryManager
            .getMetadataRepostory(MetadataRepository.class);

    protected List<Parameter> decoratedParameterList;

    protected String signature;
    protected String signatureWithBO;
    protected String parameterNames;

    /**
     * {@inheritDoc}
     */
    public JavaOperation(Operation operationToDecorate) {
        super(operationToDecorate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentation() {
        return JavaElementUtils.formatForJavadoc(super.getDocumentation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Return getReturn() {
        return new JavaReturn(wrapped.getReturn());
    }

    /**
     * {@inheritDoc}
     */
    public List<Parameter> getParameters() {
        if (decoratedParameterList == null) {
            List<Parameter> operationParameterList = super.getParameters();
            if (CollectionUtils.isEmpty(operationParameterList)) {
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
     * @return para list with appropriate commas
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
     * Whether or not this returns a collection or single instance.
     * 
     * @return is collection?
     */
    public Boolean isResponseTypeVoid() {
        return JavaElementUtils.VOID.equalsIgnoreCase(getReturn().getType()) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Calculates the plain Java signature for this operation. It's labor intensive and messy in velocity, so much more
     * clean to do it in the Java decorator.
     * 
     * @return signature
     */
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
     * Creates the signature with needed feign parameter descriptors and custom parameter expansion included.
     * 
     * @return feign compliant signature
     */
    public String getSignatureParametersWithFeignForDate() {
        return getSignatureParametersWithParameterAnnotations("DateParam");
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
                    if (annotationParam.equalsIgnoreCase("DateParam")  && param.getJavaType().equalsIgnoreCase("Date")) {
                        params.append("@").append("Param").append("(value = \"").append(param.getName()).append("\", expander = ToDateExpander.class) ");
                    } else if (annotationParam.equalsIgnoreCase("DateParam")){
                        params.append("@").append("Param").append("(\"").append(param.getName()).append("\") ");
                    } else {
                        params.append("@").append(annotationParam).append("(\"").append(param.getName()).append("\") ");                        
                    }
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
            logger.error("Cannot have a JAX-RS enabled operation with multiple entity parameters! "
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
        path.append(getUncapitalizedName());

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
            logger.error("Cannot have a JAX-RS enabled operation with multiple entity parameters! "
                    + "Use a list of params or single container/transient entity instead!");
        }

        return path.toString();
    }

    /**
     * Returns the parameter names in a comma-separated list.
     * 
     * @return param names
     */
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

    /**
     * Returns all imports leveraged by this operation's parameters.
     * 
     * @return parameter imports
     */
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
            if (JavaElementUtils.checkImportAgainstDefaults(importValue)) {
                imports.add(importValue);
            }

        }

        return imports;
    }

    /**
     * All imports for this operation, inclusive of parameters and the return.
     * 
     * @return imports
     */
    public Set<String> getImports() {
        Set<String> importSet = new HashSet<>();
        importSet.addAll(getParameterImports());
        Return returnElement = getReturn();
        if (returnElement.isMany()) {
            importSet.add(Collection.class.getName());
        }
        if (isCompressedWithGZip()) {
            importSet.add(GZIP.class.getName());
        }

        // how return types are handled is very messy in general - will cleanup when we update the metamodel
        if (!isResponseTypeVoid()) {
            String currentAppName = metadataRepository.getApplicationName();
            String returnImport = JavaElementUtils.getJavaImportType(currentAppName, returnElement.getType());
            if (JavaElementUtils.checkImportAgainstDefaults(returnImport)) {
                importSet.add(returnImport);
            }
        }

        return importSet;
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
        return !decoratedParameterList.isEmpty();
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

    /**
     * {@inheritDoc}
     */
    public String getTransactionAttribute() {
        String returnValue = null;
        String sourceTransaction = super.getTransactionAttribute();
        Transaction transaction = Transaction.getTransactionByJtaName(sourceTransaction);

        if (transaction != null) {
            returnValue = transaction.name();
        } else {
            logger.error("Unknown transaction attribute '{}' encountered!  Defaulting to '{}'", sourceTransaction,
                    Transaction.REQUIRED);
            returnValue = Transaction.REQUIRED.name();
        }

        return returnValue;

    }   

}
