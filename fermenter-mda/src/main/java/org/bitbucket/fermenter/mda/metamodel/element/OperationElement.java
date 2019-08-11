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

    @JsonInclude(Include.NON_NULL)
    protected Boolean pagedResponse;

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
        defaultTransactionAttribute();
        defaultCompressedWithGZip();
        defaultPagedResponse();
        defaultPageIndexAndSizeParamForPagedResponse();

        if (returnElement != null) {
            returnElement.validate();
        }

        for (Parameter parameter : parameters) {
            parameter.validate();
        }

        validateTransactionAttribute();
    }

    private void defaultPageIndexAndSizeParamForPagedResponse() {
        if(getReturn().isPagedResponse()) {
            ParameterElement pageIndexParam = new ParameterElement();
            pageIndexParam.setName("startPage");
            pageIndexParam.setType("integer");
            pageIndexParam.setDocumentation("The index of the page being requested. Base zero. Must be a positive integer.");
            pageIndexParam.setMany(false);
            parameters.add(pageIndexParam);
            
            ParameterElement pageSizeParam = new ParameterElement();
            pageSizeParam.setName("count");
            pageSizeParam.setType("integer");
            pageSizeParam.setMany(false);
            pageSizeParam.setDocumentation("The number of elements being requested. Must be a positive integer.");
            parameters.add(pageSizeParam);
        }
    }

    private void defaultPagedResponse() {
        if (pagedResponse == null) {
            pagedResponse = Boolean.FALSE;
        }
    }

    private void defaultCompressedWithGZip() {
        if (compressedWithGZip == null) {
            compressedWithGZip = Boolean.FALSE;
        }
    }

    private void validateTransactionAttribute() {
        if (!Transaction.isValidTransaction(getTransactionAttribute())) {
            messageTracker.addErrorMessage("Transaction attribute must be '" + Transaction.REQUIRED + "', '"
                    + Transaction.REQUIRES_NEW + "', '" + Transaction.MANDATORY + "', '" + Transaction.NOT_SUPPORTED
                    + "', '" + Transaction.SUPPORTS + "' or '" + Transaction.NEVER + "'");
        }

    }

    private void defaultTransactionAttribute() {
        if (StringUtils.isBlank(transactionAttribute)) {
            if (name.startsWith("find") || name.startsWith("query") || name.startsWith("load")) {
                transactionAttribute = Transaction.SUPPORTS.toString();
            } else {
                transactionAttribute = Transaction.REQUIRED.toString();
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
