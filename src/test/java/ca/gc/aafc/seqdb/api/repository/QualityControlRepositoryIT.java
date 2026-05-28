package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.dto.QualityControlDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.QualityControlTestFixture;

import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

public class QualityControlRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private QualityControlRepository qualityControlRepository;

  @Inject
  private MolecularAnalysisRunRepository molecularAnalysisRunRepository;

  @Inject
  private MolecularAnalysisRunItemRepository molecularAnalysisRunItemRepository;

  @Test
  public void onValidDto_dtoSavedWithoutExceptions() {

    UUID runDtoId = createWithRepository(MolecularAnalysisRunTestFixture.newMolecularAnalysisRun(), molecularAnalysisRunRepository::onCreate);

    MolecularAnalysisRunItemDto runItemDto = MolecularAnalysisRunItemTestFixture
      .newMolecularAnalysisRunItem();
    JsonApiDocument molecularAnalysisRunItemDtoToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, MolecularAnalysisRunItemDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(runItemDto),
        Map.of("run", JsonApiDocument.ResourceIdentifier.builder().id(runDtoId)
            .type(MolecularAnalysisRunDto.TYPENAME).build()));
    UUID molecularAnalysisRunItemId = createWithRepository(molecularAnalysisRunItemDtoToCreate, molecularAnalysisRunItemRepository::onCreate);

    QualityControlDto qualityControlDto = QualityControlTestFixture.newQualityControl();

    JsonApiDocument qualityControlDtoToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, QualityControlDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(qualityControlDto),
        Map.of("molecularAnalysisRunItem", JsonApiDocument.ResourceIdentifier.builder().id(molecularAnalysisRunItemId)
          .type(MolecularAnalysisRunItemDto.TYPENAME).build()));

    qualityControlRepository.onCreate(qualityControlDtoToCreate);
  }

}
