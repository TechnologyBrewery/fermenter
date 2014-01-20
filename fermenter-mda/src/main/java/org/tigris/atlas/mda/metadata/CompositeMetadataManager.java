package org.tigris.atlas.mda.metadata;


import java.util.Collections;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.tigris.atlas.mda.metadata.element.Composite;
import org.tigris.atlas.mda.metadata.element.CompositeMetadata;
import org.tigris.atlas.mda.metadata.element.FieldMetadata;

class CompositeMetadataManager extends MetadataManager {

	private static CompositeMetadataManager INSTANCE;
	
	public static CompositeMetadataManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CompositeMetadataManager();
		}
		
		return INSTANCE;
	}
	
	private CompositeMetadataManager() {
		super();
	}
	
	protected void initialize(Digester digester) {
		digester.addObjectCreate("composite", CompositeMetadata.class.getName());
		digester.addSetProperties("composite");
		
		digester.addObjectCreate("composite/field", FieldMetadata.class.getName());
		digester.addSetProperties("composite/field");
		digester.addSetNext("composite/field", "addField", FieldMetadata.class.getName());
		
		digester.addSetNext("composite", "addComposite", CompositeMetadata.class.getName() );
	}

	protected String getMetadataLocation() {
		return "composites";
	}
	
	public void addComposite(CompositeMetadata c) {
		c.setApplicationName(currentApplication);
		addMetadataElement(c.getType(), c);
	}
	
	public static Composite getCompositeMetadata(String applicationName, String name) {
		Map metadata = getInstance().getMetadataMap(applicationName);
		return metadata == null ? null : (Composite) metadata.get(name);
	}
	
	public static Map getComposites(String applicationName) {
		Map metadata = getInstance().getMetadataMap(applicationName);
		return metadata == null ? Collections.EMPTY_MAP : metadata;
	}
	
	/**
	 * Returns the metadata element by name from any application that is loaded
	 * @param name The name by which to retrieve
	 * @return The <tt>Composite</tt> instance for <tt>name</tt> or null
	 */
	public static Composite getCompositeMetadata(String name) {
		Map metadata = getInstance().getCompleteMetadataMap();
		return metadata == null ? null : (Composite) metadata.get(name);
	}

}
