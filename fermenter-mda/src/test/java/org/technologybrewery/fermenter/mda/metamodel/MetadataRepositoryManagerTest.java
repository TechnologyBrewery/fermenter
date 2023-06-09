package org.technologybrewery.fermenter.mda.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class MetadataRepositoryManagerTest {

    @Before
    public void resetManager() {
        ModelInstanceRepositoryManager.clear();
    }

    @Test
    public void testDefaultMetadataReposistory() {
        setNewDefaultMetadataRepository();

    }

    @Test
    public void testTestMetadataReposistory() {
        TestMetadataRepository testRepository = new TestMetadataRepository(null);
        TestMetadataRepository managedRespository = ModelInstanceRepositoryManager
                .getMetamodelRepository(TestMetadataRepository.class);
        assertNull(managedRespository);
        ModelInstanceRepositoryManager.setRepository(testRepository);
        managedRespository = ModelInstanceRepositoryManager.getMetamodelRepository(TestMetadataRepository.class);
        assertEquals(testRepository, managedRespository);

    }

    @Test
    public void testMultipleSimultaneousMetadataReposistory() {
        ModelInstanceRepository defaultRepository = new DefaultModelInstanceRepository(null);
        ModelInstanceRepositoryManager.setRepository(defaultRepository);

        TestMetadataRepository testRepository = new TestMetadataRepository(null);
        ModelInstanceRepositoryManager.setRepository(testRepository);

        ModelInstanceRepository managedDefaultRespository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);
        assertEquals(defaultRepository, managedDefaultRespository);

        TestMetadataRepository managedTestRespository = ModelInstanceRepositoryManager
                .getMetamodelRepository(TestMetadataRepository.class);
        assertEquals(testRepository, managedTestRespository);

    }

    @Test
    public void testOverwriteMetadataReposistory() {
        setNewDefaultMetadataRepository();

        ModelInstanceRepository newDefaultRepository = new DefaultModelInstanceRepository(null);
        ModelInstanceRepositoryManager.setRepository(newDefaultRepository);
        ModelInstanceRepository managedDefaultRespository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);
        assertEquals(newDefaultRepository, managedDefaultRespository);

    }

    @Test
    public void testClearMetadataReposistoryManager() {
        setNewDefaultMetadataRepository();

        ModelInstanceRepositoryManager.clear();
        ModelInstanceRepository managedDefaultRespository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);
        assertNull(managedDefaultRespository);

    }

    private void setNewDefaultMetadataRepository() {
        ModelInstanceRepository defaultRepository = new DefaultModelInstanceRepository(null);
        ModelInstanceRepository managedRespository = ModelInstanceRepositoryManager
                .getMetamodelRepository(DefaultModelInstanceRepository.class);
        assertNull(managedRespository);
        ModelInstanceRepositoryManager.setRepository(defaultRepository);
        managedRespository = ModelInstanceRepositoryManager.getMetamodelRepository(DefaultModelInstanceRepository.class);
        assertEquals(defaultRepository, managedRespository);
    }

}

/**
 * Used for testing only.
 */
class TestMetadataRepository extends AbstractModelInstanceRepository {

    public TestMetadataRepository(ModelRepositoryConfiguration config) {
        super(config);
    }

    @Override
    public void load() {
        // do nothing

    }

    @Override
    public void validate() {
        // do nothing

    }

}
