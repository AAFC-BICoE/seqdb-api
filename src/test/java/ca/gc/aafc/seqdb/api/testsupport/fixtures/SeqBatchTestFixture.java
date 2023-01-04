package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.StorageRestrictionFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

import java.time.LocalDate;

public class SeqBatchTestFixture {

  private static final String GROUP = "aafc";
  private static final String CREATED_BY = "createdBy";

  public static SeqBatchDto newSeqBatch() {
    SeqBatchDto seqBatchDto = new SeqBatchDto();
    seqBatchDto.setGroup(GROUP);
    seqBatchDto.setCreatedBy(CREATED_BY);
    seqBatchDto.setReactionDate(LocalDate.of(2020,3,2));
    seqBatchDto.setName(TestableEntityFactory.generateRandomName(10));

    seqBatchDto.setStorageRestriction(StorageRestrictionFactory.newStorageRestriction().build());
    return seqBatchDto;
  }
  
}

