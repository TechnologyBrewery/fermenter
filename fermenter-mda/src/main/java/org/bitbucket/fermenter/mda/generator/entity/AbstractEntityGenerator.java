package org.bitbucket.fermenter.mda.generator.entity;

import java.util.Iterator;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.MetadataRepositoryManager;
import org.bitbucket.fermenter.mda.metadata.element.Entity;

public abstract class AbstractEntityGenerator extends AbstractGenerator {

	public void generate(GenerationContext context) throws GenerationException {
	    MetadataRepository metadataRepository = 
	            MetadataRepositoryManager.getMetadataRepostory(MetadataRepository.class);
		Iterator entities = metadataRepository.getAllEntities().values().iterator();
		
		String fileName;
		String basefileName = context.getOutputFile();		
		basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
		while (entities.hasNext()) {
			Entity entity = (Entity) entities.next();
			
			VelocityContext vc = new VelocityContext();
			populateVelocityContext(vc, entity, context);
			
			
			fileName = replaceEntityName(basefileName, entity.getName());
			context.setOutputFile(fileName);
			
			generateFile(context, vc);
		}
	}
	
	/**
	 * An opportunity to populate the passed <tt>VelocityContext</tt>
	 * @param vc The <tt>VelocityContext</tt> to populate
	 * @param entity The <tt>Entity</tt> from information can be pulled
	 * @param generationContext The <tt>GenerationContext</tt> of this <tt>Generator</tt>
	 */
	protected abstract void populateVelocityContext(VelocityContext vc, Entity entity, GenerationContext generationContext);

}
