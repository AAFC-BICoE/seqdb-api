package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ConflictException;
import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.IndexSetDto;
import ca.gc.aafc.seqdb.api.dto.NgsIndexDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.IndexSetTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.NgsIndexTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.UUID;
import jakarta.inject.Inject;

public class NgsIndexRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private NgsIndexRepository ngsIndexRepository;

  @Inject
  private IndexSetRepository indexSetRepository;

  @Test
  public void createNgsIndex_onSuccess_ngsIndexCreated()
      throws ResourceGoneException, ResourceNotFoundException {

    IndexSetDto indexSetDto = IndexSetTestFixture.newIndexSet();
    UUID indexSetId = createWithRepository(indexSetDto, indexSetRepository::onCreate);

    NgsIndexDto ngsIndexDto = NgsIndexTestFixture.newNgsIndex();
    JsonApiDocument toCreate = JsonApiDocuments.createJsonApiDocumentWithRelToOne(null,
      NgsIndexDto.TYPENAME, JsonAPITestHelper.toAttributeMap(ngsIndexDto),
      Map.of("indexSet", JsonApiDocument.ResourceIdentifier.builder()
        .type(IndexSetDto.TYPENAME)
        .id(indexSetId).build())
    );

    UUID ngsIndexSetId = createWithRepository(toCreate, ngsIndexRepository::onCreate);

    NgsIndexDto reloadedDto = ngsIndexRepository.getOne(ngsIndexSetId, "include=indexSet").getDto();

    assertNotNull(reloadedDto.getIndexSet().getUuid());
    assertEquals(ngsIndexDto.getName(), reloadedDto.getName());
  }

  @Test
  public void updateNgsIndex_onSuccess_ngsIndexUpdated()
      throws ResourceGoneException, ResourceNotFoundException, ConflictException {

    NgsIndexDto ngsIndexDto = NgsIndexTestFixture.newNgsIndex();
    UUID ngsIndexId = createWithRepository(ngsIndexDto, ngsIndexRepository::onCreate);

    JsonApiDocument toUpdate = JsonApiDocuments.createJsonApiDocument(ngsIndexId, NgsIndexDto.TYPENAME,
      Map.of("name", "updated name"));
    ngsIndexRepository.onUpdate(toUpdate, ngsIndexId);

    NgsIndexDto reloadedDto = ngsIndexRepository.getOne(ngsIndexId, null).getDto();
    assertEquals("updated name", reloadedDto.getName());
  }

  @Test
  public void deleteNgsIndex_onSuccess_ngsIndexDeleted()
      throws ResourceGoneException, ResourceNotFoundException {
    NgsIndexDto ngsIndexDto = NgsIndexTestFixture.newNgsIndex();
    UUID ngsIndexId = createWithRepository(ngsIndexDto, ngsIndexRepository::onCreate);

    ngsIndexRepository.onDelete(ngsIndexId);

    assertThrows(ResourceNotFoundException.class,
      () -> ngsIndexRepository.getOne(ngsIndexId, null));
  }
}
