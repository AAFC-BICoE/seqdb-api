package ca.gc.aafc.seqdb.api.repository;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer.PrimerType;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;

import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

public class PcrBatchRepositoryIT extends BaseRepositoryTest {

  @Inject
  protected PcrBatchRepository pcrBatchRepository;

  @Inject
  protected PcrPrimerRepository pcrPrimerRepository;

  @Inject
  protected RegionRepository regionRepository;

  private PcrPrimerDto primerForwardTest;
  private PcrPrimerDto primerReverseTest;
  private RegionDto regionTest;

  @BeforeEach
  public void setup() {
    PcrPrimerDto primerForward = new PcrPrimerDto();
    primerForward.setType(PrimerType.PRIMER);
    primerForward.setName("forward");
    primerForward.setSeq("CTTGGTCATTTAGAGGAAGTAA");
    primerForward.setDirection("F");
    primerForward.setLotNumber(1);

    PcrPrimerDto primerReverse = new PcrPrimerDto();
    primerReverse.setType(PrimerType.PRIMER);
    primerReverse.setName("reverse");
    primerReverse.setSeq("CTTGGTCATTTAGAGGAAGTAA");
    primerReverse.setDirection("R");
    primerReverse.setLotNumber(1);

    primerForwardTest = pcrPrimerRepository.create(primerForward);
    primerReverseTest = pcrPrimerRepository.create(primerReverse);

    RegionDto region = new RegionDto();
    region.setSymbol("test");
    region.setName("region");

    regionTest = regionRepository.create(region);
  }

  @Test
  public void findPcrBatch_whenPcrBatchExists_PcrBatchReturned() {

    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    newDto.setPrimerForward(primerForwardTest);
    newDto.setPrimerReverse(primerReverseTest);
    newDto.setRegion(regionTest);
    
    PcrBatchDto created = pcrBatchRepository.create(newDto);

    PcrBatchDto found = pcrBatchRepository.findOne(
        created.getUuid(),
        new QuerySpec(PcrBatchDto.class)
    );
    
    assertNotNull(found);
    assertEquals(created.getGroup(), found.getGroup());
    assertEquals(primerForwardTest.getUuid(), found.getPrimerForward().getUuid());
    assertEquals(primerReverseTest.getUuid(), found.getPrimerReverse().getUuid());
    assertEquals(regionTest.getUuid(), found.getRegion().getUuid());

  }
  
  @Test
  public void createPcrBatch_onSuccess_PcrBatchCreated() {
    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    newDto.setPrimerForward(primerForwardTest);
    newDto.setPrimerReverse(primerReverseTest);
    newDto.setRegion(regionTest);
    
    PcrBatchDto created = pcrBatchRepository.create(newDto);
    assertNotNull(created.getUuid());
    assertEquals(primerForwardTest.getUuid(), created.getPrimerForward().getUuid());
    assertEquals(primerReverseTest.getUuid(), created.getPrimerReverse().getUuid());
    assertEquals(regionTest.getUuid(), created.getRegion().getUuid());
  }
  
  @Test
  public void updatePcrBatch_onSuccess_PcrBatchUpdated() {
    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    
    PcrBatchDto created = pcrBatchRepository.create(newDto);
    assertNotNull(created.getUuid());

    PcrBatchDto found = pcrBatchRepository.findOne(
        created.getUuid(),
        new QuerySpec(PcrBatchDto.class)
    );

    UUID uuid = UUID.randomUUID();

    List<UUID> experimenters = new ArrayList<>();

    experimenters.add(uuid);
    
    found.setExperimenters(experimenters);;
    PcrBatchDto updated = pcrBatchRepository.save(found);
    assertEquals(uuid, updated.getExperimenters().get(0));
  }
  
  @Test
  public void deletePcrBatch_onSuccess_PcrBatchDeleted() {
    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    
    PcrBatchDto created = pcrBatchRepository.create(newDto);
    assertNotNull(created.getUuid());

    pcrBatchRepository.delete(created.getUuid());
    assertThrows(ResourceNotFoundException.class, () -> pcrBatchRepository.findOne(
      created.getUuid(),
      new QuerySpec(PcrBatchDto.class)
    ));
  }

  
}
