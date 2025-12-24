package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisDto;
import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.GenericMolecularAnalysisItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.GenericMolecularAnalysisTestFixture;

import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

public class GenericMolecularAnalysisRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private GenericMolecularAnalysisRepository genericMolecularAnalysisRepository;

  @Inject
  private GenericMolecularAnalysisItemRepository genericMolecularAnalysisItemRepository;

  @Test
  public void onValidDto_dtoSavedWithoutExceptions() {

    UUID molecularAnalysisId = createWithRepository(GenericMolecularAnalysisTestFixture.newGenericMolecularAnalysis(), genericMolecularAnalysisRepository::onCreate);

    GenericMolecularAnalysisItemDto itemDto = GenericMolecularAnalysisItemTestFixture.newGenericMolecularAnalysisItem();

    // private GenericMolecularAnalysisDto genericMolecularAnalysis;
    JsonApiDocument itemToCreate = JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, GenericMolecularAnalysisItemDto.TYPENAME,
      JsonAPITestHelper.toAttributeMap(itemDto), Map.of("genericMolecularAnalysis", JsonApiDocument.ResourceIdentifier.builder()
        .id(molecularAnalysisId).type(GenericMolecularAnalysisDto.TYPENAME).build()));
    genericMolecularAnalysisItemRepository.onCreate(itemToCreate);
  }
}
