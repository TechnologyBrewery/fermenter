package org.bitbucket.fermenter.mda.metamodel;

import java.util.List;
import java.util.Properties;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bitbucket.fermenter.mda.generator.GenerationException;

public class DefaultMetadataRepository extends AbstractMetadataRepository {

    private static final Log log = LogFactory.getLog(DefaultMetadataRepository.class);

    protected MetamodelConfig config = KrauseningConfigFactory.create(MetamodelConfig.class);

    public DefaultMetadataRepository(String basePackage) {
        super(basePackage);
    }

    @Override
    public void load(Properties properties) {
        MetadataUrlResolver loader = createMetadataResolverInstance();

        EnumerationMetadataManager enumerationManager = EnumerationMetadataManager.getInstance();
        enumerationManager.reset();

        List<MetadataUrl> metadataUrls = loader.getMetadataURLs(properties);
        for (MetadataUrl metadataUrl : metadataUrls) {
            long start = System.currentTimeMillis();

            enumerationManager.loadMetadata(metadataUrl);
            
            if (log.isInfoEnabled()) {
                long stop = System.currentTimeMillis();
                log.info("Metadata for artifactId '" + metadataUrl.getArtifactId() + "' has been loaded - "
                        + (stop - start) + "ms");
            }
        }
    }

    @Override
    public void validate(Properties properties) {
        // TODO Auto-generated method stub

    }
    
    private MetadataUrlResolver createMetadataResolverInstance() {
        String metadataLoaderClass = config.getUrlResolver();
        MetadataUrlResolver loader;
        try {
            Class<?> clazz = Class.forName(metadataLoaderClass);
            loader = (MetadataUrlResolver) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new GenerationException("Could not create a MetadataUrlResolver instance!", e);
        }
        return loader;
    }    

}
