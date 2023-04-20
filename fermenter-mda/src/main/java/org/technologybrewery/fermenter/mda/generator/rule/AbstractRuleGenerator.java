package org.technologybrewery.fermenter.mda.generator.rule;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.technologybrewery.fermenter.mda.generator.AbstractGenerator;
import org.technologybrewery.fermenter.mda.generator.GenerationContext;
import org.technologybrewery.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.technologybrewery.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.technologybrewery.fermenter.mda.metamodel.element.Rule;

/**
 * Iterates through each rule in the meta-model and enables the generation of a single file for each rule.
 */
public abstract class AbstractRuleGenerator extends AbstractGenerator {

    /**
     * {@inheritDoc}
     */
    @Override
    public void generate(GenerationContext context) {
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);

       Map<String, Rule> rules = metadataRepository.getRulesByContext(metadataContext);

        String fileName;
        String baseFileName = context.getOutputFile();
        baseFileName = replaceBasePackage(baseFileName, context.getBasePackageAsPath());

        for (Rule rule : rules.values()) {
            VelocityContext vc = new VelocityContext();
            populateVelocityContext(vc, rule, context);

            fileName = replaceRuleName(baseFileName, rule.getName());
            fileName = replaceRuleGroup(fileName, rule.getRuleGroup());
            
            context.setOutputFile(fileName);

            generateFile(context, vc);
        }
    }

    /**
     * Enables subclasses to add any additional metadata to the provided {@link VelocityContext} to facilitate the
     * generation of the provided {@link Rule}.
     * 
     * @param vc
     * @param rule
     * @param generationContext
     */
    protected abstract void populateVelocityContext(VelocityContext vc, Rule rule,
            GenerationContext generationContext);

}
