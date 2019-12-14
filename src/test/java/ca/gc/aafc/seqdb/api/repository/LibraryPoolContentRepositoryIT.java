package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;

import javax.inject.Inject;
import javax.validation.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.LibraryPoolContentDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.entities.libraryprep.IndexSet;
import ca.gc.aafc.seqdb.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.entities.pooledlibraries.LibraryPoolContent;
import ca.gc.aafc.seqdb.testsupport.factories.IndexSetFactory;
import ca.gc.aafc.seqdb.testsupport.factories.LibraryPoolContentFactory;
import ca.gc.aafc.seqdb.testsupport.factories.LibraryPoolFactory;
import ca.gc.aafc.seqdb.testsupport.factories.LibraryPrepBatchFactory;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class LibraryPoolContentRepositoryIT extends BaseRepositoryTest {
  // LPC with pooled LibraryPrepBatch using testIndexSet1:
  private LibraryPoolContent testLpc;

  private IndexSet testIndexSet1;
  private IndexSet testIndexSet2;

  // Non-pooled LibraryPrepBatch using test index set 2:
  private LibraryPrepBatch testBatchUnpooled;
  private LibraryPrepBatchDto testBatchDtoUnpooled;
  
  @Inject
  private ResourceRepository<LibraryPoolDto, Serializable> poolRepository;
  @Inject
  private ResourceRepository<LibraryPoolContentDto, Serializable> lpcRepository;
  @Inject
  private ResourceRepository<LibraryPrepBatchDto, Serializable> batchRepository;

  private LibraryPoolContent createTestLpc() {
    testIndexSet1 = IndexSetFactory.newIndexSet().name("test index set 1").build();
    persist(testIndexSet1);
    testIndexSet2 = IndexSetFactory.newIndexSet().name("test index set 2").build();
    persist(testIndexSet2);
    
    testLpc = LibraryPoolContentFactory.newLibraryPoolContent().build();
    testLpc.getPooledLibraryPrepBatch().setName("test batch 1");
    testLpc.getPooledLibraryPrepBatch().setIndexSet(testIndexSet1);
    testLpc.getLibraryPool().setName("test pool 1");
    persist(testLpc.getLibraryPool());
    persist(testLpc.getPooledLibraryPrepBatch());
    persist(testLpc);

    testBatchUnpooled = LibraryPrepBatchFactory.newLibraryPrepBatch()
        .name("test batch 2")
        .indexSet(testIndexSet2)
        .build();
    persist(testBatchUnpooled);
    testBatchDtoUnpooled = batchRepository.findOne(testBatchUnpooled.getId(),
        new QuerySpec(LibraryPrepBatchDto.class));

    // This is needed in the tests to initialize the PersistentBags for the entities to-many list
    // fields for the test. This doesn't matter in prod.
    entityManager.flush();
    entityManager.refresh(testLpc.getLibraryPool());
    entityManager.refresh(testLpc.getPooledLibraryPrepBatch());
    entityManager.refresh(testBatchUnpooled);

    return testLpc;
  }

  @BeforeEach
  public void setup() {
    createTestLpc();
  }

  @Test
  public void findLpc_whenExists_lpcReturned() {
    LibraryPoolContentDto dto = lpcRepository.findOne(testLpc.getId(),
        new QuerySpec(LibraryPoolContentDto.class));

    assertNotNull(dto);
    assertEquals(testLpc.getId(), dto.getLibraryPoolContentId());
  }

  @Test
  public void createLpc_onSuccess_lpcCreated() {
    LibraryPoolContentDto dto = new LibraryPoolContentDto();

    // Add to same pool as the test LPC.
    dto.setLibraryPool(poolRepository.findOne(testLpc.getLibraryPool().getId(),
        new QuerySpec(LibraryPoolDto.class)));

    // Pool a new batch.
    dto.setPooledLibraryPrepBatch(testBatchDtoUnpooled);

    LibraryPoolContentDto created = lpcRepository.create(dto);
    assertNotNull(created.getLibraryPoolContentId());
    // Assert parent pool:
    assertEquals(testLpc.getLibraryPool().getId(), created.getLibraryPool().getLibraryPoolId());
    // Assert pooled LibraryPrepBatch:
    assertEquals(testBatchDtoUnpooled.getLibraryPrepBatchId(),
        created.getPooledLibraryPrepBatch().getLibraryPrepBatchId());
  }

  @Test
  public void updateLpc_onSuccess_lpcUpdated() {
    LibraryPoolContentDto dto = lpcRepository.findOne(testLpc.getId(),
        new QuerySpec(LibraryPoolContentDto.class));

    dto.setPooledLibraryPrepBatch(testBatchDtoUnpooled);

    LibraryPoolContentDto updated = lpcRepository.save(dto);
    assertEquals(testBatchDtoUnpooled.getLibraryPrepBatchId(),
        updated.getPooledLibraryPrepBatch().getLibraryPrepBatchId());
  }

  @Test
  public void deleteLpc_onSuccess_lpcDeleted() {
    lpcRepository.delete(testLpc.getId());
    assertNull(entityManager.find(LibraryPoolContent.class, testLpc.getId()));
  }

  @Test
  public void createLpc_onDuplicatePooledIndexSet_throwValidationException() {
    LibraryPoolContentDto newLpc = new LibraryPoolContentDto();
    newLpc.setLibraryPool(
        poolRepository.findOne(
            testLpc.getLibraryPool().getId(),
            new QuerySpec(LibraryPoolDto.class)
        )
    );
    
    // Try to link a batch that is also using test index set 1:
    testBatchUnpooled.setIndexSet(testIndexSet1);
    newLpc.setPooledLibraryPrepBatch(testBatchDtoUnpooled);
    
    ValidationException exception = assertThrows(
        ValidationException.class,
        () -> lpcRepository.create(newLpc)
    );
    
    assertEquals(
        "Duplicate index set usage: Batches 'test batch 2' and 'test batch 1' are both using index set 'test index set 1'",
        exception.getMessage()
    );
  }
  
  @Test
  public void createLpc_onDuplicateNestedPooledBatchIndexSet_throwValidationException() {
    LibraryPrepBatch existingBatch = testLpc.getPooledLibraryPrepBatch();
    
    // The existing LPC should pool a pool:
    LibraryPool testSubPool1 = LibraryPoolFactory.newLibraryPool()
        .name("test sub-pool 1")
        .build();
    persist(testSubPool1);
    testLpc.setPooledLibraryPrepBatch(null);
    testLpc.setPooledLibraryPool(testSubPool1);
    
    // The nested pool will link to the existing LibraryPrepBatch:
    LibraryPoolContent batchLpc1 = LibraryPoolContentFactory.newLibraryPoolContent()
        .libraryPool(testSubPool1)
        .pooledLibraryPrepBatch(existingBatch)
        .build();
    persist(batchLpc1);
    entityManager.flush();
    entityManager.refresh(testSubPool1);
    
    // Add a new Sub-pool to add to the existing pool:
    LibraryPool testSubPool2 = LibraryPoolFactory.newLibraryPool()
        .name("test sub-pool 2")
        .build();
    persist(testSubPool2);
    // Sub-pool 2 will contain a batch linking to the same index set as the existing batch:
    testBatchUnpooled.setIndexSet(testIndexSet1);
    LibraryPoolContent batchLpc2 = LibraryPoolContentFactory.newLibraryPoolContent()
        .libraryPool(testSubPool2)
        .pooledLibraryPrepBatch(testBatchUnpooled)
        .build();
    persist(batchLpc2);
    entityManager.flush();
    entityManager.refresh(testSubPool2);
    
    // Create the LPC to add sub-pool 2 to the initial test pool:
    LibraryPoolContentDto pooledPoolLpc2 = new LibraryPoolContentDto();
    pooledPoolLpc2.setLibraryPool(
        poolRepository.findOne(
            testLpc.getLibraryPool().getId(),
            new QuerySpec(LibraryPoolDto.class)
        )
    );
    pooledPoolLpc2.setPooledLibraryPool(
        poolRepository.findOne(
            testSubPool2.getId(),
            new QuerySpec(LibraryPoolDto.class)
        )
    );
    
    ValidationException exception = assertThrows(
        ValidationException.class,
        () -> lpcRepository.create(pooledPoolLpc2)
    );
    
    assertEquals(
        "Duplicate index set usage: Batches 'test batch 2' and 'test batch 1' are both using index set 'test index set 1'",
        exception.getMessage()
    );
  }
  
  @Test
  public void createLpc_onNoDuplicateNestedPooledBatchIndexSet_lpcCreated() {
    LibraryPrepBatch existingBatch = testLpc.getPooledLibraryPrepBatch();
    
    // The existing LPC should pool a pool:
    LibraryPool testSubPool1 = LibraryPoolFactory.newLibraryPool()
        .name("test sub-pool 1")
        .build();
    persist(testSubPool1);
    testLpc.setPooledLibraryPrepBatch(null);
    testLpc.setPooledLibraryPool(testSubPool1);
    
    // The nested pool will link to the existing LibraryPrepBatch:
    LibraryPoolContent batchLpc1 = LibraryPoolContentFactory.newLibraryPoolContent()
        .libraryPool(testSubPool1)
        .pooledLibraryPrepBatch(existingBatch)
        .build();
    persist(batchLpc1);
    entityManager.flush();
    entityManager.refresh(testSubPool1);
    
    // Add a new Sub-pool to add to the existing pool:
    LibraryPool testSubPool2 = LibraryPoolFactory.newLibraryPool()
        .name("test sub-pool 2")
        .build();
    persist(testSubPool2);
    // Sub-pool 2 will contain a batch linking to a different index set than the existing batch:
    testBatchUnpooled.setIndexSet(testIndexSet2);
    LibraryPoolContent batchLpc2 = LibraryPoolContentFactory.newLibraryPoolContent()
        .libraryPool(testSubPool2)
        .pooledLibraryPrepBatch(testBatchUnpooled)
        .build();
    persist(batchLpc2);
    entityManager.flush();
    entityManager.refresh(testSubPool2);
    
    // Create the LPC to add sub-pool 2 to the initial test pool:
    LibraryPoolContentDto pooledPoolLpc2 = new LibraryPoolContentDto();
    pooledPoolLpc2.setLibraryPool(
        poolRepository.findOne(
            testLpc.getLibraryPool().getId(),
            new QuerySpec(LibraryPoolDto.class)
        )
    );
    pooledPoolLpc2.setPooledLibraryPool(
        poolRepository.findOne(
            testSubPool2.getId(),
            new QuerySpec(LibraryPoolDto.class)
        )
    );
    
    LibraryPoolContentDto createdPooledPoolLpc2 = lpcRepository.create(pooledPoolLpc2);
    assertNotNull(createdPooledPoolLpc2.getLibraryPoolContentId());
  }
  
  @Test
  public void createLpc_onDuplicateNestedLibraryPrepBatch_throwValidationException() {
    LibraryPrepBatch existingBatch = testLpc.getPooledLibraryPrepBatch();
    
    // The existing LPC should pool a pool:
    LibraryPool testSubPool1 = LibraryPoolFactory.newLibraryPool()
        .name("test sub-pool 1")
        .build();
    persist(testSubPool1);
    testLpc.setPooledLibraryPrepBatch(null);
    testLpc.setPooledLibraryPool(testSubPool1);
    
    // The nested pool will link to the existing LibraryPrepBatch:
    LibraryPoolContent batchLpc1 = LibraryPoolContentFactory.newLibraryPoolContent()
        .libraryPool(testSubPool1)
        .pooledLibraryPrepBatch(existingBatch)
        .build();
    persist(batchLpc1);
    entityManager.flush();
    entityManager.refresh(testSubPool1);
    
    // Add a new Sub-pool to add to the existing pool:
    LibraryPool testSubPool2 = LibraryPoolFactory.newLibraryPool()
        .name("test sub-pool 2")
        .build();
    persist(testSubPool2);
    // Sub-pool 2 will contain the same LibraryPrepBatch as sub-pool 1:
    LibraryPoolContent batchLpc2 = LibraryPoolContentFactory.newLibraryPoolContent()
        .libraryPool(testSubPool2)
        .pooledLibraryPrepBatch(existingBatch)
        .build();
    persist(batchLpc2);
    entityManager.flush();
    entityManager.refresh(testSubPool2);
    
    // Create the LPC to add sub-pool 2 to the initial test pool:
    LibraryPoolContentDto pooledPoolLpc2 = new LibraryPoolContentDto();
    pooledPoolLpc2.setLibraryPool(
        poolRepository.findOne(
            testLpc.getLibraryPool().getId(),
            new QuerySpec(LibraryPoolDto.class)
        )
    );
    pooledPoolLpc2.setPooledLibraryPool(
        poolRepository.findOne(
            testSubPool2.getId(),
            new QuerySpec(LibraryPoolDto.class)
        )
    );
    
    ValidationException exception = assertThrows(
        ValidationException.class,
        () -> lpcRepository.create(pooledPoolLpc2)
    );
    
    assertEquals(
        "Duplicate libary prep batch usage: Batch 'test batch 1' is already pooled.",
        exception.getMessage()
    );
  }
}
