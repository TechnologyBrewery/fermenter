package org.tigris.atlas.service;

import org.tigris.atlas.transfer.TransferObject;

/**
 * Base class for service responses containing one application entity
 * 
 * @author Steve Andrews
 *
 */
public abstract class EntityServiceResponse extends ValueServiceResponse {
	
	public final TransferObject getEntity() {
		return (TransferObject) getValue();
	}

	public final void setEntity(TransferObject entity) {
		setValue(entity);
	}
	
	public boolean hasErrors() {
		boolean entityHasErrors = false;
		if( getEntity() != null && getEntity().getAllMessages() != null ) {
			entityHasErrors = getEntity().getAllMessages().hasErrorMessages();
		}
		return super.hasErrors() || entityHasErrors;
	}
	
}
