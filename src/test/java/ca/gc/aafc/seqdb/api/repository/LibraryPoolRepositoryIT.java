package ca.gc.aafc.seqdb.api.repository;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ConflictException;
import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.LibraryPoolTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.UUID;
import jakarta.inject.Inject;

public class LibraryPoolRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private LibraryPoolRepository libraryPoolRepository;
  
  private UUID createTestLibraryPool() {
    LibraryPoolDto dto = LibraryPoolTestFixture.newLibraryPool();
    return createWithRepository(dto, libraryPoolRepository::onCreate);
  }

  @Test
  public void createPool_onSuccess_poolCreated()
      throws ResourceGoneException, ResourceNotFoundException {

    UUID poolId = createTestLibraryPool();
    LibraryPoolDto dto = libraryPoolRepository.getOne(poolId, "").getDto();
    assertTrue(StringUtils.isNotBlank(dto.getName()));
  }

  @Test
  public void updatePool_onSuccess_poolUpdated()
      throws ResourceGoneException, ResourceNotFoundException, ConflictException {

    UUID poolId = createTestLibraryPool();

    JsonApiDocument libraryPoolToUpdate = JsonApiDocuments.createJsonApiDocument(poolId,
      LibraryPoolDto.TYPENAME, Map.of("name", "updated name"));
    libraryPoolRepository.onUpdate(libraryPoolToUpdate, poolId);

    LibraryPoolDto dto = libraryPoolRepository.getOne(poolId, "").getDto();
    assertEquals("updated name", dto.getName());
  }

  @Test
  public void deletePool_onSuccess_poolDeleted()
      throws ResourceGoneException, ResourceNotFoundException {
    UUID poolId = createTestLibraryPool();

    libraryPoolRepository.onDelete(poolId);

    // ResourceNotFoundException since there is no audit
    assertThrows(ResourceNotFoundException.class, () -> libraryPoolRepository.getOne(poolId, null));
  }
}
