package org.tigris.atlas.mda.element.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tigris.atlas.mda.metadata.element.Field;
import org.tigris.atlas.mda.metadata.element.Query;

public class JavaQuery implements Query {
	
	private Query query;
	private List decoratedCriteriaList;
	
	/**
	 * Create a new instance of <tt>Query</tt> with the correct functionality set 
	 * to generate Java code
	 * @param queryToDecorate The <tt>Query</tt> to decorate
	 */
	public JavaQuery(Query queryToDecorate) {
		if (queryToDecorate == null) {
			throw new IllegalArgumentException("JavaQuerys must be instatiated with a non-null query!");
		}
		query = queryToDecorate;
	}


	public String getStatement() {
		return query.getStatement();
	}

	public List getCriteria() {
		if (decoratedCriteriaList == null) {
			List queryCriteriaList = query.getCriteria();
			if ((queryCriteriaList == null) || (queryCriteriaList.size() == 0)) {
				decoratedCriteriaList = Collections.EMPTY_LIST;
				
			} else {
				Field f;
				decoratedCriteriaList = new ArrayList((int)(queryCriteriaList.size()));
				Iterator i = queryCriteriaList.iterator();
				while (i.hasNext()) {
					f = (Field)i.next();
					decoratedCriteriaList.add(new JavaField(f));
					
				}
				
			}
		}
		
		return decoratedCriteriaList;
	}

	public String getName() {
		return query.getName();
	}
	
	public String getDocumentation() {
		return query.getDocumentation();
	}

	public String getLowercaseName() {
		return StringUtils.uncapitalize(query.getName());
	}

}
