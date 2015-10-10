package org.bitbucket.fermenter.stout.mda;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Field;

public class RelatedJavaEntity extends JavaEntity {
	
	private Entity entity;
	private Entity parentEntity;
	private Map decoratedIdFieldMap;
	
	public RelatedJavaEntity(Entity entity, Entity parentEntity) {
		super(entity);
		
		this.entity = entity;
		this.parentEntity = parentEntity;
		
	}
	
	public String getLabel() {
		return StringUtils.uncapitalise(entity.getName());
	}
	
	public Map getIdFields() {
		if (isSelfRelation().booleanValue()) {
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
						decoratedIdFieldMap.put(f.getName(), new SelfReferenceField(f));
						
					}
					
				}
			}
			
		} else {
			decoratedIdFieldMap = super.getIdFields();
		}
		
		return decoratedIdFieldMap;
	}

	public Field getIdField(String name) {
		return (Field)getIdFields().get(name);
	}
	
	
	class SelfReferenceField extends JavaField {
		
		private Field field;
		private String overriddenColumnName;
		
		SelfReferenceField(Field field) {
			super(field);
			this.field = field;
		}

		/**
		 * Prevents this field from having the same column name as its parent
		 * @see org.bitbucket.fermenter.stout.mda.element.java.JavaField#getColumn()
		 */
		public String getColumn() {
			if (overriddenColumnName == null) {
				overriddenColumnName = "FK_" + field.getColumn();
				
			}
			
			return overriddenColumnName;
		}
		
	}
	
	public Boolean isSelfRelation() {
		return (entity.getName().equals(parentEntity.getName())) ? Boolean.TRUE : Boolean.FALSE;
	}

}
