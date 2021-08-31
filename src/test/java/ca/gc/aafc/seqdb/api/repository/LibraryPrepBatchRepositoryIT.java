package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.ContainerTypeDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.dto.ProductDto;
import ca.gc.aafc.seqdb.api.dto.ProtocolDto;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.Protocol;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.testsupport.factories.ContainerTypeFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.LibraryPrepBatchFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.ProductFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.ProtocolFactory;
import io.crnk.core.queryspec.QuerySpec;

public class LibraryPrepBatchRepositoryIT extends BaseRepositoryTest {

  private static String TEST_NOTES = "test notes";

  private ContainerType testContainerType;
  private LibraryPrepBatch testBatch;
  private Product testProduct;
  private Protocol testProtocol;

  @Inject
  private LibraryPrepBatchRepository libraryPrepBatchRepository;

  @Inject
  private ContainerTypeRepository containerTypeRepository;

  @Inject
  private ProductRepository productRepository;

  @Inject
  private ProtocolRepository protocolRepository;
  
  private LibraryPrepBatch createTestBatch() {
    testContainerType = ContainerTypeFactory.newContainerType()
        .build();
    
    persist(testContainerType);
    
    testProduct = ProductFactory.newProduct().build();
    persist(testProduct);
    
    testProtocol = ProtocolFactory.newProtocol().build();
    persist(testProtocol);
    
    testBatch = LibraryPrepBatchFactory.newLibraryPrepBatch()
        .name("test batch")
        .containerType(testContainerType)
        .product(testProduct)
        .protocol(testProtocol)
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
    newDto.setContainerType(
      containerTypeRepository.findOne(
            testContainerType.getUuid(),
            new QuerySpec(ContainerTypeDto.class)
        )
     );
    newDto.setProduct(
        productRepository.findOne(
            testProduct.getUuid(),
            new QuerySpec(ProductDto.class)
        )
    );
    newDto.setProtocol(
        protocolRepository.findOne(
            testProtocol.getUuid(),
            new QuerySpec(ProtocolDto.class)
        )
    );
    
    LibraryPrepBatchDto created = libraryPrepBatchRepository.create(newDto);
    
    assertNotNull(created.getUuid());
    assertEquals("notes", created.getNotes());
    assertEquals("cleanup notes", created.getCleanUpNotes());
    assertEquals("yield notes", created.getYieldNotes());
    assertEquals(testProduct.getUuid(), created.getProduct().getUuid());
    assertEquals(testProtocol.getUuid(), created.getProtocol().getUuid());
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
