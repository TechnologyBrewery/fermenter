package org.tigris.atlas.mda.element.java;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.tigris.atlas.mda.metadata.element.Composite;
import org.tigris.atlas.mda.metadata.element.Entity;
import org.tigris.atlas.mda.metadata.element.Field;
import org.tigris.atlas.mda.metadata.element.Query;
import org.tigris.atlas.mda.metadata.element.Reference;
import org.tigris.atlas.mda.metadata.element.Relation;

public class JavaEntity implements Entity {
	
	private Entity entity;
	private Map decoratedFieldMap;
	private Map decoratedIdFieldMap;
	private Map decoratedCompositeMap;
	private Map decoratedRelationMap;
	private Map decoratedInverseRelationMap;
	private Map decoratedReferenceMap;
	private Map decoratedQueryMap;
	private Set imports;
	
	private String keySignature;
	private String keySignatureParams;
	
	/**
	 * Create a new instance of <tt>Entity</tt> with the correct functionality set 
	 * to generate Java code
	 * @param entityToDecorate The <tt>Entity</tt> to decorate
	 */
	public JavaEntity(Entity entityToDecorate) {
		if (entityToDecorate == null) {
			throw new IllegalArgumentException("JavaEntity must be instatiated with a non-null entity!");
		}
		entity = entityToDecorate;
	}
	
	public String getName() {
		return entity.getName();
	}
	
	public String getDocumentation() {
		return entity.getDocumentation();
	}
	
	/**
	 * @see org.tigris.atlas.mda.metadata.element.Composite#getApplicationName()
	 */
	public String getApplicationName() {
		return entity.getApplicationName();
	}

	public String getTable() {
		return entity.getTable();
	}

	public Map getFields() {
		if (decoratedFieldMap == null) {
			Map entityFieldMap = entity.getFields();
			if ((entityFieldMap == null) || (entityFieldMap.size() == 0)) {
				decoratedFieldMap = Collections.EMPTY_MAP;
				
			} else {
				Field f;
				decoratedFieldMap = new HashMap((int)(entityFieldMap.size() * 1.25));
				Iterator i = entityFieldMap.values().iterator();
				while (i.hasNext()) {
					f = (Field)i.next();
					JavaField jField = new JavaField(f);
					decoratedFieldMap.put(f.getName(), jField);
				}
				
			}
		}
		
		return decoratedFieldMap;
	}

	public Field getField(String name) {
		return (Field)getFields().get(name);
	}

	public Map getIdFields() {
		if (decoratedIdFieldMap == null) {
			Map entityIdFieldMap = entity.getIdFields();
			if ((entityIdFieldMap == null) || (entityIdFieldMap.size() == 0)) {
				decoratedIdFieldMap = Collections.EMPTY_MAP;
				
			} else {
				Field f;
				decoratedIdFieldMap = new HashMap((int)(entityIdFieldMap.size() * 1.25));
				Iterator i = entityIdFieldMap.values().iterator();
				while (i.hasNext()) {
					f = (Field)i.next();
					decoratedIdFieldMap.put(f.getName(), new JavaField(f));
					
				}
				
			}
		}
		
		return decoratedIdFieldMap;
	}

	public Field getIdField(String name) {
		return (Field)getIdFields().get(name);
	}

	public Map getComposites() {
		if (decoratedCompositeMap == null) {
			Map entityCompositeMap = entity.getComposites();
			if ((entityCompositeMap == null) || (entityCompositeMap.size() == 0)) {
				decoratedCompositeMap = Collections.EMPTY_MAP;
				
			} else {
				Composite c;
				decoratedCompositeMap = new HashMap((int)(entityCompositeMap.size() * 1.25));
				Iterator i = entityCompositeMap.values().iterator();
				while (i.hasNext()) {
					c = (Composite)i.next();
					decoratedCompositeMap.put(c.getName(), new JavaComposite(c));
					
				}
				
			}
		}
		
		return decoratedCompositeMap;
	}

	public Composite getComposite(String name) {
		return (Composite)getComposites().get(name);
	}

	public Map getRelations() {
		if (decoratedRelationMap == null) {
			Map entityRelationMap = entity.getRelations();
			if ((entityRelationMap == null) || (entityRelationMap.size() == 0)) {
				decoratedRelationMap = Collections.EMPTY_MAP;
				
			} else {
				Relation r;
				decoratedRelationMap = new HashMap((int)(entityRelationMap.size() * 1.25));
				Iterator i = entityRelationMap.values().iterator();
				while (i.hasNext()) {
					r = (Relation)i.next();
					decoratedRelationMap.put(r.getType(), new JavaRelation(r));
					
				}
				
			}
		}
		
		return decoratedRelationMap;
	}

	public Relation getRelation(String type) {
		return (Relation)getRelations().get(type);
	}

	public Map getInverseRelations() {
		if (decoratedInverseRelationMap == null) {
			Map entityInverseRelationMap = entity.getInverseRelations();
			if ((entityInverseRelationMap == null) || (entityInverseRelationMap.size() == 0)) {
				decoratedInverseRelationMap = Collections.EMPTY_MAP;
				
			} else {
				Entity r;
				decoratedInverseRelationMap = new HashMap((int)(entityInverseRelationMap.size() * 1.25));
				Iterator i = entityInverseRelationMap.values().iterator();
				while (i.hasNext()) {
					r = (Entity)i.next();
					decoratedInverseRelationMap.put(r.getName(), new RelatedJavaEntity(r, this));			
					
				}
				
			}
		}
		
		return decoratedInverseRelationMap;
	}

	public Relation getInverseRelation(String type) {
		return (Relation)getInverseRelations().get(type);
	}

	public Map getReferences() {
		if (decoratedReferenceMap == null) {
			Map entityReferenceMap = entity.getReferences();
			if ((entityReferenceMap == null) || (entityReferenceMap.size() == 0)) {
				decoratedReferenceMap = Collections.EMPTY_MAP;
				
			} else {
				Reference r;
				decoratedReferenceMap = new HashMap((int)(entityReferenceMap.size() * 1.25));
				Iterator i = entityReferenceMap.values().iterator();
				while (i.hasNext()) {
					r = (Reference)i.next();
					decoratedReferenceMap.put(r.getName(), new JavaReference(r));
					
				}
				
			}
		}
		
		return decoratedReferenceMap;
	}

	public Reference getReference(String type) {
		return (Reference)getReferences().get(type);
	}
	
	//TODO: Probably shouldn't be here... will come back
	public String getSuperclass() {
		return entity.getSuperclass();
	}
	
	//TODO: Probably shouldn't be here... will come back
	public String getParent() {
		return entity.getParent();
	}

	public Map getQueries() {
		if (decoratedQueryMap == null) {
			Map entityQueryMap = entity.getQueries();
			if ((entityQueryMap == null) || (entityQueryMap.size() == 0)) {
				decoratedQueryMap = Collections.EMPTY_MAP;
				
			} else {
				Query q;
				decoratedQueryMap = new HashMap((int)(entityQueryMap.size() * 1.25));
				Iterator i = entityQueryMap.values().iterator();
				while (i.hasNext()) {
					q = (Query)i.next();
					decoratedQueryMap.put(q.getName(), new JavaQuery(q));
					
				}
				
			}
		}
		
		return decoratedQueryMap;
	}

	public Query getQuery(String name) {
		return (Query)getQueries().get(name);
	}

	public String getLockStrategy() {
		return entity.getLockStrategy();
	}

	public boolean useOptimisticLocking() {
		return entity.useOptimisticLocking();
	}
	
	//java-specific generation methods:
	
	public String getUncapitalizedName() {
		return StringUtils.uncapitalize( getName() );
	}	
	
	public Set getImports() {
		if (imports == null) {
			imports = new HashSet();
			imports.addAll(getFieldImports());
			imports.addAll(getReferenceImports());
		}
		
		return imports;
	}

	public Set getFieldImports() {
		Set importSet = new HashSet();
		importSet.add(Date.class.getName());
		importSet.add(Timestamp.class.getName());
		importSet.add(BigDecimal.class.getName());
		
		JavaField field;
		Map operationCollection = getFields();
		Iterator operationIterator = operationCollection.values().iterator();
		while (operationIterator.hasNext()) {
			field = (JavaField)operationIterator.next();
			importSet.add(field.getImport());
		}
		
		return importSet;
	}
	
	public Set getReferenceImports() {
		Set importSet = new HashSet();
		Set fkSet;
		
		JavaReference reference;
		Iterator references = getReferences().values().iterator();
		while( references.hasNext() ) {
			reference = (JavaReference) references.next();
			fkSet = reference.getFkImports();
			if (fkSet != null) {
				importSet.addAll( fkSet );
			}
		}
		
		return importSet;
	}
	
	/**
	 * Returns a Java method's signature  based on this instance's key fields
	 * @return The key signature
	 */
	public String getKeySignature() {
		if (keySignature == null) {
			Map idFieldMap = getIdFields();
			if (idFieldMap !=  null) {
				List keyList = new ArrayList(idFieldMap.values());
				keySignature = JavaElementUtils.createSignatureFields(keyList);
				
			}				
		}
		
		return keySignature;
	}
	
	/**
	 * Returns a Java signature's parameters based on this instance's key fields
	 * @return The key signature params
	 */
	public String getKeySignatureParams() {
		if (keySignatureParams == null) {
			Map idFieldMap = getIdFields();
			if (idFieldMap !=  null) {
				List keyList = new ArrayList(idFieldMap.values());
				keySignatureParams = JavaElementUtils.createSignatureFieldParams(keyList);
				
			}				
		}

		return keySignatureParams;
	}
	
	public Collection getImportPrefixes() {
		Collection prefixes = new HashSet();
		
		for (Iterator i = getReferences().values().iterator(); i.hasNext();) {
			JavaReference ref = (JavaReference) i.next();
			prefixes.add(ref.getImportPrefix());
		}
		
		return prefixes;
	}

}
