package org.bitbucket.fermenter.mda.metadata;

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
		managedRespository = MetadataRepositoryManager
				.getMetadataRepostory(TestMetadataRepository.class);
		assertEquals(testRepository, managedRespository);
				
	}
	
	@Test
	public void testMultipleSimultaneousMetadataReposistory() {
		MetadataRepository defaultRepository = new MetadataRepository(null);
		MetadataRepositoryManager.setRepository(defaultRepository);
		
		TestMetadataRepository testRepository = new TestMetadataRepository(null);
		MetadataRepositoryManager.setRepository(testRepository);
		
		MetadataRepository managedDefaultRespository = MetadataRepositoryManager
				.getMetadataRepostory(MetadataRepository.class);
		assertEquals(defaultRepository, managedDefaultRespository);
		
		TestMetadataRepository managedTestRespository = MetadataRepositoryManager
				.getMetadataRepostory(TestMetadataRepository.class);
		assertEquals(testRepository, managedTestRespository);
				
	}
	
	@Test
	public void testOverwriteMetadataReposistory() {
		setNewDefaultMetadataRepository();
		
		MetadataRepository newDefaultRepository = new MetadataRepository(null);
		MetadataRepositoryManager.setRepository(newDefaultRepository);
		MetadataRepository managedDefaultRespository = MetadataRepositoryManager
				.getMetadataRepostory(MetadataRepository.class);
		assertEquals(newDefaultRepository, managedDefaultRespository);
					
	}
	
	@Test
	public void testClearMetadataReposistoryManager() {
		setNewDefaultMetadataRepository();
	
		MetadataRepositoryManager.clear();
		MetadataRepository managedDefaultRespository = MetadataRepositoryManager
				.getMetadataRepostory(MetadataRepository.class);
		assertNull(managedDefaultRespository);
					
	}

	private void setNewDefaultMetadataRepository() {
		MetadataRepository defaultRepository = new MetadataRepository(null);
		MetadataRepository managedRespository = MetadataRepositoryManager
				.getMetadataRepostory(MetadataRepository.class);
		assertNull(managedRespository);
		MetadataRepositoryManager.setRepository(defaultRepository);
		managedRespository = MetadataRepositoryManager
				.getMetadataRepostory(MetadataRepository.class);
		assertEquals(defaultRepository, managedRespository);
	}	
	
}

/**
 * Used for testing only.
 */
class TestMetadataRepository extends AbstractMetadataRepository {
	
	public TestMetadataRepository(Properties props) {
		super(props);
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
