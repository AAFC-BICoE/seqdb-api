package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchDto;
import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchItemDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MetagenomicsBatchItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MetagenomicsBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;

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

  @Test
  public void onValidDto_dtoSavedWithoutExceptions() {

    MetagenomicsBatchDto metagenomicsBatchDto = metagenomicsBatchRepository
      .create(MetagenomicsBatchTestFixture.newMetagenomicsBatch());

    PcrBatchDto pcrBatchDto = PcrBatchTestFixture.newPcrBatch();
    pcrBatchDto = pcrBatchRepository.create(pcrBatchDto);

    PcrBatchItemDto pcrBatchItemDto = PcrBatchItemTestFixture.newPcrBatchItem(pcrBatchDto);
    pcrBatchItemDto = pcrBatchItemRepository.create(pcrBatchItemDto);

    MetagenomicsBatchItemDto itemDto = MetagenomicsBatchItemTestFixture.newMetagenomicsBatchItem(metagenomicsBatchDto);
    itemDto.setPcrBatchItem(pcrBatchItemDto);

    metagenomicsBatchItemRepository.create(itemDto);
  }
}
