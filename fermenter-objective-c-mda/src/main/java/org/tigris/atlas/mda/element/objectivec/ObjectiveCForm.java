package org.tigris.atlas.mda.element.objectivec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.tigris.atlas.mda.metadata.element.Field;
import org.tigris.atlas.mda.metadata.element.Form;
import org.tigris.atlas.mda.metadata.element.FormEntity;

public class ObjectiveCForm implements Form {

	private Form form;
	private Map entities;
	private Collection imports;
	private List enumerations;
	private List formOnlyFields;

	public ObjectiveCForm(Form formToDecorate) {
		if (formToDecorate == null) {
			throw new IllegalArgumentException("ObjectiveCForm must be instatiated with a non-null form!");
		}
		form = formToDecorate;
	}

	/**
	 * @see org.tigris.atlas.mda.metadata.element.Composite#getApplicationName()
	 */
	@Override
	public String getApplicationName() {
		return form.getApplicationName();
	}

	@Override
	public Collection getFormOnlyFields() {
		if (formOnlyFields == null) {
			formOnlyFields = new ArrayList();
			for (Iterator i = form.getFormOnlyFields().iterator(); i.hasNext();) {
				Field temp = (Field) i.next();
				ObjectiveCEnumerationField field = new ObjectiveCEnumerationField(null, new ObjectiveCField(temp));
				formOnlyFields.add(field);
			}
		}
		return formOnlyFields;
	}

	public List getEnumerations() {
		if (enumerations == null) {
			enumerations = new ArrayList();
			for (Iterator i = getEntities().iterator(); i.hasNext();) {
				ObjectiveCFormEntity entity = (ObjectiveCFormEntity) i.next();
				for (Iterator j = entity.getFields().iterator(); j.hasNext();) {
					ObjectiveCField f = (ObjectiveCField) j.next();
					ObjectiveCEnumerationField enumField = new ObjectiveCEnumerationField(entity, f);
					if (f.isEnumerationType().booleanValue()) {
						enumerations.add(enumField);
					}
				}
			}
		}
		return enumerations;
	}

	public Boolean hasEnumerations() {
		return new Boolean(!getEnumerations().isEmpty());
	}

	@Override
	public String getName() {
		return form.getName();
	}

	public Collection getImports() {
		if (imports == null) {
			imports = new HashSet();
			imports.addAll(getEntityImports());
		}

		return imports;
	}

	private Collection getEntityImports() {
		Collection imports = new HashSet();
		for (Iterator i = getEntities().iterator(); i.hasNext();) {
			ObjectiveCFormEntity entity = (ObjectiveCFormEntity) i.next();
			imports.add(entity.getImport());
			imports.addAll(entity.getFieldImports());
		}
		return imports;
	}

	@Override
	public Collection getEntities() {
		if (entities == null) {
			entities = new TreeMap();
			for (Iterator i = form.getEntities().iterator(); i.hasNext();) {
				FormEntity entity = (FormEntity) i.next();
				entities.put(entity.getName(), new ObjectiveCFormEntity(entity));
			}
		}
		return entities.values();
	}

	/**
	 * Get the signature for the copyTo and copyFrom methods
	 *
	 * @return String The signature
	 */
	public String getCopySignature() {
		StringBuffer params = new StringBuffer(100);
		for (Iterator i = getEntities().iterator(); i.hasNext();) {
			ObjectiveCFormEntity jEntity = (ObjectiveCFormEntity) i.next();
			params.append(jEntity.getType());
			params.append( " " );
			params.append( jEntity.getUncapitalizedName() );
			if (i.hasNext()) {
				params.append( ", " );
			}
		}

		return params.toString();
	}

}
