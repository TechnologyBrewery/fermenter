package org.bitbucket.fermenter.mda.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class MetadataRepositoryManagerTest {

    @Before
    public void resetManager() {
        MetadataRepositoryManager.clear();
    }

    @Test
    public void testDefaultMetadataReposistory() {
        setNewDefaultMetadataRepository();

    }

    @Test
    public void testTestMetadataReposistory() {
        TestMetadataRepository testRepository = new TestMetadataRepository(null);
        TestMetadataRepository managedRespository = MetadataRepositoryManager
                .getMetadataRepostory(TestMetadataRepository.class);
        assertNull(managedRespository);
        MetadataRepositoryManager.setRepository(testRepository);
        managedRespository = MetadataRepositoryManager.getMetadataRepostory(TestMetadataRepository.class);
        assertEquals(testRepository, managedRespository);

    }

    @Test
    public void testMultipleSimultaneousMetadataReposistory() {
        MetadataRepository defaultRepository = new DefaultMetadataRepository(null);
        MetadataRepositoryManager.setRepository(defaultRepository);

        TestMetadataRepository testRepository = new TestMetadataRepository(null);
        MetadataRepositoryManager.setRepository(testRepository);

        MetadataRepository managedDefaultRespository = MetadataRepositoryManager
                .getMetadataRepostory(DefaultMetadataRepository.class);
        assertEquals(defaultRepository, managedDefaultRespository);

        TestMetadataRepository managedTestRespository = MetadataRepositoryManager
                .getMetadataRepostory(TestMetadataRepository.class);
        assertEquals(testRepository, managedTestRespository);

    }

    @Test
    public void testOverwriteMetadataReposistory() {
        setNewDefaultMetadataRepository();

        MetadataRepository newDefaultRepository = new DefaultMetadataRepository(null);
        MetadataRepositoryManager.setRepository(newDefaultRepository);
        MetadataRepository managedDefaultRespository = MetadataRepositoryManager
                .getMetadataRepostory(DefaultMetadataRepository.class);
        assertEquals(newDefaultRepository, managedDefaultRespository);

    }

    @Test
    public void testClearMetadataReposistoryManager() {
        setNewDefaultMetadataRepository();

        MetadataRepositoryManager.clear();
        MetadataRepository managedDefaultRespository = MetadataRepositoryManager
                .getMetadataRepostory(DefaultMetadataRepository.class);
        assertNull(managedDefaultRespository);

    }

    private void setNewDefaultMetadataRepository() {
        MetadataRepository defaultRepository = new DefaultMetadataRepository(null);
        MetadataRepository managedRespository = MetadataRepositoryManager
                .getMetadataRepostory(DefaultMetadataRepository.class);
        assertNull(managedRespository);
        MetadataRepositoryManager.setRepository(defaultRepository);
        managedRespository = MetadataRepositoryManager.getMetadataRepostory(DefaultMetadataRepository.class);
        assertEquals(defaultRepository, managedRespository);
    }

}

/**
 * Used for testing only.
 */
class TestMetadataRepository extends AbstractMetadataRepository {

    public TestMetadataRepository(String basePackage) {
        super(basePackage);
    }

    @Override
    public void load(Properties properties) {
        // do nothing

    }

    @Override
    public void validate(Properties properties) {
        // do nothing

    }

}
