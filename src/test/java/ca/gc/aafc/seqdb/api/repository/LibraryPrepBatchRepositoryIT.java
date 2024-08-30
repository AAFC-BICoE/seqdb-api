package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.dto.ProductDto;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.testsupport.factories.LibraryPrepBatchFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.ProductFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.crnk.core.queryspec.QuerySpec;
import java.util.UUID;
import javax.inject.Inject;

public class LibraryPrepBatchRepositoryIT extends BaseRepositoryTest {

  private static String TEST_NOTES = "test notes";

  private LibraryPrepBatch testBatch;
  private Product testProduct;

  private static final UUID TEST_STORAGE_UNIT_UUID = UUID.randomUUID();
  private static final UUID TEST_PROTOCOL_UUID = UUID.randomUUID();

  @Inject
  private LibraryPrepBatchRepository libraryPrepBatchRepository;

  @Inject
  private ProductRepository productRepository;
  
  private LibraryPrepBatch createTestBatch() {

    testProduct = ProductFactory.newProduct().build();
    persist(testProduct);
    
    testBatch = LibraryPrepBatchFactory.newLibraryPrepBatch()
        .name("test batch")
        .product(testProduct)
        .protocol(TEST_PROTOCOL_UUID)
        .notes(TEST_NOTES)
        .build();
    
    persist(testBatch);
    
    return testBatch;
  }

  @BeforeEach
  public void setup() {
    createTestBatch();
  }

  @Test
  public void findBatch_whenBatchExists_batchReturned() {
    LibraryPrepBatchDto dto = libraryPrepBatchRepository.findOne(
        testBatch.getUuid(),
        new QuerySpec(LibraryPrepBatchDto.class)
    );
    
    assertNotNull(dto);
    assertEquals(TEST_NOTES, testBatch.getNotes());
  }
  
  @Test
  public void createBatch_onSuccess_batchCreated() {
    LibraryPrepBatchDto newDto = new LibraryPrepBatchDto();
    newDto.setName("new test batch");
    newDto.setGroup("dina");
    newDto.setNotes("notes");
    newDto.setCleanUpNotes("cleanup notes");
    newDto.setYieldNotes("yield notes");
    newDto.setProtocol(ExternalRelationDto.builder().id(TEST_PROTOCOL_UUID.toString()).type("protocol").build());
    newDto.setStorageUnit(ExternalRelationDto.builder().id(TEST_STORAGE_UNIT_UUID.toString()).type("storage-unit-usage").build());
    newDto.setProduct(
        productRepository.findOne(
            testProduct.getUuid(),
            new QuerySpec(ProductDto.class)
        )
    );
    
    LibraryPrepBatchDto created = libraryPrepBatchRepository.create(newDto);
    
    assertNotNull(created.getUuid());
    assertEquals("notes", created.getNotes());
    assertEquals("cleanup notes", created.getCleanUpNotes());
    assertEquals("yield notes", created.getYieldNotes());
    assertEquals(testProduct.getUuid(), created.getProduct().getUuid());
    assertEquals(TEST_PROTOCOL_UUID.toString(), created.getProtocol().getId());
    assertEquals(TEST_STORAGE_UNIT_UUID.toString(), created.getStorageUnit().getId());
  }
  
  @Test
  public void updateBatch_onSuccess_batchUpdated() {
    LibraryPrepBatchDto dto = libraryPrepBatchRepository.findOne(
        testBatch.getUuid(),
        new QuerySpec(LibraryPrepBatchDto.class)
    );
    
    dto.setNotes("updated notes");
    libraryPrepBatchRepository.save(dto);
    assertEquals("updated notes", testBatch.getNotes());
  }
  
  @Test
  public void deleteBatch_onSuccess_batchDeleted() {
    libraryPrepBatchRepository.delete(testBatch.getUuid());
    assertNull(entityManager.find(LibraryPrepBatch.class, testBatch.getId()));
  }

}
