package org.bitbucket.fermenter.mda.metadata.element;

import java.util.Collection;

public interface Message {

	static final String DEFAULT_LOCALE = "DEFAULT";
	
	String getKey();
	
	Collection<String> getLocales();
	
	MessageText getSummary(String locale);
	
	MessageText getDetail(String locale);
	
}
