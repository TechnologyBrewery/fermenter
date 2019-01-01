package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.generator.GenerationException;
import org.bitbucket.fermenter.mda.generator.entity.AbstractEntityGenerator;
import org.bitbucket.fermenter.mda.metadata.MetadataRepository;
import org.bitbucket.fermenter.mda.metadata.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;

public class AngularEntityGenerator extends AbstractEntityGenerator {

    @Override
    public void generate(GenerationContext context) throws GenerationException {
        String currentApplication = context.getArtifactId();

        MetadataRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetadataRepostory(MetadataRepository.class);
        Map<String, Entity> entityMap = metadataRepository.getEntitiesByMetadataContext(metadataContext,
                currentApplication);
        Iterator<Entity> entities = entityMap.values().iterator();

        String fileName;
        String basefileName = context.getOutputFile();
        basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
        while (entities.hasNext()) {
            // key piece that is needed for this generate method
            // is that the entities need to be passed into the velocity context
            // as Angular entities in order to have access to the angular entity
            // methods like getting the name in lower case camel
            AngularEntity entity = new AngularEntity(entities.next());

            VelocityContext vc = new VelocityContext();
            populateVelocityContext(vc, entity, context);

            fileName = replaceEntityName(basefileName, entity.getName());
            context.setOutputFile(fileName);

            generateFile(context, vc);
        }
    }

    @Override
    protected void populateVelocityContext(VelocityContext vc, Entity entity, GenerationContext generationContext) {
        AngularEntity angularEntity = new AngularEntity(entity);
        vc.put("entity", angularEntity);
        vc.put("StringUtils", StringUtils.class);
    }

    @Override
    protected boolean generatePersistentEntitiesOnly() {
        return false;
    }

    @Override
    protected String getOutputSubFolder() {
        return AngularGeneratorUtil.ANGULAR_SRC_FOLDER_FOR_APP;
    }

}
