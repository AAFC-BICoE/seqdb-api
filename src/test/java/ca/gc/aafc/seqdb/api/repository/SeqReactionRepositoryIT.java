package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.dto.SeqReactionDto;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrPrimerTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqReactionTestFixture;

import io.crnk.core.queryspec.IncludeRelationSpec;
import io.crnk.core.queryspec.PathSpec;
import io.crnk.core.queryspec.QuerySpec;
import org.junit.jupiter.api.Test;

import java.util.List;
import javax.inject.Inject;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class SeqReactionRepositoryIT extends BaseRepositoryTest {

  @Inject
  private SeqReactionRepository seqReactionRepository;

  @Inject
  private SeqBatchRepository seqBatchRepository;

  @Inject
  private PcrBatchItemRepository pcrBatchItemRepository;

  @Inject
  private PcrPrimerRepository pcrPrimerRepository;

  private RegionDto regionTest;
  private ThermocyclerProfileDto thermocyclerProfileTest;
  private static final UUID TEST_PROTOCOL_UUID = UUID.randomUUID();


  public SeqReactionDto setupSeqReaction() {

    // Create SeqBatch, PcrBatchItem
    SeqBatchDto seqBatchDto = seqBatchRepository.create(SeqBatchTestFixture.newSeqBatch());
    PcrBatchItemDto pcrBatchItemDto = pcrBatchItemRepository.create(PcrBatchItemTestFixture.newPcrBatchItem());
    PcrPrimerDto pcrPrimerDto = pcrPrimerRepository.create(PcrPrimerTestFixture.newPcrPrimer());

    SeqReactionDto seqReaction = SeqReactionTestFixture.newSeqReaction();
    seqReaction.setSeqBatch(seqBatchDto);
    seqReaction.setPcrBatchItem(pcrBatchItemDto);
    seqReaction.setSeqPrimer(pcrPrimerDto);

    return seqReactionRepository.create(seqReaction);
  }

  @Test
  public void createSeqReaction_onSuccess_SeqReactionCreated() {
    SeqReactionDto seqReactionDto = setupSeqReaction();
    assertNotNull(seqReactionDto.getUuid());
  }

  @Test
  public void updateSeqReaction_onSuccess_SeqReactionUpdated() {
    SeqReactionDto seqReactionCreated = setupSeqReaction();
    assertNotNull(seqReactionCreated.getUuid());

    PcrPrimerDto pcrPrimerDto = PcrPrimerTestFixture.newPcrPrimer();
    pcrPrimerDto.setName("Updated Primer");
    pcrPrimerDto = pcrPrimerRepository.create(pcrPrimerDto);

    SeqReactionDto found = seqReactionRepository.findOne(
            seqReactionCreated.getUuid(),
            new QuerySpec(SeqReactionDto.class)
    );
    found.setSeqPrimer(pcrPrimerDto);
    seqReactionRepository.save(found);

    QuerySpec querySpec = new QuerySpec(SeqReactionDto.class);
    querySpec.setIncludedRelations(List.of(new IncludeRelationSpec(PathSpec.of("seqPrimer"))));

    SeqReactionDto updated = seqReactionRepository.findOne(seqReactionCreated.getUuid(), querySpec);

    assertEquals("Updated Primer", updated.getSeqPrimer().getName());
  }
  
}
