package org.bitbucket.fermenter.mda.metadata;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.bitbucket.fermenter.mda.metadata.element.CompositeInstanceMetadata;
import org.bitbucket.fermenter.mda.metadata.element.EntityMetadata;
import org.bitbucket.fermenter.mda.metadata.element.EnumerationLabel;
import org.bitbucket.fermenter.mda.metadata.element.Form;
import org.bitbucket.fermenter.mda.metadata.element.FormEntityMetadata;
import org.bitbucket.fermenter.mda.metadata.element.FormFieldMetadata;
import org.bitbucket.fermenter.mda.metadata.element.FormMetadata;
import org.bitbucket.fermenter.mda.metadata.element.RelationMetadata;

/**
 * Responsible for maintaining the list of form metadata elements in the system.
 * 
 * @author Steve Andrews
 *
 */
class FormMetadataManager extends MetadataManager {
	

    private static FormMetadataManager INSTANCE = null;
	
    public static FormMetadataManager getInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new FormMetadataManager();
		}
		
		return INSTANCE;
	}
	
	protected FormMetadataManager() {
		super();
	}
	
	public void addForm(FormMetadata form) {
		form.setApplicationName(currentApplication);
		addMetadataElement( form.getName(), form );
	}
	
	/**
	 * Answer the metadata for a specified form
	 * @param formName
	 * @return
	 */
	public static Form getForm(String applicationName, String formName) {
		return (Form) getInstance().getMetadataMap( applicationName ).get( formName );
	}

	/**
	 * Get the full collection 
	 * @return
	 */
	public static Map getForms(String applicationName) {
		return getInstance().getMetadataMap( applicationName );
	}
	
	/**
	 * Returns the metadata element by name from any application that is loaded
	 * @param name The name by which to retrieve
	 * @return The <tt>Form</tt> instance for <tt>name</tt> or null
	 */
	public static Form getFormMetadata(String name) {
		Map map = getInstance().getCompleteMetadataMap();
		return (map != null) ? (Form)map.get(name) : null;
	}
	
	public static boolean isLoaded(String applicationName) {
		return ( getInstance().getMetadataMap( applicationName ) != null ) ? true : false;
	}
	
	protected String getMetadataLocation() {
		return "forms";
	}

	/**
	 * Sets up the parsing rules for the digester
	 * 
	 * @param digester Parses the metadata file into the metadata elements
	 */
	protected void initialize(Digester digester) {
		digester.addObjectCreate(	"form", 								FormMetadata.class.getName()							);
		digester.addCallMethod( 	"form/name", 							"setName", 				0						);
		digester.addObjectCreate("form/formOnlyField", FormFieldMetadata.class);
		digester.addSetProperties("form/formOnlyField");
		digester.addSetNext("form/formOnlyField", "addFormOnlyField");
		digester.addObjectCreate( 	"form/entity", 				FormEntityMetadata.class.getName() 				);
		digester.addCallMethod( 	"form/entity/type", 			"setType", 				0	);
		digester.addCallMethod( 	"form/entity/name", 			"setName", 				0	);
		digester.addCallMethod( 	"form/entity/project", 			"setProject", 				0						);
		digester.addObjectCreate( 	"form/entity/field",	FormFieldMetadata.class.getName() 							);
		digester.addSetProperties( 	"form/entity/field" 													);
		digester.addSetNext( 		"form/entity/field", 	"addField", 			FormFieldMetadata.class.getName() 	);
		digester.addObjectCreate("form/entity/composite", CompositeInstanceMetadata.class);
		digester.addSetProperties("form/entity/composite");
		digester.addSetNext("form/entity/composite", "addComposite", CompositeInstanceMetadata.class.getName());
		digester.addObjectCreate("form/entity/field/labelForName", EnumerationLabel.class);
		digester.addCallMethod( 	"form/entity/field/labelForName", 			"setLabel", 				0	);
		digester.addSetProperties("form/entity/field/labelForName");
		digester.addSetNext("form/entity/field/labelForName", "addEnumerationLabel", EnumerationLabel.class.getName());
		digester.addSetNext( 		"form/entity", "addEntity", 	FormEntityMetadata.class.getName() 				);
		digester.addSetNext( 		"form", 								"addForm", 				FormMetadata.class.getName() 	);
	}	
	
}
