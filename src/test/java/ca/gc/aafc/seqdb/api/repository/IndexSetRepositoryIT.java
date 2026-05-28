package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.seqdb.api.dto.IndexSetDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.IndexSetTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.UUID;
import jakarta.inject.Inject;

public class IndexSetRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private IndexSetRepository indexSetRepository;

  @Test
  public void createIndexSet_onSuccess_indexSetCreated()
      throws ResourceGoneException, ResourceNotFoundException {

    IndexSetDto dto = IndexSetTestFixture.newIndexSet();
    UUID indexSetId = createWithRepository(dto, indexSetRepository::onCreate);

    IndexSetDto reloadedDto = indexSetRepository.getOne(indexSetId, null).getDto();
    
    assertNotNull(dto);
    assertEquals(dto.getName(), reloadedDto.getName());
  }

  @Test
  public void updateIndexSet_onSuccess_indexSetUpdated()
      throws ResourceGoneException, ResourceNotFoundException {

    IndexSetDto dto = IndexSetTestFixture.newIndexSet();
    UUID indexSetId = createWithRepository(dto, indexSetRepository::onCreate);

    JsonApiDocument toUpdate = JsonApiDocuments.createJsonApiDocument(indexSetId, IndexSetDto.TYPENAME,
      Map.of("name", "updated name"));
    indexSetRepository.onUpdate(toUpdate, indexSetId);

    IndexSetDto reloadedDto = indexSetRepository.getOne(indexSetId, null).getDto();
    assertEquals("updated name", reloadedDto.getName());
  }

  @Test
  public void deleteIndexSet_onSuccess_indexSetDeleted()
      throws ResourceGoneException, ResourceNotFoundException {
    IndexSetDto dto = IndexSetTestFixture.newIndexSet();
    UUID indexSetId = createWithRepository(dto, indexSetRepository::onCreate);

    indexSetRepository.onDelete(indexSetId);

    assertThrows(ResourceNotFoundException.class,
      () -> indexSetRepository.getOne(indexSetId, null));
  }
}
