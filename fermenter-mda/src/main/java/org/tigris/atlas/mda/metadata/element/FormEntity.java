package org.tigris.atlas.mda.metadata.element;

import java.util.Collection;

public interface FormEntity {

	String getProject();
	
	String getName();
	
	String getType();
	
	Collection getFields();
	
}
