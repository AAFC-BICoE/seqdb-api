package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.dto.external.MaterialSampleExternalDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.LibraryPrepBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.LibraryPrepTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

public class LibraryPrepRepositoryIT extends BaseRepositoryTestV2 {

  private static final String TEST_QUALITY = "test quality";

  @Inject
  private LibraryPrepBatchRepository libraryPrepBatchRepository;

  @Inject
  private LibraryPrepRepository libraryPrepRepository;

  private UUID createTestLibraryPrepBatch() {
    LibraryPrepBatchDto dto = LibraryPrepBatchTestFixture.newLibraryPrepBatch();
    return createWithRepository(dto, libraryPrepBatchRepository::onCreate);
  }

  private UUID createTestLibraryPrep(UUID batchId) {

    LibraryPrepDto dto = LibraryPrepTestFixture.newLibraryPrep();
    dto.setQuality(TEST_QUALITY);

    JsonApiDocument libraryPrepDtoToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, LibraryPrepDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(dto),
        Map.of("libraryPrepBatch", JsonApiDocument.ResourceIdentifier.builder().id(batchId)
            .type(LibraryPrepBatchDto.TYPENAME).build(),
          "materialSample", JsonApiDocument.ResourceIdentifier.builder().id(UUID.randomUUID())
            .type(MaterialSampleExternalDto.EXTERNAL_TYPENAME).build()));
    return createWithRepository(libraryPrepDtoToCreate, libraryPrepRepository::onCreate);
  }

  @Test
  public void libPrep_create_libPrepReturned()
      throws ResourceGoneException, ResourceNotFoundException {
    UUID batchId = createTestLibraryPrepBatch();
    UUID libraryPrepId = createTestLibraryPrep(batchId);

    LibraryPrepDto dto = libraryPrepRepository.getOne(libraryPrepId, null).getDto();
    
    assertNotNull(dto);
    assertEquals(TEST_QUALITY, dto.getQuality());
  }

  @Test
  public void updateLibPrep_onSuccess_libPrepUpdated()
      throws ResourceGoneException, ResourceNotFoundException {
    UUID batchId = createTestLibraryPrepBatch();
    UUID libraryPrepId = createTestLibraryPrep(batchId);

    JsonApiDocument libraryPrepToUpdate = JsonApiDocuments.createJsonApiDocument(libraryPrepId,
      LibraryPrepDto.TYPENAME, Map.of("quality", "updated quality"));

    libraryPrepRepository.onUpdate(libraryPrepToUpdate, libraryPrepId);

    LibraryPrepDto dto = libraryPrepRepository.getOne(libraryPrepId, null).getDto();
    assertEquals("updated quality", dto.getQuality());
  }

  @Test
  public void deleteLibPrep_onSuccess_libPrepDeleted()
      throws ResourceGoneException, ResourceNotFoundException {
    UUID batchId = createTestLibraryPrepBatch();
    UUID libraryPrepId = createTestLibraryPrep(batchId);

    libraryPrepRepository.onDelete(libraryPrepId);

    // ResourceNotFoundException since there is no audit
    assertThrows(ResourceNotFoundException.class, () -> libraryPrepRepository.getOne(libraryPrepId, null));
  }
}
