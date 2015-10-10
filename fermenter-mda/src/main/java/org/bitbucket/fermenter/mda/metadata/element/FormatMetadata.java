package org.bitbucket.fermenter.mda.metadata.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FormatMetadata implements Format {

	private String name;
	private Collection patterns;
	
	public FormatMetadata() {
		super();
		
		patterns = new ArrayList();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void addPattern(Pattern pattern) {
		patterns.add(pattern);
	}
	
	public Collection getPatterns() {
		return Collections.unmodifiableCollection(patterns);
	}

}
