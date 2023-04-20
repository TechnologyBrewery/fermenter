package org.technologybrewery.fermenter.ale.mda.generator.angular;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;
import org.technologybrewery.fermenter.mda.generator.entity.AbstractEntityGenerator;
import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.technologybrewery.fermenter.mda.metamodel.element.Entity;

public abstract class AbstractAngularEntityGenerator extends AbstractEntityGenerator {

    @Override
    public void generate(GenerationContext context) {
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);
        String baseFileName = context.getOutputFile();

        // Key piece - need to generate for all artifacts and WITHOUT having a
        // local set of entities.
        for (String artifactId : metadataRepository.getArtifactIds()) {
            Map<String, Entity> entityMap = metadataRepository.getEntitiesByArtifactId(artifactId);
            if (entityMap != null && !entityMap.isEmpty()) {
                for (Entity baseEntity : entityMap.values()) {
                    AngularEntity entity = new AngularEntity(baseEntity);

                    boolean generateAllEntities = !generatePersistentEntitiesOnly();
                    boolean isRegularPersistentEntity = !entity.isTransient() && !entity.isNonPersistentParentEntity();
                    if (generateAllEntities || isRegularPersistentEntity) {
                        context.setArtifactId(artifactId);
                        VelocityContext vc = getNewVelocityContext(context);
                        populateVelocityContext(vc, entity, context);

                        // KEY piece - need to set the entity name to be
                        // lower-hyphen for the file name
                        String fileName = replaceEntityName(baseFileName, entity.getNameLowerHyphen());
                        context.setOutputFile(fileName);

                        generateFile(context, vc);
                    }
                }
            }
        }

    }

    @Override
    protected String getOutputSubFolder() {
        return AngularGeneratorUtil.ANGULAR_SRC_FOLDER_FOR_APP;
    }

    @Override
    protected void populateVelocityContext(VelocityContext vc, Entity entity, GenerationContext generationContext) {
        AngularEntity angularEntity = (AngularEntity) entity;
        vc.put("entity", angularEntity);
        vc.put("StringUtils", StringUtils.class);
    }
}
