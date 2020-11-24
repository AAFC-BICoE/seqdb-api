package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.seqdb.api.dto.SampleDto;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.Sample;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class SampleResourceRepositoryIT extends BaseRepositoryTest {

  private static final String TEST_SAMPLE_NAME = "sample name";
  private static final String TEST_SAMPLE_VERSION = "sample version";
  
  private static final String TEST_SAMPLE_NAME_CREATE = "sample name";
  private static final String TEST_SAMPLE_VERSION_CREATE = "sample version";
  
  private static final String TEST_SAMPLE_NAME_UPDATE = "update name";
  
  @Inject
  private ResourceRepository<SampleDto, Serializable> sampleRepository;

  @Inject
  private BaseDAO baseDao;
  
  private Sample testSample;
  
  /**
   * Generate a test sample entity.
   */
  private Sample createTestSample() {
    testSample = new Sample();
    testSample.setName(TEST_SAMPLE_NAME);
    testSample.setVersion(TEST_SAMPLE_VERSION);
    
    persist(testSample);
    
    return testSample;
  }
  
  @BeforeEach
  public void setup() {
    createTestSample();
  }
  
  /**
   * Try to find the persisted entity and see if the data matches what was persisted.
   * Based on the createTestSample method. 
   */
  @Test
  public void findSample_whenNoFieldsAreSelected_sampleReturnedWithAllFields() {
    SampleDto sampleDto = sampleRepository.findOne(
        testSample.getUuid(),
        new QuerySpec(SampleDto.class)
    );
    
    assertNotNull(sampleDto);
    assertEquals(testSample.getUuid(), sampleDto.getUuid());
    assertEquals(TEST_SAMPLE_NAME, sampleDto.getName());
    assertEquals(TEST_SAMPLE_VERSION, sampleDto.getVersion());
  }
  
  /**
   * Test the sample repositories create method to ensure it can persisted and retrieved.
   */
  @Test
  public void createSample_onSuccess_allFieldsHaveSetValueAfterPersisted() {
    String newSampleName = "new sample";

    SampleDto newSample = new SampleDto();
    newSample.setName(newSampleName);
    newSample.setVersion(TEST_SAMPLE_VERSION_CREATE);
    
    SampleDto createdSample = sampleRepository.create(newSample);
    
    //DTO has the set value
    assertNotNull(createdSample.getUuid());
    assertEquals(newSampleName, createdSample.getName());
    assertEquals(TEST_SAMPLE_VERSION_CREATE, createdSample.getVersion());
    
    //entity has the set value    
    Sample sampleEntity = baseDao.findOneByNaturalId(createdSample.getUuid(), Sample.class);
    
    assertNotNull(sampleEntity.getId());
    assertEquals(newSampleName, sampleEntity.getName());
    assertEquals(TEST_SAMPLE_VERSION_CREATE, sampleEntity.getVersion());
  }
  
  /**
   * This test will update a specific field for the sample, save it, then check if it updates the
   * test sample which has been persisted.
   */
  @Test
  public void updateSample_whenSomeFieldsAreUpdated_sampleReturnedWithSelectedFieldsUpdated() {
     // Get the test product's DTO.
    QuerySpec querySpec = new QuerySpec(SampleDto.class);

    SampleDto sampleDto = sampleRepository.findOne(testSample.getUuid(), querySpec);
    
    // Change the DTO's desc value.
    sampleDto.setName(TEST_SAMPLE_NAME_UPDATE);
    
    // Save the DTO using the repository.
    sampleRepository.save(sampleDto);
    
    // Check that the entity has the new desc value.
    assertEquals(TEST_SAMPLE_NAME_UPDATE, testSample.getName());
  }
  
  /**
   * Delete an existing product. It should not exist anymore if the delete was successful.
   */
  @Test
  public void deleteProduct_onProductLookup_productNotFound() {
    sampleRepository.delete(testSample.getUuid());
    assertNull(entityManager.find(Product.class, testSample.getId()));
  }

  /**
   * Test the behavior if the sample could not be found when trying to delete it.
   */
  @Test
  public void deleteSample_onSampleNotFound_throwResourceNotFoundException() {
    assertThrows(
      ResourceNotFoundException.class,
      () -> sampleRepository.delete(UUID.fromString("00000000-0000-0000-0000-000000000000")
    ));
  }

}
