package org.bitbucket.fermenter.mda.generator.entity;

import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;

/**
 * Iterates through each entity in the meta-model and enables the generation of
 * a single file for each entity.
 */
public abstract class AbstractEntityGenerator extends AbstractGenerator {

    public void generate(GenerationContext context) throws GenerationException {

        DefaultModelInstanceRepository metamodelRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(DefaultModelInstanceRepository.class);

        Map<String, Entity> entityMap = metamodelRepository.getEntitiesByContext(metadataContext);
        Iterator<Entity> entities = entityMap.values().iterator();

        String fileName;
        String basefileName = context.getOutputFile();
        basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
        while (entities.hasNext()) {
            Entity entity = (Entity) entities.next();

            if (!generatePersistentEntitiesOnly() || (generatePersistentEntitiesOnly() && !entity.isTransient()
            && !entity.isNonPersistentParentEntity()
            )) {
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
     * @return true to generate only peristent entities, false to generate
     *         persistent and transient entities
     */
    protected abstract boolean generatePersistentEntitiesOnly();

}
