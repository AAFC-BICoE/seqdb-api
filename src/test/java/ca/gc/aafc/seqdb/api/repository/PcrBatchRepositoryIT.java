package ca.gc.aafc.seqdb.api.repository;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer.PrimerType;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;

import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

public class PcrBatchRepositoryIT extends BaseRepositoryTest {

  @Inject
  private PcrBatchRepository pcrBatchRepository;

  @Inject
  private PcrPrimerRepository pcrPrimerRepository;

  @Inject
  private RegionRepository regionRepository;

  @Inject
  private ThermocyclerProfileRepository thermocyclerProfileRepository;

  private PcrPrimerDto primerForwardTest;
  private PcrPrimerDto primerReverseTest;
  private RegionDto regionTest;
  private ThermocyclerProfileDto thermocyclerProfileTest;

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

    ThermocyclerProfileDto thermocyclerProfileDto = new ThermocyclerProfileDto();
    thermocyclerProfileDto.setName("thermocyclerProfile");

    thermocyclerProfileTest = thermocyclerProfileRepository.create(thermocyclerProfileDto);
  }

  @Test
  public void findPcrBatch_whenPcrBatchExists_PcrBatchReturned() {

    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    newDto.setPrimerForward(primerForwardTest);
    newDto.setPrimerReverse(primerReverseTest);
    newDto.setRegion(regionTest);
    newDto.setThermocyclerProfile(thermocyclerProfileTest);
    
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
    assertEquals(thermocyclerProfileTest.getUuid(), found.getThermocyclerProfile().getUuid());
  }
  
  @Test
  public void createPcrBatch_onSuccess_PcrBatchCreated() {
    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    newDto.setPrimerForward(primerForwardTest);
    newDto.setPrimerReverse(primerReverseTest);
    newDto.setRegion(regionTest);
    newDto.setThermocyclerProfile(thermocyclerProfileTest);
    
    PcrBatchDto created = pcrBatchRepository.create(newDto);
    assertNotNull(created.getUuid());
    assertEquals(primerForwardTest.getUuid(), created.getPrimerForward().getUuid());
    assertEquals(primerReverseTest.getUuid(), created.getPrimerReverse().getUuid());
    assertEquals(regionTest.getUuid(), created.getRegion().getUuid());
    assertEquals(thermocyclerProfileTest.getUuid(), created.getThermocyclerProfile().getUuid());
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
    
    found.setName("updatedName");
    PcrBatchDto updated = pcrBatchRepository.save(found);
    assertEquals("updatedName", updated.getName());
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
