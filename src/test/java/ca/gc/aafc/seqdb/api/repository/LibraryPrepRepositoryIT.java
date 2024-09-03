package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.api.testsupport.factories.LibraryPrepFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.ProductFactory;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.LibraryPrepTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.crnk.core.queryspec.QuerySpec;
import javax.inject.Inject;

public class LibraryPrepRepositoryIT extends BaseRepositoryTest {

  private static final String TEST_QUALITY = "test quality";

  private LibraryPrep testLibPrep;
  private LibraryPrepBatchDto testBatchDto;

  @Inject
  private LibraryPrepBatchRepository libraryPrepBatchRepository;

  @Inject
  private LibraryPrepRepository libraryPrepRepository;

  private LibraryPrep createTestLibraryPrep() {

    Product testProduct = ProductFactory.newProduct().build();
    persist(testProduct);

    testLibPrep = LibraryPrepFactory.newLibraryPrep()
        .quality(TEST_QUALITY)
        .build();
   // testLibPrep.getLibraryPrepBatch().setContainerType(testContainerType);
    persist(testLibPrep.getLibraryPrepBatch());
    persist(testLibPrep);
    
    // This is needed in the tests to initialize the PersistentBag for the batch's "libraryPreps"
    // field for the test. This doesn't matter in prod.
    entityManager.flush();
    entityManager.refresh(testLibPrep.getLibraryPrepBatch());

    testBatchDto = libraryPrepBatchRepository.findOne(
        testLibPrep.getLibraryPrepBatch().getUuid(),
        new QuerySpec(LibraryPrepBatchDto.class)
    );
    
    return testLibPrep;
  }
  
  @BeforeEach
  public void setup() {
    createTestLibraryPrep();
  }
  
  @Test
  public void findLibPrep_whenLibPrepExists_libPrepReturned() {
    LibraryPrepDto dto = libraryPrepRepository.findOne(
        testLibPrep.getUuid(),
        new QuerySpec(LibraryPrepDto.class)
    );
    
    assertNotNull(dto);
    assertEquals(TEST_QUALITY, dto.getQuality());
  }
  
  @Test
  public void createLibPrep_onSuccess_libPrepCreated() {

    LibraryPrepDto newDto = LibraryPrepTestFixture.newLibraryPrep();
    newDto.setLibraryPrepBatch(testBatchDto);

    LibraryPrepDto created = libraryPrepRepository.create(newDto);
    
    assertNotNull(created.getUuid());
    assertEquals("test size", created.getSize());
    assertEquals(
      LibraryPrepTestFixture.MATERIAL_SAMPLE_UUID.toString(),
        created.getMaterialSample().getId()
    );
    assertEquals(
        testLibPrep.getLibraryPrepBatch().getUuid(),
        created.getLibraryPrepBatch().getUuid()
    );
  }

  @Test
  public void updateLibPrep_onSuccess_libPrepUpdated() {
    LibraryPrepDto dto = libraryPrepRepository.findOne(
        testLibPrep.getUuid(),
        new QuerySpec(LibraryPrepDto.class)
    );
    
    dto.setQuality("updated quality");
    libraryPrepRepository.save(dto);
    assertEquals("updated quality", testLibPrep.getQuality());
  }
  
  @Test
  public void deleteLibPrep_onSuccess_libPrepDeleted() {
    libraryPrepRepository.delete(testLibPrep.getUuid());
    assertNull(entityManager.find(LibraryPrep.class, testLibPrep.getId()));
  }
  
}
