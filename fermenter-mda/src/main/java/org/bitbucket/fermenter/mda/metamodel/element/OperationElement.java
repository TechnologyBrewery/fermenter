package org.bitbucket.fermenter.mda.metamodel.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents an operation within a service.
 */
@JsonPropertyOrder({ "name" })
public class OperationElement extends MetamodelElement implements Operation {

    @JsonInclude(Include.NON_NULL)
    protected String documentation;

    @JsonInclude(Include.NON_NULL)
    protected String transactionAttribute;

    @JsonProperty(value = "return", required = true)
    protected Return returnElement;

    protected List<Parameter> parameters = new ArrayList<>();

    @JsonInclude(Include.NON_NULL)
    protected Boolean compressedWithGZip;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentation() {
        return documentation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTransactionAttribute() {
        return transactionAttribute;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Return getReturn() {
        return returnElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isCompressedWithGZip() {
        return compressedWithGZip;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (returnElement != null) {
            returnElement.validate();
        }

        for (Parameter parameter : parameters) {
            parameter.validate();
        }

        validateTransactionAttribute();
        defaultTransactionAttribute();
        
        //default:
        if (compressedWithGZip == null) {
           compressedWithGZip = Boolean.FALSE;
        }

    }

    private void validateTransactionAttribute() {
        if ((!TRANSACTION_REQUIRED.equals(transactionAttribute))
                && (!TRANSACTION_REQUIRES_NEW.equals(transactionAttribute))
                && (!TRANSACTION_MANDATORY.equals(transactionAttribute))
                && (!TRANSACTION_NOT_SUPPORTED.equals(transactionAttribute))
                && (!TRANSACTION_SUPPORTS.equals(transactionAttribute))
                && (!TRANSACTION_NEVER.equals(transactionAttribute))) {
            messageTracker.addErrorMessage("Transaction attribute must be '" + TRANSACTION_REQUIRED + "', '"
                    + TRANSACTION_REQUIRES_NEW + "', '" + TRANSACTION_MANDATORY + "', '" + TRANSACTION_NOT_SUPPORTED
                    + "', '" + TRANSACTION_SUPPORTS + "' or '" + TRANSACTION_NEVER + "'");
        }

    }

    private void defaultTransactionAttribute() {
        if (StringUtils.isBlank(transactionAttribute)) {
            if (name.startsWith("find") || name.startsWith("query") || name.startsWith("load")) {
                transactionAttribute = TRANSACTION_SUPPORTS;
            } else {
                transactionAttribute = TRANSACTION_REQUIRED;
            }
        }
    }

    /**
     * Sets the return information for this operation.
     * 
     * @param returnElement
     *            return info
     */
    public void setReturn(Return returnElement) {
        this.returnElement = returnElement;
    }

    /**
     * Sets the documentation for this operation.
     * 
     * @param documentation
     *            operation documentation
     */
    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    /**
     * Sets the transaction attribute for this method.
     * 
     * @param transactionAttribute
     *            transaction attribute
     */
    public void setTransactionAttribute(String transactionAttribute) {
        this.transactionAttribute = transactionAttribute;
    }

    /**
     * Sets compression for this operation.
     * 
     * @param compress
     *            whether or not to compress
     */
    public void setCompressedWithGZip(Boolean compress) {
        this.compressedWithGZip = compress;
    }

}
