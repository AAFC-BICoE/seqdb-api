package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchDto;
import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchItemDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisResultDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MetagenomicsBatchItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MetagenomicsBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisResultFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunTestFixture;

import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

public class MetagenomicsBatchIT extends BaseRepositoryTestV2 {

  @Inject
  private MetagenomicsBatchRepository metagenomicsBatchRepository;

  @Inject
  private MetagenomicsBatchItemRepository metagenomicsBatchItemRepository;

  @Inject
  private PcrBatchRepository pcrBatchRepository;

  @Inject
  private PcrBatchItemRepository pcrBatchItemRepository;

  @Inject
  private MolecularAnalysisRunRepository molecularAnalysisRunRepository;

  @Inject
  private MolecularAnalysisRunItemRepository molecularAnalysisRunItemRepository;

  @Inject
  private MolecularAnalysisResultRepository molecularAnalysisResultRepository;

  @Test
  public void onValidDto_dtoSavedWithoutExceptions() {

    UUID metagenomicsBatchDtoId = createWithRepository(MetagenomicsBatchTestFixture.newMetagenomicsBatch(), metagenomicsBatchRepository::onCreate);
//
//    PcrBatchDto pcrBatchDto = PcrBatchTestFixture.newPcrBatch();
//    pcrBatchDto = pcrBatchRepository.create(pcrBatchDto);
//
//    PcrBatchItemDto pcrBatchItemDto = PcrBatchItemTestFixture.newPcrBatchItem(pcrBatchDto);
//    pcrBatchItemDto = pcrBatchItemRepository.create(pcrBatchItemDto);

    UUID runDtoId = createWithRepository(MolecularAnalysisRunTestFixture.newMolecularAnalysisRun(), molecularAnalysisRunRepository::onCreate);
    UUID resultDtoId = createWithRepository(MolecularAnalysisResultFixture.newMolecularAnalysisResult(), molecularAnalysisResultRepository::onCreate);

    MolecularAnalysisRunItemDto runItemDto = MolecularAnalysisRunItemTestFixture
      .newMolecularAnalysisRunItem();

    JsonApiDocument molecularAnalysisRunItemDtoToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, MolecularAnalysisRunItemDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(runItemDto),
        Map.of("run", JsonApiDocument.ResourceIdentifier.builder().id(runDtoId)
            .type(MolecularAnalysisRunDto.TYPENAME).build(),
          "result", JsonApiDocument.ResourceIdentifier.builder().id(resultDtoId)
            .type(MolecularAnalysisResultDto.TYPENAME).build()));

    UUID molecularAnalysisRunItemId = createWithRepository(molecularAnalysisRunItemDtoToCreate,
      molecularAnalysisRunItemRepository::onCreate);


    MetagenomicsBatchItemDto metagenomicsBatchItemDto = MetagenomicsBatchItemTestFixture.newMetagenomicsBatchItem();
//    itemDto.setPcrBatchItem(pcrBatchItemDto);

    JsonApiDocument metagenomicsBatchItemDtoToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, MetagenomicsBatchItemDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(metagenomicsBatchItemDto),
        Map.of("metagenomicsBatch", JsonApiDocument.ResourceIdentifier.builder().id(metagenomicsBatchDtoId)
            .type(MetagenomicsBatchDto.TYPENAME).build(),
          "molecularAnalysisRunItem", JsonApiDocument.ResourceIdentifier.builder().id(molecularAnalysisRunItemId)
            .type(MolecularAnalysisRunItemDto.TYPENAME).build()));

    metagenomicsBatchItemRepository.onCreate(metagenomicsBatchItemDtoToCreate);
  }
}
