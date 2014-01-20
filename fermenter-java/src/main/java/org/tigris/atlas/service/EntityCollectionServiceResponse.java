package org.tigris.atlas.service;


import java.util.ArrayList;
import java.util.Collection;

import org.tigris.atlas.transfer.TransferObject;

/**
 * Base class for services that return collections of entities. 
 *
 */
public abstract class EntityCollectionServiceResponse extends CollectionServiceResponse {

	public final Collection getEntities() {
		return super.getItems();
	}

	protected final void addEntity(TransferObject entity) {
		addItem(entity);
	}
	
	protected final void addEntities(Collection entities) {
		addItems(entities);
	}
	
	protected final Class getCollectionType() {
		return ArrayList.class;
	}

}
