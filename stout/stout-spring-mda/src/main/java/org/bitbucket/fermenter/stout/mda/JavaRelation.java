package org.bitbucket.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.persistence.FetchType;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Relation;

public class JavaRelation implements Relation {
	
	private Relation relation;
	private Collection decoratedChildRelationCollection;
	private Collection decoratedKeyCollection;
	
	/**
	 * Create a new instance of <tt>Relation</tt> with the correct functionality set 
	 * to generate Java code
	 * @param relationToDecorate The <tt>Relation</tt> to decorate
	 */
	public JavaRelation(Relation relationToDecorate) {
		if (relationToDecorate == null) {
			throw new IllegalArgumentException("JavaRelations must be instatiated with a non-null relation!");
		}
		relation = relationToDecorate;
	}	

	public String getDocumentation() {
		return relation.getDocumentation();
	}
	
	public String getMultiplicity() {
		return relation.getMultiplicity();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Return the fetch mode, defaulting to eager unless otherwise specified.  While many would like the default to
	 * be lazy, unless you are working with very large data sizes, this often is less convenient in practice and has
	 * little performance impact.  
	 */
	@Override
	public String getFetchMode() {
		String fetchMode = relation.getFetchMode();
		return (StringUtils.isNotBlank(fetchMode)) ? fetchMode : FetchType.EAGER.toString();
	}

	public String getType() {
		return relation.getType();
	}

	public String getLabel() {
		return relation.getLabel();
	}

	public String getTable() {
		return relation.getTable();
	}

	public Collection getChildRelations() {
		if (decoratedChildRelationCollection == null) {
			Collection referenceForeignKeyFieldCollection = relation.getChildRelations();
			if ((referenceForeignKeyFieldCollection == null) || (referenceForeignKeyFieldCollection.size() == 0)) {
				decoratedChildRelationCollection = Collections.EMPTY_LIST;
				
			} else {
				Relation r;
				decoratedChildRelationCollection = new ArrayList((int)(referenceForeignKeyFieldCollection.size()));
				Iterator i = referenceForeignKeyFieldCollection.iterator();
				while (i.hasNext()) {
					r = (Relation)i.next();
					decoratedChildRelationCollection.add(new JavaRelation(r));
					
				}
				
			}
		}
		
		return decoratedChildRelationCollection;
	}

	public Collection getKeys(String parentEntityName) {
		if (decoratedKeyCollection == null) {
			Collection relationKeyCollection = relation.getKeys(parentEntityName);
			if ((relationKeyCollection == null) || (relationKeyCollection.size() == 0)) {
				decoratedKeyCollection = Collections.EMPTY_LIST;
				
			} else {
				Field f;
				decoratedKeyCollection = new ArrayList((int)(relationKeyCollection.size()));
				Iterator i = relationKeyCollection.iterator();
				while (i.hasNext()) {
					f = (Field)i.next();
					decoratedKeyCollection.add(new JavaField(f));
					
				}
				
			}
		}
		
		return decoratedKeyCollection;
	}

	//java-specific generation methods:
	
	public String getUncapitalizedType() {
		return StringUtils.uncapitalize( getType() );
	}	
	
}
