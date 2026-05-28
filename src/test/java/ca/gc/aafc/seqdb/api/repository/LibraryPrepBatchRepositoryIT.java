package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.LibraryPrepBatchTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.UUID;
import jakarta.inject.Inject;

public class LibraryPrepBatchRepositoryIT extends BaseRepositoryTestV2 {

  private static String TEST_NOTES = "test notes";

  //private LibraryPrepBatch testBatch;
 // private Product testProduct;

  private static final UUID TEST_STORAGE_UNIT_UUID = UUID.randomUUID();
  private static final UUID TEST_PROTOCOL_UUID = UUID.randomUUID();

  @Inject
  private LibraryPrepBatchRepository libraryPrepBatchRepository;

  @Inject
  private ProductRepository productRepository;

  private UUID createTestLibraryPrepBatch() {
    //testProduct = ProductFactory.newProduct().build();
    //persist(testProduct);

    LibraryPrepBatchDto dto = LibraryPrepBatchTestFixture.newLibraryPrepBatch();
    dto.setName("test batch");
    dto.setNotes(TEST_NOTES);
    //.product(testProduct)

    JsonApiDocument dtoToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, LibraryPrepBatchDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(dto),
        Map.of("protocol", JsonApiDocument.ResourceIdentifier.builder().id(TEST_PROTOCOL_UUID)
          .type("protocol").build()));
    return createWithRepository(dtoToCreate, libraryPrepBatchRepository::onCreate);
  }

  @Test
  public void libraryPrepBatch_create_libPrepBatchReturned()
      throws ResourceGoneException, ResourceNotFoundException {
    UUID batchId = createTestLibraryPrepBatch();

    LibraryPrepBatchDto dto = libraryPrepBatchRepository.getOne(batchId, "include=protocol").getDto();

    assertNotNull(dto);
    assertEquals(TEST_NOTES, dto.getNotes());
    assertEquals(TEST_PROTOCOL_UUID.toString(), dto.getProtocol().getId());
  }

  @Test
  public void updateBatch_onSuccess_batchUpdated()
      throws ResourceGoneException, ResourceNotFoundException {
    UUID batchId = createTestLibraryPrepBatch();

    JsonApiDocument libraryPrepBatchToUpdate = JsonApiDocuments.createJsonApiDocument(batchId,
      LibraryPrepBatchDto.TYPENAME, Map.of("notes", "updated notes"));

    libraryPrepBatchRepository.onUpdate(libraryPrepBatchToUpdate, batchId);
    LibraryPrepBatchDto dto = libraryPrepBatchRepository.getOne(batchId, null).getDto();
    assertEquals("updated notes", dto.getNotes());
  }

  @Test
  public void deleteBatch_onSuccess_batchDeleted() throws ResourceGoneException, ResourceNotFoundException {
    UUID batchId = createTestLibraryPrepBatch();

    libraryPrepBatchRepository.onDelete(batchId);

    // ResourceNotFoundException since there is no audit
    assertThrows(ResourceNotFoundException.class,
      () -> libraryPrepBatchRepository.getOne(batchId, null));
  }
}
