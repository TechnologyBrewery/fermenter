package org.bitbucket.fermenter.mda.metadata.element;

import java.util.Collection;

public interface Format {

	String getName();

	Collection<Pattern> getPatterns();
	
}
