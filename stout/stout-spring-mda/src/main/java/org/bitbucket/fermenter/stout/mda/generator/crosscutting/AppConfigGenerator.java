package org.bitbucket.fermenter.stout.mda.generator.crosscutting;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.bitbucket.fermenter.mda.generator.AbstractGenerator;
import org.bitbucket.fermenter.mda.generator.GenerationContext;
import org.bitbucket.fermenter.mda.metamodel.DefaultModelInstanceRepository;
import org.bitbucket.fermenter.mda.metamodel.ModelInstanceRepositoryManager;
import org.bitbucket.fermenter.mda.metamodel.element.Entity;
import org.bitbucket.fermenter.mda.metamodel.element.Service;
import org.bitbucket.fermenter.stout.mda.java.JavaGeneratorUtil;
import org.codehaus.plexus.util.StringUtils;

/**
 * Provides access to both services and entities to allow for generation of combined artifacts (e.g., factories).
 */
public class AppConfigGenerator extends AbstractGenerator {

    /**
     * {@inheritDoc}
     */
    public void generate(GenerationContext context) {
        // only generate those concepts that are part of the targeted generation run (vice all model instances):
        DefaultModelInstanceRepository metadataRepository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);
        Map<String, Service> services = metadataRepository.getServicesByContext(metadataContext);        
        
        DefaultModelInstanceRepository metamodelRepository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);

        Map<String, Entity> entities = metamodelRepository.getEntitiesByContext(metadataContext);
        
        VelocityContext vc;
        String fileName;
        String basefileName = context.getOutputFile();
        basefileName = replaceBasePackage(basefileName, context.getBasePackageAsPath());
        vc = new VelocityContext();
        vc.put("services", services);
        vc.put("entities", entities);
        vc.put("basePackage", context.getBasePackage());
        vc.put("artifactId", context.getArtifactId());
        vc.put("version", context.getVersion());

        String artifactId = artifactIdInCamelCase(context);
        fileName = replaceArtifactId(basefileName, artifactId);
        vc.put("configName", artifactId);
        context.setOutputFile(fileName);

        generateFile(context, vc);

    }

    private String artifactIdInCamelCase(GenerationContext context) {
        String artifactIdAsWords = context.getArtifactId().replace("-", org.apache.commons.lang3.StringUtils.SPACE);
        String artifactIdWithCamelCasedWords = StringUtils.capitaliseAllWords(artifactIdAsWords);
        return artifactIdWithCamelCasedWords.replace(org.apache.commons.lang3.StringUtils.SPACE, "");
    }
    
    

    @Override
    protected String getOutputSubFolder() {
        return JavaGeneratorUtil.OUTPUT_SUB_FOLDER_JAVA;
    }

}
