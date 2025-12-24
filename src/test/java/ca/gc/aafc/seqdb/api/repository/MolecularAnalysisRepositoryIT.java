package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisResultDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisResultFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunTestFixture;

import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

/**
 * Tests for all MolecularAnalysisRun-based entities.
 */
public class MolecularAnalysisRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private MolecularAnalysisRunRepository molecularAnalysisRunRepository;

  @Inject
  private MolecularAnalysisRunItemRepository molecularAnalysisRunItemRepository;

  @Inject
  private MolecularAnalysisResultRepository molecularAnalysisResultRepository;

  @Test
  public void onValidDto_dtoSavedWithoutExceptions() {

    UUID runDtoId = createWithRepository(MolecularAnalysisRunTestFixture.newMolecularAnalysisRun(), molecularAnalysisRunRepository::onCreate);
    UUID resultDtoId = createWithRepository(MolecularAnalysisResultFixture.newMolecularAnalysisResult(), molecularAnalysisResultRepository::onCreate);

    MolecularAnalysisRunItemDto runItemDto = MolecularAnalysisRunItemTestFixture
      .newMolecularAnalysisRunItem();

    JsonApiDocument itemToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, MolecularAnalysisRunItemDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(runItemDto),
        Map.of("run", JsonApiDocument.ResourceIdentifier.builder().id(runDtoId)
            .type(MolecularAnalysisRunDto.TYPENAME).build(),
          "result", JsonApiDocument.ResourceIdentifier.builder().id(resultDtoId)
            .type(MolecularAnalysisResultDto.TYPENAME).build()));

    molecularAnalysisRunItemRepository.onCreate(itemToCreate);
  }
}
