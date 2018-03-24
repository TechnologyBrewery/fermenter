package org.bitbucket.fermenter.mda.generator.entity;

import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.MetadataRepositoryManager;

/**
 * Iterates through each entity in the meta-model and enables the generation of a single file for each entity.
 */
public abstract class AbstractEntityGenerator extends AbstractGenerator {

    public void generate(GenerationContext context) throws GenerationException {
        String currentApplication = context.getArtifactId();

        MetadataRepository metadataRepository = MetadataRepositoryManager
                .getMetadataRepostory(MetadataRepository.class);
        Map<String, Entity> entityMap = metadataRepository.getEntitiesByMetadataContext(metadataContext,
                currentApplication);
        Iterator<Entity> entities = entityMap.values().iterator();

        String fileName;
        String basefileName = context.getOutputFile();
        basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
        while (entities.hasNext()) {
            Entity entity = (Entity) entities.next();

            if (!generatePersistentEntitiesOnly() || (generatePersistentEntitiesOnly() && !entity.isTransient())) {
                VelocityContext vc = new VelocityContext();
                populateVelocityContext(vc, entity, context);

                fileName = replaceEntityName(basefileName, entity.getName());
                context.setOutputFile(fileName);

                generateFile(context, vc);
            }
        }
    }

    /**
     * An opportunity to populate the passed <tt>VelocityContext</tt>
     * 
     * @param vc
     *            The <tt>VelocityContext</tt> to populate
     * @param entity
     *            The <tt>Entity</tt> from information can be pulled
     * @param generationContext
     *            The <tt>GenerationContext</tt> of this <tt>Generator</tt>
     */
    protected abstract void populateVelocityContext(VelocityContext vc, Entity entity,
            GenerationContext generationContext);

    /**
     * If true, will trigger generation of persistent entities only.
     * 
     * @return true to generate only peristent entities, false to generate persistent and transient entities
     */
    protected abstract boolean generatePersistentEntitiesOnly();

}
