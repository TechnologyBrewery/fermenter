package org.bitbucket.fermenter.stout.mda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.fermenter.mda.PackageManager;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metadata.element.Field;
import org.bitbucket.fermenter.mda.metadata.element.Reference;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

public class JavaReference implements Reference {
	
	private Reference reference;
	private List decoratedForeignKeyFieldList;
	
	/**
	 * Create a new instance of <tt>Reference</tt> with the correct functionality set 
	 * to generate Java code
	 * @param referenceToDecorate The <tt>Reference</tt> to decorate
	 */
	public JavaReference(Reference referenceToDecorate) {
		if (referenceToDecorate == null) {
			throw new IllegalArgumentException("JavaReferences must be instatiated with a non-null reference!");
		}
		reference = referenceToDecorate;
	}	

	public String getType() {
		return reference.getType();
	}

	public String getLabel() {
		return reference.getLabel();
	}

	public String getName() {
		return reference.getName();
	}
	
	public String getDocumentation() {
		return reference.getDocumentation();
	}

	public String getCapitalizedName() {
		return StringUtils.capitalize(reference.getName());
	}

	public List getForeignKeyFields() {
		if (decoratedForeignKeyFieldList == null) {
			List referenceForeignKeyFieldList = reference.getForeignKeyFields();
			if ((referenceForeignKeyFieldList == null) || (referenceForeignKeyFieldList.size() == 0)) {
				decoratedForeignKeyFieldList = Collections.EMPTY_LIST;
				
			} else {
				Field f;
				decoratedForeignKeyFieldList = new ArrayList((int)(referenceForeignKeyFieldList.size()));
				Iterator i = referenceForeignKeyFieldList.iterator();
				while (i.hasNext()) {
					f = (Field)i.next();
					decoratedForeignKeyFieldList.add(new JavaField(f));
				}
			}
		}
		
		return decoratedForeignKeyFieldList;
	}

	public String getRequired() {
		return reference.getRequired();
	}

	public boolean isRequired() {
		return reference.isRequired();
	}

	public String getProject() {
		return reference.getProject();
	}
	
	public boolean isExternal() {
		MetadataRepository metadataRepository = 
                ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		String currentProject = metadataRepository.getApplicationName();
		String basePackage = PackageManager.getBasePackage(currentProject);
		Entity referenceEntity = metadataRepository.getAllEntities().get(getType());
		String namespace = referenceEntity.getNamespace();
		return !StringUtils.isBlank(namespace) && !namespace.equals(basePackage);
	}
	
	public String getImportPrefix() {
	    MetadataRepository metadataRepository = 
                ModelInstanceRepositoryManager.getMetadataRepostory(MetadataRepository.class);
	    Entity referenceEntity = metadataRepository.getAllEntities().get(getType());
	    String namespace = referenceEntity.getNamespace();
		return StringUtils.isBlank(namespace) ? PackageManager.getBasePackage(getProject()) : namespace;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Set getFkImports() {
		Set importSet = new HashSet( );
		
		if( ! isExternal() ) {
			return importSet;
		}
		
		JavaField fk;
		Iterator fks = getForeignKeyFields().iterator();
		while (fks.hasNext()) {
			fk = (JavaField) fks.next();
			importSet.add(fk.getImport());
		}		
		
		return importSet;
	}

	
	public String getFkCondition() {
		if( ! isExternal() ) {
			return null;
		}
		
		StringBuffer sb = new StringBuffer(100);
		JavaField fk;
		Iterator fks = getForeignKeyFields().iterator();
		while (fks.hasNext()) {
			fk = (JavaField) fks.next();
			sb.append( "get" );
			sb.append( getCapitalizedName() );
			sb.append( fk.getCapitalizedName() );
			sb.append( "() == null" );
			if( fks.hasNext() ) {
				sb.append( "&&" );
			}
		}
		
		return sb.toString();
	}
	
	public String getUncapitalizedType() {
		return StringUtils.uncapitalize( getType() );
	}	
	
	public String getImport() {
	    return JavaElementUtils.getJavaImportType("", getType());
	}
	
}
