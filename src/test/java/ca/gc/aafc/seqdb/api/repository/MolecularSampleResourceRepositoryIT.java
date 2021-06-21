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

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.dto.MolecularSampleDto;
import ca.gc.aafc.seqdb.api.entities.MolecularSample;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class MolecularSampleResourceRepositoryIT extends BaseRepositoryTest {

  private static final String TEST_MOLECULAR_SAMPLE_NAME = "molecular sample name";
  private static final String TEST_MOLECULAR_SAMPLE_VERSION = "molecular sample version";
  
  private static final String TEST_MOLECULAR_SAMPLE_NAME_CREATE = "molecular sample name";
  private static final String TEST_MOLECULAR_SAMPLE_VERSION_CREATE = "molecular sample version";
  
  private static final String TEST_MOLECULAR_SAMPLE_NAME_UPDATE = "molecular update name";

  private static final UUID TEST_MATERIAL_SAMPLE_UUID = UUID.randomUUID();
  
  @Inject
  private ResourceRepository<MolecularSampleDto, Serializable> molecularSampleRepository;

  @Inject
  private BaseDAO baseDao;
  
  private MolecularSample testMolecularSample;
  
  /**
   * Generate a test sample entity.
   */
  private MolecularSample createTestSample() {
    testMolecularSample = new MolecularSample();
    testMolecularSample.setName(TEST_MOLECULAR_SAMPLE_NAME_CREATE);
    testMolecularSample.setVersion(TEST_MOLECULAR_SAMPLE_VERSION_CREATE);
    testMolecularSample.setMaterialSample(TEST_MATERIAL_SAMPLE_UUID);
    
    persist(testMolecularSample);
    
    return testMolecularSample;
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
    MolecularSampleDto molecularSampleDto = molecularSampleRepository.findOne(
        testMolecularSample.getUuid(),
        new QuerySpec(MolecularSampleDto.class)
    );
    
    assertNotNull(molecularSampleDto);
    assertEquals(testMolecularSample.getUuid(), molecularSampleDto.getUuid());
    assertEquals(TEST_MOLECULAR_SAMPLE_NAME, molecularSampleDto.getName());
    assertEquals(TEST_MOLECULAR_SAMPLE_VERSION, molecularSampleDto.getVersion());
    assertEquals(TEST_MATERIAL_SAMPLE_UUID.toString(), molecularSampleDto.getMaterialSample().getId());
  }
  
  /**
   * Test the sample repositories create method to ensure it can persisted and retrieved.
   */
  @Test
  public void createSample_onSuccess_allFieldsHaveSetValueAfterPersisted() {
    String newSampleName = "new sample";

    MolecularSampleDto newSample = new MolecularSampleDto();
    newSample.setName(newSampleName);
    newSample.setVersion(TEST_MOLECULAR_SAMPLE_VERSION_CREATE);
    newSample.setMaterialSample(ExternalRelationDto.builder().id(TEST_MATERIAL_SAMPLE_UUID.toString()).type("material-sample").build());
    
    MolecularSampleDto createdSample = molecularSampleRepository.create(newSample);
    
    //DTO has the set value
    assertNotNull(createdSample.getUuid());
    assertEquals(newSampleName, createdSample.getName());
    assertEquals(TEST_MOLECULAR_SAMPLE_VERSION_CREATE, createdSample.getVersion());
    assertEquals(TEST_MATERIAL_SAMPLE_UUID.toString(), createdSample.getMaterialSample().getId());

    
    //entity has the set value    
    MolecularSample sampleEntity = baseDao.findOneByNaturalId(createdSample.getUuid(), MolecularSample.class);
    
    assertNotNull(sampleEntity.getId());
    assertEquals(newSampleName, sampleEntity.getName());
    assertEquals(TEST_MOLECULAR_SAMPLE_VERSION_CREATE, sampleEntity.getVersion());
    assertEquals(TEST_MATERIAL_SAMPLE_UUID, sampleEntity.getMaterialSample());

  }
  
  /**
   * This test will update a specific field for the sample, save it, then check if it updates the
   * test sample which has been persisted.
   */
  @Test
  public void updateSample_whenSomeFieldsAreUpdated_sampleReturnedWithSelectedFieldsUpdated() {
     // Get the test product's DTO.
    QuerySpec querySpec = new QuerySpec(MolecularSampleDto.class);

    MolecularSampleDto molecularSampleDto = molecularSampleRepository.findOne(testMolecularSample.getUuid(), querySpec);
    
    // Change the DTO's desc value.
    molecularSampleDto.setName(TEST_MOLECULAR_SAMPLE_NAME_UPDATE);
    
    // Save the DTO using the repository.
    molecularSampleRepository.save(molecularSampleDto);
    
    // Check that the entity has the new desc value.
    assertEquals(TEST_MOLECULAR_SAMPLE_NAME_UPDATE, testMolecularSample.getName());
  }
  
  /**
   * Delete an existing product. It should not exist anymore if the delete was successful.
   */
  @Test
  public void deleteProduct_onProductLookup_productNotFound() {
    molecularSampleRepository.delete(testMolecularSample.getUuid());
    assertNull(entityManager.find(Product.class, testMolecularSample.getId()));
  }

  /**
   * Test the behavior if the sample could not be found when trying to delete it.
   */
  @Test
  public void deleteSample_onSampleNotFound_throwResourceNotFoundException() {
    assertThrows(
      ResourceNotFoundException.class,
      () -> molecularSampleRepository.delete(UUID.fromString("00000000-0000-0000-0000-000000000000")
    ));
  }

}
