package org.bitbucket.fermenter.mda.metadata.element;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metamodel.MetadataRepositoryManager;



public class ParameterMetadata extends MetadataElement implements Parameter {

	private String name;
	private String documentation;
	private String type;
	private String project;
	private Boolean many = Boolean.FALSE;
	
	private static Log log = LogFactory.getLog(ParameterMetadata.class);
	
	/*
	 * @see org.bitbucket.fermenter.mda.metadata.ParameterInterface#getName()
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/*
	 * @see org.bitbucket.fermenter.mda.metadata.ParameterInterface#getDocumentation()
	 */
	public String getDocumentation() {
		return documentation;
	}
	
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/*
	 * @see org.bitbucket.fermenter.mda.metadata.ParameterInterface#getType()
	 */
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}	
	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * Executed to ensure that valid combinations of metadata have been loaded.
	 *
	 */
	public void validate() {
		if (project != null) {
		    MetadataRepository metadataRepository = 
                    MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
			Map entityMap = metadataRepository.getAllEntities(project);
			if (entityMap != null) {
				Object crossProjectType = entityMap.get(type);
				if (crossProjectType == null) {
					log.error("The type '" + project + "." + type + "' does not exist!"); 
				}
			}
		}
		
	}

	public boolean isMany() {
		return many.booleanValue();
	}
	
	public void setMany(String many) {
		if (many != null && many.equals("true")) {
			this.many = Boolean.TRUE;
		} else {
			this.many = Boolean.FALSE;
		}
	}

}
