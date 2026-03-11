package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.IndexSetDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolContentDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.IndexSetTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.LibraryPoolContentTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.LibraryPoolTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.LibraryPrepBatchTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.validation.ValidationException;

public class LibraryPoolContentRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private IndexSetRepository indexSetRepository;

  @Inject
  private LibraryPoolRepository libraryPoolRepository;

  @Inject
  private LibraryPrepBatchRepository libraryPrepBatchRepository;

  @Inject
  private LibraryPoolContentRepository libraryPoolContentRepository;

  private UUID createIndexSet(String name) {
    IndexSetDto indexSetDto = IndexSetTestFixture.newIndexSet();
    indexSetDto.setName(name);
    return createWithRepository(indexSetDto, indexSetRepository::onCreate);
  }

  private UUID createLibraryPrepBatchDto(String name, UUID indexSetId) {
    LibraryPrepBatchDto libraryPrepBatchDto = LibraryPrepBatchTestFixture.newLibraryPrepBatch();
    libraryPrepBatchDto.setName(name);

    JsonApiDocument dtoToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, LibraryPrepBatchDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(libraryPrepBatchDto),
        Map.of("indexSet", JsonApiDocument.ResourceIdentifier.builder().id(indexSetId)
          .type(IndexSetDto.TYPENAME).build()));

    return createWithRepository(dtoToCreate, libraryPrepBatchRepository::onCreate);
  }

  private UUID createLibraryPool(String name) {
    LibraryPoolDto libraryPoolDto = LibraryPoolTestFixture.newLibraryPool();
    libraryPoolDto.setName(name);
    return  createWithRepository(libraryPoolDto, libraryPoolRepository::onCreate);
  }

  private UUID createLibraryPoolContent(UUID poolId, UUID pooledLibraryPrepBatch) {
    LibraryPoolContentDto libraryPoolContentDto = LibraryPoolContentTestFixture.newLibraryPoolContent();

    Map<String, JsonApiDocument.ResourceIdentifier> rel = new HashMap<>();
    rel.put("libraryPool", JsonApiDocument.ResourceIdentifier.builder().id(poolId)
      .type(LibraryPoolDto.TYPENAME).build());

    if (pooledLibraryPrepBatch != null) {
      rel.put("pooledLibraryPrepBatch",
        JsonApiDocument.ResourceIdentifier.builder().id(pooledLibraryPrepBatch)
          .type(LibraryPrepBatchDto.TYPENAME).build());
    }

    JsonApiDocument dtoToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, LibraryPoolContentDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(libraryPoolContentDto), rel);

    return createWithRepository(dtoToCreate, libraryPoolContentRepository::onCreate);
  }

  @Test
  public void createLpc_onSuccess_lpcCreated()
      throws ResourceGoneException, ResourceNotFoundException {

    UUID indexSet1 = createIndexSet("test index set 1");
    UUID indexSet2 = createIndexSet("test index set 2");

    UUID libraryPrepBatchId = createLibraryPrepBatchDto("test batch 1", indexSet1);
    UUID libraryPrepBatchId2 = createLibraryPrepBatchDto("test batch 2", indexSet2);

    UUID libraryPoolId = createLibraryPool("test pool 1");
    UUID libraryPoolContentId = createLibraryPoolContent(libraryPoolId, libraryPrepBatchId);

    // Add to same pool as the test LPC
    UUID libraryPoolContentId2 = createLibraryPoolContent(libraryPoolId, libraryPrepBatchId2);

    LibraryPoolContentDto dto = libraryPoolContentRepository.getOne(libraryPoolContentId2, "include=libraryPool,pooledLibraryPrepBatch").getDto();
    // Assert parent pool
    assertEquals(libraryPoolId, dto.getLibraryPool().getUuid());
    // Assert pooled LibraryPrepBatch:
    assertEquals(libraryPrepBatchId2,
        dto.getPooledLibraryPrepBatch().getUuid());
  }

//  @Test
//  public void updateLpc_onSuccess_lpcUpdated() {
//    LibraryPoolContentDto dto = libraryPoolContentRepository.findOne(testLpc.getUuid(),
//        new QuerySpec(LibraryPoolContentDto.class));
//
//    dto.setPooledLibraryPrepBatch(testBatchDtoUnpooled);
//
//    LibraryPoolContentDto updated = libraryPoolContentRepository.save(dto);
//    assertEquals(testBatchDtoUnpooled.getUuid(),
//        updated.getPooledLibraryPrepBatch().getUuid());
//  }

//  @Test
//  public void deleteLpc_onSuccess_lpcDeleted() {
//    libraryPoolContentRepository.delete(testLpc.getUuid());
//    assertNull(entityManager.find(LibraryPoolContent.class, testLpc.getId()));
//  }
//
  @Test
  public void createLpc_onDuplicatePooledIndexSet_throwValidationException() {

    UUID indexSet = createIndexSet("test index set 1");
    UUID libraryPrepBatchId = createLibraryPrepBatchDto("test batch", indexSet);

    UUID libraryPoolId = createLibraryPool("test pool 1");
    UUID libraryPoolContentId = createLibraryPoolContent(libraryPoolId, libraryPrepBatchId);

    UUID libraryPoolContent2Id = createLibraryPoolContent(libraryPoolId, null);
    // Try to link a batch that is also using test index set 1:
    UUID libraryPrepBatchId2 = createLibraryPrepBatchDto("test batch2", indexSet);

    JsonApiDocument dtoToUpdate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(libraryPoolContent2Id, LibraryPoolContentDto.TYPENAME,
        Map.of(),
        Map.of("pooledLibraryPrepBatch", JsonApiDocument.ResourceIdentifier.builder().id(libraryPrepBatchId2)
          .type(LibraryPrepBatchDto.TYPENAME).build()));

    ValidationException exception = assertThrows( ValidationException.class,
      () -> libraryPoolContentRepository.onUpdate(dtoToUpdate, libraryPoolContent2Id));

    assertEquals(
        "Duplicate index set usage: 'test index set 1'",
        exception.getMessage()
    );
  }

  @Test
  public void createLpc_onDuplicateNestedPooledBatchIndexSet_throwValidationException() {
    UUID indexSet = createIndexSet("test index set 1");
    UUID libraryPrepBatchId = createLibraryPrepBatchDto("test batch", indexSet);
    UUID libraryPoolId = createLibraryPool("test pool 1");
    UUID libraryPoolContentId = createLibraryPoolContent(libraryPoolId, libraryPrepBatchId);

    // The existing LPC should pool a pool:
    UUID testSubPoolId = createLibraryPool("test sub-pool 1");
    UUID librarySubPoolContentId = createLibraryPoolContent(testSubPoolId, null);

    // The nested pool will link to the existing LibraryPrepBatch:
    UUID batchLibraryPoolContentId = createLibraryPoolContent(testSubPoolId, libraryPrepBatchId);

   // Add a new Sub-pool to add to the existing pool:
    UUID testSubPool2Id = createLibraryPool("test sub-pool 2");

    // Sub-pool 2 will contain a batch linking to the same index set as the existing batch:
    UUID libraryPrepBatch2Id = createLibraryPrepBatchDto("test batch 2", indexSet);
    UUID batchLibraryPoolContent2Id = createLibraryPoolContent(testSubPool2Id, libraryPrepBatch2Id);

    // Create the LPC to add sub-pool 2 to the initial test pool:
    LibraryPoolContentDto libraryPoolContentDto = LibraryPoolContentTestFixture.newLibraryPoolContent();
    JsonApiDocument dtoToUpdate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, LibraryPoolContentDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(libraryPoolContentDto),
        Map.of("libraryPool", JsonApiDocument.ResourceIdentifier.builder().id(libraryPoolId).type(LibraryPoolDto.TYPENAME).build(),
        "pooledLibraryPool", JsonApiDocument.ResourceIdentifier.builder().id(testSubPool2Id).type(LibraryPoolDto.TYPENAME).build()));

    ValidationException exception = assertThrows(
        ValidationException.class,
        () -> libraryPoolContentRepository.onCreate(dtoToUpdate)
    );

    assertEquals(
        "Duplicate index set usage: Batches 'test batch 2' and 'test batch 1' are both using index set 'test index set 1'",
        exception.getMessage()
    );
  }

//  @Test
//  public void createLpc_onNoDuplicateNestedPooledBatchIndexSet_lpcCreated() {
//    LibraryPrepBatch existingBatch = testLpc.getPooledLibraryPrepBatch();
//
//    // The existing LPC should pool a pool:
//    LibraryPool testSubPool1 = LibraryPoolFactory.newLibraryPool()
//        .name("test sub-pool 1")
//        .build();
//    persist(testSubPool1);
//    testLpc.setPooledLibraryPrepBatch(null);
//    testLpc.setPooledLibraryPool(testSubPool1);
//
//    // The nested pool will link to the existing LibraryPrepBatch:
//    LibraryPoolContent batchLpc1 = LibraryPoolContentFactory.newLibraryPoolContent()
//        .libraryPool(testSubPool1)
//        .pooledLibraryPrepBatch(existingBatch)
//        .build();
//    persist(batchLpc1);
//    entityManager.flush();
//    entityManager.refresh(testSubPool1);
//
//    // Add a new Sub-pool to add to the existing pool:
//    LibraryPool testSubPool2 = LibraryPoolFactory.newLibraryPool()
//        .name("test sub-pool 2")
//        .build();
//    persist(testSubPool2);
//    // Sub-pool 2 will contain a batch linking to a different index set than the existing batch:
//    testBatchUnpooled.setIndexSet(testIndexSet2);
//    LibraryPoolContent batchLpc2 = LibraryPoolContentFactory.newLibraryPoolContent()
//        .libraryPool(testSubPool2)
//        .pooledLibraryPrepBatch(testBatchUnpooled)
//        .build();
//    persist(batchLpc2);
//    entityManager.flush();
//    entityManager.refresh(testSubPool2);
//
//    // Create the LPC to add sub-pool 2 to the initial test pool:
//    LibraryPoolContentDto pooledPoolLpc2 = new LibraryPoolContentDto();
//    pooledPoolLpc2.setLibraryPool(
//        libraryPoolRepository.findOne(
//            testLpc.getLibraryPool().getUuid(),
//            new QuerySpec(LibraryPoolDto.class)
//        )
//    );
//    pooledPoolLpc2.setPooledLibraryPool(
//        libraryPoolRepository.findOne(
//            testSubPool2.getUuid(),
//            new QuerySpec(LibraryPoolDto.class)
//        )
//    );
//
//    LibraryPoolContentDto createdPooledPoolLpc2 = libraryPoolContentRepository.create(pooledPoolLpc2);
//    assertNotNull(createdPooledPoolLpc2.getUuid());
//  }
//
//  @Test
//  public void createLpc_onDuplicateNestedLibraryPrepBatch_throwValidationException() {
//    LibraryPrepBatch existingBatch = testLpc.getPooledLibraryPrepBatch();
//
//    // The existing LPC should pool a pool:
//    LibraryPool testSubPool1 = LibraryPoolFactory.newLibraryPool()
//        .name("test sub-pool 1")
//        .build();
//    persist(testSubPool1);
//    testLpc.setPooledLibraryPrepBatch(null);
//    testLpc.setPooledLibraryPool(testSubPool1);
//
//    // The nested pool will link to the existing LibraryPrepBatch:
//    LibraryPoolContent batchLpc1 = LibraryPoolContentFactory.newLibraryPoolContent()
//        .libraryPool(testSubPool1)
//        .pooledLibraryPrepBatch(existingBatch)
//        .build();
//    persist(batchLpc1);
//    entityManager.flush();
//    entityManager.refresh(testSubPool1);
//
//    // Add a new Sub-pool to add to the existing pool:
//    LibraryPool testSubPool2 = LibraryPoolFactory.newLibraryPool()
//        .name("test sub-pool 2")
//        .build();
//    persist(testSubPool2);
//    // Sub-pool 2 will contain the same LibraryPrepBatch as sub-pool 1:
//    LibraryPoolContent batchLpc2 = LibraryPoolContentFactory.newLibraryPoolContent()
//        .libraryPool(testSubPool2)
//        .pooledLibraryPrepBatch(existingBatch)
//        .build();
//    persist(batchLpc2);
//    entityManager.flush();
//    entityManager.refresh(testSubPool2);
//
//    // Create the LPC to add sub-pool 2 to the initial test pool:
//    LibraryPoolContentDto pooledPoolLpc2 = new LibraryPoolContentDto();
//    pooledPoolLpc2.setLibraryPool(
//        libraryPoolRepository.findOne(
//            testLpc.getLibraryPool().getUuid(),
//            new QuerySpec(LibraryPoolDto.class)
//        )
//    );
//    pooledPoolLpc2.setPooledLibraryPool(
//        libraryPoolRepository.findOne(
//            testSubPool2.getUuid(),
//            new QuerySpec(LibraryPoolDto.class)
//        )
//    );
//
//    ValidationException exception = assertThrows(
//        ValidationException.class,
//        () -> libraryPoolContentRepository.create(pooledPoolLpc2)
//    );
//
//    assertEquals(
//        "Duplicate libary prep batch usage: Batch 'test batch 1' is already pooled.",
//        exception.getMessage()
//    );
//  }
}
