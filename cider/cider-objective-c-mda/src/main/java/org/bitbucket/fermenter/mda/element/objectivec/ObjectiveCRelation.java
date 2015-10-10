package org.bitbucket.fermenter.mda.element.objectivec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Relation;

public class ObjectiveCRelation implements Relation {

	private Relation relation;
	private Collection<ObjectiveCRelation> decoratedChildRelationCollection;
	private Collection<ObjectiveCField> decoratedKeyCollection;

	/**
	 * Create a new instance of <tt>Relation</tt> with the correct functionality set
	 * to generate Objective-C code
	 * @param relationToDecorate The <tt>Relation</tt> to decorate
	 */
	public ObjectiveCRelation(Relation relationToDecorate) {
		if (relationToDecorate == null) {
			throw new IllegalArgumentException("ObjectiveCRelations must be instatiated with a non-null relation!");
		}
		relation = relationToDecorate;
	}

	@Override
	public String getDocumentation() {
		return relation.getDocumentation();
	}

	@Override
	public String getMultiplicity() {
		return relation.getMultiplicity();
	}

	@Override
	public String getType() {
		return ObjectiveCElementUtils.getObjectiveCType(MetadataRepository.getInstance().getApplicationName(), relation.getType());
	}

	public String getUncapitalizedType() {
		return StringUtils.uncapitalize(getType());
	}

	public String getWrappedType() {
		return relation.getType();
	}

	public String getUncapitalizedWrappedType() {
		return StringUtils.uncapitalize(getWrappedType());
	}

	public Entity getTypeEntity() {
		return ObjectiveCElementUtils.getObjectiveCEntity(relation.getType());
	}

	public String getTypeAttributes() {
		return "nonatomic, copy";
	}

	public String getSerializedName() {
		return relation.getLabel() + "s";
	}

	@Override
	public String getLabel() {
		return relation.getLabel();
	}

	@Override
	public String getTable() {
		return relation.getTable();
	}

	@Override
	public Collection<ObjectiveCRelation> getChildRelations() {
		if (decoratedChildRelationCollection == null) {
			@SuppressWarnings("unchecked")
			Collection<Relation> referenceForeignKeyFieldCollection = relation.getChildRelations();
			if ((referenceForeignKeyFieldCollection == null) || (referenceForeignKeyFieldCollection.size() == 0)) {
				decoratedChildRelationCollection = Collections.<ObjectiveCRelation>emptyList();

			}
			else {
				decoratedChildRelationCollection = new ArrayList<ObjectiveCRelation>(referenceForeignKeyFieldCollection.size());
				for (Relation r : referenceForeignKeyFieldCollection) {
					decoratedChildRelationCollection.add(new ObjectiveCRelation(r));
				}

			}
		}

		return decoratedChildRelationCollection;
	}

	@Override
	public Collection<ObjectiveCField> getKeys() {
		if (decoratedKeyCollection == null) {
			@SuppressWarnings("unchecked")
			Collection<Field> relationKeyCollection = relation.getKeys();
			if ((relationKeyCollection == null) || (relationKeyCollection.size() == 0)) {
				decoratedKeyCollection = Collections.<ObjectiveCField>emptyList();
			}
			else {
				decoratedKeyCollection = new ArrayList<ObjectiveCField>(relationKeyCollection.size());
				for (Field f : relationKeyCollection) {
					decoratedKeyCollection.add(new ObjectiveCField(f));
				}
			}
		}

		return decoratedKeyCollection;
	}
}
