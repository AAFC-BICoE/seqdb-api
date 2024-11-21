package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchDto;
import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MetagenomicsBatchItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MetagenomicsBatchTestFixture;

import javax.inject.Inject;

public class MetagenomicsBatchIT extends BaseRepositoryTestV2 {

  @Inject
  private MetagenomicsBatchRepository metagenomicsBatchRepository;

  @Inject
  private MetagenomicsBatchItemRepository metagenomicsBatchItemRepository;

  @Test
  public void onValidDto_dtoSavedWithoutExceptions() {

    MetagenomicsBatchDto genericMolecularAnalysisDto = metagenomicsBatchRepository
      .create(MetagenomicsBatchTestFixture.newMetagenomicsBatch());

    MetagenomicsBatchItemDto itemDto = MetagenomicsBatchItemTestFixture.newMetagenomicsBatchItem();
    itemDto.setMetagenomicsBatch(genericMolecularAnalysisDto);

    metagenomicsBatchItemRepository.create(itemDto);
  }
}
