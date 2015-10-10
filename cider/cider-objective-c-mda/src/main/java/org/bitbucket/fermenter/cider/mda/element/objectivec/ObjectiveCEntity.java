package org.bitbucket.fermenter.cider.mda.element.objectivec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.cider.mda.objectivec.ObjectiveCTypeManager;
import org.bitbucket.fermenter.mda.metadata.element.Composite;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Query;
import org.bitbucket.fermenter.mda.metadata.element.Reference;
import org.bitbucket.fermenter.mda.metadata.element.Relation;

public class ObjectiveCEntity implements Entity {

	private Entity entity;
	private Map<String, ObjectiveCField> decoratedFieldMap;
	private Map<String, ObjectiveCField> decoratedIdFieldMap;
	private Map decoratedCompositeMap;
	private Map decoratedRelationMap;
	private Map decoratedInverseRelationMap;
	private Map decoratedReferenceMap;
	private Map decoratedQueryMap;
	private Set<String> imports;

	private String keySignature;
	private String keySignatureParams;

	/**
	 * Create a new instance of <tt>Entity</tt> with the correct functionality set
	 * to generate Objective-C code
	 * @param entityToDecorate The <tt>Entity</tt> to decorate
	 */
	public ObjectiveCEntity(Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("ObjectiveCEntity must be instatiated with a non-null entity!");
		}
		this.entity = entity;
	}

	@Override
	public String getName() {
		return ObjectiveCTypeManager.getObjectiveCClassPrefix() + entity.getName();
	}

	@Override
	public String getDocumentation() {
		return entity.getDocumentation();
	}

	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Composite#getApplicationName()
	 */
	@Override
	public String getApplicationName() {
		return entity.getApplicationName();
	}

	@Override
	public String getTable() {
		return entity.getTable();
	}

	@Override
	public Map<String, ObjectiveCField> getFields() {
		if (decoratedFieldMap == null) {
			@SuppressWarnings("unchecked")
			Map<String, Field> entityFieldMap = entity.getFields();
			if ((entityFieldMap == null) || (entityFieldMap.isEmpty())) {
				decoratedFieldMap = Collections.<String, ObjectiveCField>emptyMap();
			}
			else {
				Field f;
				decoratedFieldMap = new HashMap<String, ObjectiveCField>((int)(entityFieldMap.size() * 1.25));
				Iterator<Field> i = entityFieldMap.values().iterator();
				while (i.hasNext()) {
					f = i.next();
					decoratedFieldMap.put(f.getName(), new ObjectiveCField(f));
				}
			}
		}

		return decoratedFieldMap;
	}

	@Override
	public Field getField(String name) {
		return getFields().get(name);
	}

	@Override
	public Map<String, ObjectiveCField> getIdFields() {
		if (decoratedIdFieldMap == null) {
			@SuppressWarnings("unchecked")
			Map<String, Field> entityIdFieldMap = entity.getIdFields();
			if ((entityIdFieldMap == null) || (entityIdFieldMap.isEmpty())) {
				decoratedIdFieldMap = Collections.<String, ObjectiveCField>emptyMap();
			}
			else {
				Field f;
				decoratedIdFieldMap = new HashMap<String, ObjectiveCField>((int)(entityIdFieldMap.size() * 1.25));
				Iterator<Field> i = entityIdFieldMap.values().iterator();
				while (i.hasNext()) {
					f = i.next();
					decoratedIdFieldMap.put(f.getName(), new ObjectiveCField(f));
				}
			}
		}

		return decoratedIdFieldMap;
	}

	@Override
	public Field getIdField(String name) {
		return getIdFields().get(name);
	}

	@Override
	public Map getComposites() {
		if (decoratedCompositeMap == null) {
			Map entityCompositeMap = entity.getComposites();
			if ((entityCompositeMap == null) || (entityCompositeMap.isEmpty())) {
				decoratedCompositeMap = Collections.EMPTY_MAP;

			} else {
				Composite c;
				decoratedCompositeMap = new HashMap((int)(entityCompositeMap.size() * 1.25));
				Iterator i = entityCompositeMap.values().iterator();
				while (i.hasNext()) {
					c = (Composite)i.next();
					decoratedCompositeMap.put(c.getName(), new ObjectiveCComposite(c));

				}

			}
		}

		return decoratedCompositeMap;
	}

	@Override
	public Composite getComposite(String name) {
		return (Composite)getComposites().get(name);
	}

	@Override
	public Map getRelations() {
		if (decoratedRelationMap == null) {
			Map entityRelationMap = entity.getRelations();
			if ((entityRelationMap == null) || (entityRelationMap.isEmpty())) {
				decoratedRelationMap = Collections.EMPTY_MAP;

			} else {
				Relation r;
				decoratedRelationMap = new HashMap((int)(entityRelationMap.size() * 1.25));
				Iterator i = entityRelationMap.values().iterator();
				while (i.hasNext()) {
					r = (Relation)i.next();
					decoratedRelationMap.put(r.getType(), new ObjectiveCRelation(r));

				}

			}
		}

		return decoratedRelationMap;
	}

	@Override
	public Relation getRelation(String type) {
		return (Relation)getRelations().get(type);
	}

	@Override
	public Map getInverseRelations() {
		if (decoratedInverseRelationMap == null) {
			Map entityInverseRelationMap = entity.getInverseRelations();
			if ((entityInverseRelationMap == null) || (entityInverseRelationMap.isEmpty())) {
				decoratedInverseRelationMap = Collections.EMPTY_MAP;

			}
			else {
				Entity r;
				decoratedInverseRelationMap = new HashMap((int)(entityInverseRelationMap.size() * 1.25));
				Iterator i = entityInverseRelationMap.values().iterator();
				while (i.hasNext()) {
					r = (Entity)i.next();
					decoratedInverseRelationMap.put(r.getName(), new RelatedObjectiveCEntity(r, this));
				}
			}
		}

		return decoratedInverseRelationMap;
	}

	@Override
	public Relation getInverseRelation(String type) {
		return (Relation)getInverseRelations().get(type);
	}

	@Override
	public Map<String, ObjectiveCReference> getReferences() {
		if (decoratedReferenceMap == null) {
			Map entityReferenceMap = entity.getReferences();
			if ((entityReferenceMap == null) || (entityReferenceMap.isEmpty())) {
				decoratedReferenceMap = Collections.EMPTY_MAP;
			}
			else {
				Reference r;
				decoratedReferenceMap = new HashMap<String, ObjectiveCReference>((int)(entityReferenceMap.size() * 1.25));
				Iterator i = entityReferenceMap.values().iterator();
				while (i.hasNext()) {
					r = (Reference)i.next();
					decoratedReferenceMap.put(r.getName(), new ObjectiveCReference(r));

				}

			}
		}

		return decoratedReferenceMap;
	}

	@Override
	public Reference getReference(String type) {
		return getReferences().get(type);
	}

	@Override
	public String getSuperclass() {
		return entity.getSuperclass();
	}

	@Override
	public String getParent() {
		return entity.getParent();
	}

	@Override
	public Map getQueries() {
		if (decoratedQueryMap == null) {
			Map entityQueryMap = entity.getQueries();
			if ((entityQueryMap == null) || (entityQueryMap.isEmpty())) {
				decoratedQueryMap = Collections.EMPTY_MAP;

			} else {
				Query q;
				decoratedQueryMap = new HashMap((int)(entityQueryMap.size() * 1.25));
				Iterator i = entityQueryMap.values().iterator();
				while (i.hasNext()) {
					q = (Query)i.next();
					decoratedQueryMap.put(q.getName(), new ObjectiveCQuery(q));

				}

			}
		}

		return decoratedQueryMap;
	}

	@Override
	public Query getQuery(String name) {
		return (Query)getQueries().get(name);
	}

	@Override
	public String getLockStrategy() {
		return entity.getLockStrategy();
	}

	@Override
	public boolean useOptimisticLocking() {
		return entity.useOptimisticLocking();
	}

	public String getUncapitalizedName() {
		return StringUtils.uncapitalize( getName() );
	}

	public Set<String> getImports() {
		if (imports == null) {
			imports = new HashSet<String>();
			imports.addAll(getFieldImports());
			imports.addAll(getReferenceImports());
		}

		return imports;
	}

	public Set<String> getFieldImports() {
		Set<String> importSet = new HashSet<String>();

		for (ObjectiveCField field : getFields().values()) {
			String fieldImport = field.getImport();
			if (fieldImport != null) {
				importSet.add(fieldImport);
			}
		}

		return importSet;
	}

	public Set<String> getReferenceImports() {
		Set<String> importSet = new HashSet<String>();

		for (ObjectiveCReference reference : getReferences().values()) {
			String referenceImport = reference.getImport();
			if (referenceImport != null) {
				importSet.add(referenceImport);
			}
		}

		return importSet;
	}

	/**
	 * Returns a Objective-C method's signature  based on this instance's key fields
	 * @return The key signature
	 */
	public String getKeySignature() {
		if (keySignature == null) {
			Map idFieldMap = getIdFields();
			if (idFieldMap !=  null) {
				List keyList = new ArrayList(idFieldMap.values());
				keySignature = ObjectiveCElementUtils.createSignatureFields(keyList);

			}
		}

		return keySignature;
	}

	/**
	 * Returns a Objective-C signature's parameters based on this instance's key fields
	 * @return The key signature params
	 */
	public String getKeySignatureParams() {
		if (keySignatureParams == null) {
			Map idFieldMap = getIdFields();
			if (idFieldMap !=  null) {
				List keyList = new ArrayList(idFieldMap.values());
				keySignatureParams = ObjectiveCElementUtils.createSignatureFieldParams(keyList);

			}
		}

		return keySignatureParams;
	}

	public Collection getImportPrefixes() {
		Collection prefixes = new HashSet();

		for (Iterator i = getReferences().values().iterator(); i.hasNext();) {
			ObjectiveCReference ref = (ObjectiveCReference) i.next();
			prefixes.add(ref.getImportPrefix());
		}

		return prefixes;
	}

}
