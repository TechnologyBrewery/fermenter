package org.bitbucket.fermenter.mda.metadata.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Representation of operation metadata.  This class contains <b>ONLY</b> data
 * relevant to the concept of an operation.  Absolutely no implementation-specific
 * functionality should reside in this class.
 */
public class OperationMetadata extends MetadataElement implements Operation {
	
	private String name;
	private String documentation;
	private String returnType;
	private String returnManyType;
	private List<Parameter> parameters;
	private String transactionAttribute;
	private String responseEncoding;
	private String compressWithGzip;

    private static Log log = LogFactory.getLog(Operation.class);
	
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return StringUtils.capitalize( name );
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getDocumentation() {
		return documentation;
	}
	
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getReturnType() {
		return returnType;
	}
	
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getLowercaseName() {
		return StringUtils.uncapitalize(getName());
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Parameter> getParameters() {
		if( parameters == null ) {
			parameters = new ArrayList<Parameter>();
		}
		
		return parameters;
	}
	
	public void addParameter(Parameter parameter) {
		getParameters().add(parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getReturnManyType() {
		return returnManyType;
	}

	public void setReturnManyType(String returnManyType) {
		this.returnManyType = returnManyType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getTransactionAttribute() {
		return (transactionAttribute != null) ? transactionAttribute : TRANSACTION_REQUIRED;
	}

	/**
	 * @param transactionAttribute The transactionAttribute to set.
	 */
	public void setTransactionAttribute(String transactionAttribute) {
		if (transactionAttribute != null) {			
			if (TRANSACTION_REQUIRED.equalsIgnoreCase(transactionAttribute)) {
				this.transactionAttribute = TRANSACTION_REQUIRED;
			} else if (TRANSACTION_REQUIRES_NEW.equalsIgnoreCase(transactionAttribute)) {
				this.transactionAttribute = TRANSACTION_REQUIRES_NEW;
			} else if (TRANSACTION_MANDATORY.equalsIgnoreCase(transactionAttribute)) {
				this.transactionAttribute = TRANSACTION_MANDATORY;
			} else if (TRANSACTION_NOT_SUPPORTED.equalsIgnoreCase(transactionAttribute)) {
				this.transactionAttribute = TRANSACTION_NOT_SUPPORTED;
			} else if (TRANSACTION_SUPPORTS.equalsIgnoreCase(transactionAttribute)) {
				this.transactionAttribute = TRANSACTION_SUPPORTS;
			} else if (TRANSACTION_NEVER.equalsIgnoreCase(transactionAttribute)) {
				this.transactionAttribute = TRANSACTION_SUPPORTS;
			} else {
				this.transactionAttribute = transactionAttribute;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getResponseEncoding() {
		return responseEncoding != null ? responseEncoding : "UTF-8";
	}
	
	public void setResponseEncoding(String responseEncoding) {
		this.responseEncoding = responseEncoding;
	}
	
	   
	/**
	 * @return Whether this data will be compressed using GZIP.
	 */
    public boolean isCompressedWithGzip() {
        return (this.compressWithGzip != null && this.compressWithGzip.equalsIgnoreCase("true"));
    }

    /**
     * @param compression true if the data will be compressed with GZIP, false otherwise
     */
    public void setCompressedWithGzip(String compression) {
        this.compressWithGzip = compression;
    }
	
	/**
	 * {@inheritDoc}
	 */
	public void validate() {			
		validateTransactionAttribute();
	}

	private void validateTransactionAttribute() {
		String transAttr = getTransactionAttribute();
		if ( (!TRANSACTION_REQUIRED.equals(transAttr))
		&& (!TRANSACTION_REQUIRES_NEW.equals(transAttr))
		&& (!TRANSACTION_MANDATORY.equals(transAttr))
		&& (!TRANSACTION_NOT_SUPPORTED.equals(transAttr))
		&& (!TRANSACTION_SUPPORTS.equals(transAttr))
		&& (!TRANSACTION_NEVER.equals(transAttr)) ) {
			log.error("Transaction attribute must be '" + TRANSACTION_REQUIRED + "', '"
					+ TRANSACTION_REQUIRES_NEW + "', '" + TRANSACTION_MANDATORY + "', '"
					+ TRANSACTION_NOT_SUPPORTED + "', '"+ TRANSACTION_SUPPORTS + "' or '"
					+ TRANSACTION_NEVER + "'");
		}
		
	}
	
}