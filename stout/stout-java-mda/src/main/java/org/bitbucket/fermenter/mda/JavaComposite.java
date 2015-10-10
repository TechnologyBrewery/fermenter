package org.bitbucket.fermenter.mda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.metadata.element.Composite;
import org.bitbucket.fermenter.mda.metadata.element.Field;

public class JavaComposite implements Composite {
	
	private Composite composite;
	private List decoratedFieldList;
	private Set imports;
	
	/**
	 * Create a new instance of <tt>Composite</tt> with the correct functionality set 
	 * to generate Java code
	 * @param compositeToDecorate The <tt>Composite</tt> to decorate
	 */
	public JavaComposite(Composite compositeToDecorate) {
		if (compositeToDecorate == null) {
			throw new IllegalArgumentException("JavaComposites must be instatiated with a non-null composite!");
		}
		composite = compositeToDecorate;
	}	

	public String getPrefix() {
		return composite.getPrefix();
	}
	
	/**
	 * @see org.bitbucket.fermenter.mda.metadata.element.Composite#getApplicationName()
	 */
	public String getApplicationName() { 
		return composite.getApplicationName();
	}

	public String getType() {
		return composite.getType();
	}

	public String getCapitalizedName() {
		return StringUtils.capitalize(composite.getName());
	}

	public Collection getFields() {
		if (decoratedFieldList == null) {
			Collection compositeFieldList = composite.getFields();
			if ((compositeFieldList == null) || (compositeFieldList.size() == 0)) {
				decoratedFieldList = Collections.EMPTY_LIST;
				
			} else {
				Field f;
				decoratedFieldList = new ArrayList(compositeFieldList.size());
				Iterator i = compositeFieldList.iterator();
				while (i.hasNext()) {
					f = (Field)i.next();
					decoratedFieldList.add(new JavaField(f));
					
				}
				
			}
		}
		
		return decoratedFieldList;
	}

	public String getName() {
		return composite.getName();
	}
	
	public String getProject() {
		return composite.getProject();
	}

	public String getLabel() {
		return composite.getLabel();
	}
	
	public Set getImports() {
		if (imports == null) {
			imports = new HashSet();
			imports.addAll(getFieldImports());
		}
		
		return imports;
	}
	
	public Set getFieldImports() {
		Set importSet = new HashSet();
		
		JavaField field;
		Collection fieldCollection = getFields();
		Iterator fieldIter = fieldCollection.iterator();
		while (fieldIter.hasNext()) {
			field = (JavaField)fieldIter.next();
			importSet.add(field.getImport());
		}
		
		return importSet;
	}

}
