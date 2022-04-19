package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.field.AbstractFieldGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Field;

public class AngularFieldUiGenerator extends AbstractFieldGenerator {

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
                    Map<String, AngularField> fieldMap = entity.getAllFields();
                	for (AngularField field : fieldMap.values()) {
                		if (field.hasLabel()) {
                            context.setArtifactId(artifactId);
                            VelocityContext vc = getNewVelocityContext(context);
                            populateVelocityContext(vc, entity, field, context);

                            // KEY piece - need to set the entity name to be
                            // lower-hyphen for the file name
                            String fileName = replace("entityName", baseFileName, entity.getNameLowerHyphen());
                            fileName = replace("fieldName", fileName, field.getNameLowerHyphen());
                            context.setOutputFile(fileName);

                            generateFile(context, vc);
                		}
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
    protected void populateVelocityContext(VelocityContext vc, Entity entity, Field field, GenerationContext generationContext) {
        AngularEntity angularEntity = (AngularEntity) entity;
        AngularField angularField = (AngularField) field;
        vc.put("entity", angularEntity);
        vc.put("field", angularField);
        vc.put("StringUtils", StringUtils.class);
    }
}