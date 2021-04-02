package org.bitbucket.fermenter.mda.generator.field;

import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.BaseFieldDecorator;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Field;

public abstract class AbstractFieldGenerator extends AbstractGenerator {

    @Override
    public void generate(GenerationContext context) {
        DefaultModelInstanceRepository metamodelRepository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);
        String baseFileName = context.getOutputFile();
        baseFileName = replaceBasePackage(baseFileName, context.getBasePackageAsPath());

        Map<String, Entity> entityMap = metamodelRepository.getEntitiesByContext(metadataContext);
        if (entityMap != null && !entityMap.isEmpty()) {
            for (Entity entity : entityMap.values()) {
                List<Field> entityFields = entity.getFields();
            	for (Field field : entityFields) {
            		BaseFieldDecorator decoratedField = new BaseFieldDecorator(field);
            		if (decoratedField.hasLabel()) {
                        VelocityContext vc = getNewVelocityContext(context);
                        populateVelocityContext(vc, entity, field, context);

                        String fileName = replace("entityName", baseFileName, entity.getNameLowerHyphen());
                        fileName = replace("fieldName", fileName, field.getNameLowerHyphen());
                        context.setOutputFile(fileName);

                        generateFile(context, vc);
            		}
            	}
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
            Field field, GenerationContext generationContext);


}
