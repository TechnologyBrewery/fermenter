package org.tigris.atlas.mda.element.objectivec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.tigris.atlas.mda.metadata.element.Field;
import org.tigris.atlas.mda.metadata.element.Relation;

public class ObjectiveCRelation implements Relation {

	private Relation relation;
	private Collection decoratedChildRelationCollection;
	private Collection decoratedKeyCollection;

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
		return relation.getType();
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
	public Collection getChildRelations() {
		if (decoratedChildRelationCollection == null) {
			Collection referenceForeignKeyFieldCollection = relation.getChildRelations();
			if ((referenceForeignKeyFieldCollection == null) || (referenceForeignKeyFieldCollection.size() == 0)) {
				decoratedChildRelationCollection = Collections.EMPTY_LIST;

			} else {
				Relation r;
				decoratedChildRelationCollection = new ArrayList((referenceForeignKeyFieldCollection.size()));
				Iterator i = referenceForeignKeyFieldCollection.iterator();
				while (i.hasNext()) {
					r = (Relation)i.next();
					decoratedChildRelationCollection.add(new ObjectiveCRelation(r));

				}

			}
		}

		return decoratedChildRelationCollection;
	}

	@Override
	public Collection getKeys() {
		if (decoratedKeyCollection == null) {
			Collection relationKeyCollection = relation.getKeys();
			if ((relationKeyCollection == null) || (relationKeyCollection.size() == 0)) {
				decoratedKeyCollection = Collections.EMPTY_LIST;

			} else {
				Field f;
				decoratedKeyCollection = new ArrayList((relationKeyCollection.size()));
				Iterator i = relationKeyCollection.iterator();
				while (i.hasNext()) {
					f = (Field)i.next();
					decoratedKeyCollection.add(new ObjectiveCField(f));

				}

			}
		}

		return decoratedKeyCollection;
	}

	public String getUncapitalizedType() {
		return StringUtils.uncapitalize( getType() );
	}

}
