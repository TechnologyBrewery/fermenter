package org.tigris.atlas.service;

import java.util.Collection;
import java.util.Collections;

public abstract class CollectionServiceResponse extends ServiceResponse {

	private Collection items;
	
	public final Collection getItems() {
		return Collections.unmodifiableCollection(getCollection());
	}
	
	public final void addItem(Object item) {
		getCollection().add(item);
	}
	
	public final void addItems(Collection items) {
		getCollection().addAll(items);
	}
	
	protected abstract Class getCollectionType();
	
	private Collection getCollection() {
		if (items == null) {
			Class clazz = getCollectionType();
			try {
				items = (Collection) clazz.newInstance();	
			} catch (Exception ex) {
				throw new RuntimeException("Unable to create collection for class " + clazz);
			}
		}
		
		return items;
	}
	
}
