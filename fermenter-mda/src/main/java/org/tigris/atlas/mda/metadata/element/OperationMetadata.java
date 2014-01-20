package org.tigris.atlas.mda.metadata.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tigris.atlas.mda.metadata.MetadataRepository;

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
	private List parameters;
	private String transactionAttribute;
	private String viewType;
	private String transmissionMethod;
	
	private static Log log = LogFactory.getLog(Operation.class);
	
	/*
	 * @see org.tigris.atlas.mda.metadata.Operation#getName()
	 */
	public String getName() {
		return StringUtils.capitalize( name );
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/*
	 * @see org.tigris.atlas.mda.metadata.Operation#getDocumentation()
	 */
	public String getDocumentation() {
		return documentation;
	}
	
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	
	/*
	 * @see org.tigris.atlas.mda.metadata.Operation#getReturnType()
	 */
	public String getReturnType() {
		return returnType;
	}
	
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	/*
	 * @see org.tigris.atlas.mda.metadata.Operation#getLowercaseName()
	 */
	public String getLowercaseName() {
		return StringUtils.uncapitalize(getName());
	}

	/*
	 * @see org.tigris.atlas.mda.metadata.Operation#getParameters()
	 */
	public List getParameters() {
		if( parameters == null ) {
			parameters = new ArrayList();
		}
		
		return parameters;
	}
	
	public void addParameter(org.tigris.atlas.mda.metadata.element.Parameter parameter) {
		getParameters().add(parameter);
	}

	/*
	 * @see org.tigris.atlas.mda.metadata.Operation#getReturnManyType()
	 */
	public String getReturnManyType() {
		return returnManyType;
	}

	public void setReturnManyType(String returnManyType) {
		this.returnManyType = returnManyType;
	}
	
	/*
	 * @see org.tigris.atlas.mda.metadata.Operation#getTransactionAttribute()
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
	 * @see org.tigris.atlas.mda.metadata.element.Operation#getViewType()
	 */
	public String getViewType() {
		return (viewType != null) ? viewType : VIEW_TYPE_BOTH;
	}

	/**
	 * Sets the view type of this operation
	 * @param viewType The viewType to set.
	 */
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	/**
	 * @see org.tigris.atlas.mda.metadata.element.Operation#getTransmissionMethod()
	 */
	public String getTransmissionMethod() {
		return  (transmissionMethod != null) ? transmissionMethod : TRANSMISSION_METHOD_SYNC;
	}	
	
	/**
	 * @param transmissionMethod The transmissionMethod to set.
	 */
	public void setTransmissionMethod(String transmissionMethod) {
		this.transmissionMethod = transmissionMethod;
	}

	/**
	 * Executed to ensure that valid combinations of metadata have been loaded.
	 *
	 */
	public void validate() {			
		validateTransactionAttribute();
		validateTransmissionMethod();
		validateReturnManyType();
	}
	
	private void validateReturnManyType() {
		// Currently return-many is not supported for primitive types
		String returnManyType = getReturnManyType();		
		if (!StringUtils.isBlank(returnManyType)) {
			Entity e = MetadataRepository.getInstance().getEntity(returnManyType);
			if (e == null) {
				String msg = "'return-many' on operation '" + getName() + "' is not currently supported for the primitive (non-entity) type '" + returnManyType + "'";
				log.error(msg);
				throw new IllegalArgumentException(msg);
			}
		}
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
	
	private void validateTransmissionMethod() {
		String transmissionMethod = getTransmissionMethod();
		if ( (!TRANSMISSION_METHOD_SYNC.equals(transmissionMethod))
		&& (!TRANSMISSION_METHOD_ASYNC.equals(transmissionMethod))
		) {
			log.error("Transmission method must be '" + TRANSMISSION_METHOD_SYNC + "' or '"
					+ TRANSMISSION_METHOD_ASYNC + "'");
		}
		
	}
	
}