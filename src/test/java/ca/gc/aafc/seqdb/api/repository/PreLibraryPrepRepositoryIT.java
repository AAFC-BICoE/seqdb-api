package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ConflictException;
import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.dto.PreLibraryPrepDto;
import ca.gc.aafc.seqdb.api.dto.external.MaterialSampleExternalDto;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep.PreLibraryPrepType;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.LibraryPrepBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.LibraryPrepTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PreLibraryPrepTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.UUID;
import jakarta.inject.Inject;

/**
 * Integration test for PreLibraryPrep Repository.
 * 
 * This test is designed to work only using Postgresql, if you are using any other database to run
 * the test, the tests will be ignored.
 */
public class PreLibraryPrepRepositoryIT extends BaseRepositoryTestV2 {

  protected static final PreLibraryPrepType TEST_PRELIBRARYPREP_TYPE_CREATE = PreLibraryPrepType.SIZE_SELECTION;
  protected static final String TEST_PRELIBRARYPREP_NOTES_CREATE = "PreLibraryPrep notes create";
  protected static final Double TEST_PRELIBRARYPREP_CONCENTRATION_CREATE = 3.2;
  
  protected static final String TEST_PRELIBRARYPREP_NOTES_UPDATE = "PreLibraryPrep notes update";

  @Inject
  private LibraryPrepBatchRepository libraryPrepBatchRepository;

  @Inject
  private LibraryPrepRepository libraryPrepRepository;

  @Inject
  private PreLibraryPrepRepository preLibraryPrepRepository;

  @Test
  public void createPreLibraryPrep_onSuccess_allFieldsHaveSetValueAfterPersisted()
      throws ResourceGoneException, ResourceNotFoundException {
    PreLibraryPrepDto newPreLibraryPrep = PreLibraryPrepTestFixture.newPreLibraryPrep();
    newPreLibraryPrep.setPreLibraryPrepType(TEST_PRELIBRARYPREP_TYPE_CREATE);
    newPreLibraryPrep.setNotes(TEST_PRELIBRARYPREP_NOTES_CREATE);
    newPreLibraryPrep.setConcentration(TEST_PRELIBRARYPREP_CONCENTRATION_CREATE);

    UUID preLibraryPrepUuid = createWithRepository(newPreLibraryPrep, preLibraryPrepRepository::onCreate);
    PreLibraryPrepDto reloadedDto = preLibraryPrepRepository.getOne(preLibraryPrepUuid, "").getDto();

    // DTO has the set value.
    assertEquals(TEST_PRELIBRARYPREP_TYPE_CREATE, reloadedDto.getPreLibraryPrepType());
    assertEquals(TEST_PRELIBRARYPREP_NOTES_CREATE, reloadedDto.getNotes());
    assertEquals(TEST_PRELIBRARYPREP_CONCENTRATION_CREATE, reloadedDto.getConcentration());
  }

  @Test
  public void createPreLibraryPrep_withRelationship_persisted()
      throws ResourceGoneException, ResourceNotFoundException {
    UUID libraryPrepBatchUuid = createWithRepository(LibraryPrepBatchTestFixture.newLibraryPrepBatch(), libraryPrepBatchRepository::onCreate);

    LibraryPrepDto libraryPrepDto = LibraryPrepTestFixture.newLibraryPrep();
    JsonApiDocument libraryPrepDtoToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, LibraryPrepDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(libraryPrepDto),
        Map.of(
          "libraryPrepBatch", JsonApiDocument.ResourceIdentifier.builder().id(libraryPrepBatchUuid)
            .type(LibraryPrepBatchDto.TYPENAME).build(),
          "materialSample", JsonApiDocument.ResourceIdentifier.builder().id(UUID.randomUUID())
            .type(MaterialSampleExternalDto.EXTERNAL_TYPENAME).build()));
    UUID libraryPrepUuid = createWithRepository(libraryPrepDtoToCreate, libraryPrepRepository::onCreate);

    PreLibraryPrepDto preLibraryPrepDto = PreLibraryPrepTestFixture.newPreLibraryPrep();
    JsonApiDocument preLibraryPrepDtoToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, PreLibraryPrepDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(preLibraryPrepDto),
        Map.of("libraryPrep", JsonApiDocument.ResourceIdentifier.builder().id(libraryPrepUuid)
            .type(LibraryPrepBatchDto.TYPENAME).build()));
    UUID preLibraryPrep = createWithRepository(preLibraryPrepDtoToCreate, preLibraryPrepRepository::onCreate);

    PreLibraryPrepDto reloadedDto = preLibraryPrepRepository.getOne(preLibraryPrep, "include=libraryPrep").getDto();
    assertEquals(libraryPrepUuid, reloadedDto.getLibraryPrep().getUuid());
  }

  @Test
  public void updatePreLibraryPrep_whenSomeFieldsAreUpdated_preLibraryPrepReturnedWithSelectedFieldsUpdated()
      throws ResourceGoneException, ResourceNotFoundException, ConflictException {
    PreLibraryPrepDto newPreLibraryPrep = PreLibraryPrepTestFixture.newPreLibraryPrep();
    UUID preLibraryPrepUuid = createWithRepository(newPreLibraryPrep, preLibraryPrepRepository::onCreate);

    JsonApiDocument productToUpdate = JsonApiDocuments.createJsonApiDocument(preLibraryPrepUuid,
      PreLibraryPrepDto.TYPENAME, Map.of("notes", TEST_PRELIBRARYPREP_NOTES_UPDATE));
    preLibraryPrepRepository.onUpdate(productToUpdate, preLibraryPrepUuid);

    PreLibraryPrepDto reloadedDto = preLibraryPrepRepository.getOne(preLibraryPrepUuid, "").getDto();

   // Check that the entity has the new notes value.
   assertEquals(TEST_PRELIBRARYPREP_NOTES_UPDATE, reloadedDto.getNotes());
  }

  @Test
  public void deletePreLibraryPrep_onPreLibraryPrepLookup_preLibraryPrepNotFound()
      throws ResourceGoneException, ResourceNotFoundException {
    PreLibraryPrepDto newPreLibraryPrep = PreLibraryPrepTestFixture.newPreLibraryPrep();
    UUID preLibraryPrepUuid = createWithRepository(newPreLibraryPrep, preLibraryPrepRepository::onCreate);
    preLibraryPrepRepository.onDelete(preLibraryPrepUuid);
  }

  @Test
  public void deletePreLibraryPrep_onPreLibraryPrepNotFound_throwResourceNotFoundException() {
    assertThrows(
      ResourceNotFoundException.class,
      () -> preLibraryPrepRepository.onDelete(UUID.fromString("00000000-0000-0000-0000-000000000000"))
    );
  }
}
