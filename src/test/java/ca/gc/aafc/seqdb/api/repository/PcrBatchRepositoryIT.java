package ca.gc.aafc.seqdb.api.repository;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import javax.inject.Inject;

import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrPrimerTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchDto;
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
    PcrPrimerDto primerForward = PcrPrimerTestFixture.newPcrPrimer();
    primerForward.setType(PrimerType.PRIMER);
    primerForward.setName("forward");
    primerForward.setDirection("F");

    PcrPrimerDto primerReverse = PcrPrimerTestFixture.newPcrPrimer();
    primerReverse.setType(PrimerType.PRIMER);
    primerReverse.setName("reverse");
    primerReverse.setDirection("R");

    primerForwardTest = pcrPrimerRepository.create(primerForward);
    primerReverseTest = pcrPrimerRepository.create(primerReverse);
    
    assertNotNull(primerForwardTest.getUuid());
    assertNotNull(primerForwardTest.getCreatedOn());

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

    assertEquals(PcrBatchTestFixture.GROUP, found.getGroup());
    assertEquals(PcrBatchTestFixture.CREATED_BY, found.getCreatedBy());
    assertEquals(PcrBatchTestFixture.THERMOCYCLER, found.getThermocycler());
    assertEquals(PcrBatchTestFixture.OBJECTIVE, found.getObjective());
    assertEquals(PcrBatchTestFixture.POSITIVE_CONTROL, found.getPositiveControl());
    assertEquals(PcrBatchTestFixture.REACTION_VOLUME, found.getReactionVolume());
    assertEquals(PcrBatchTestFixture.REACTION_DATE, found.getReactionDate());

    assertEquals(PcrBatchTestFixture.STORAGE_UNIT_UUID.toString(), found.getStorageUnit().getId());
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

    assertEquals(PcrBatchTestFixture.GROUP, created.getGroup());
    assertEquals(PcrBatchTestFixture.CREATED_BY, created.getCreatedBy());
    assertEquals(PcrBatchTestFixture.THERMOCYCLER, created.getThermocycler());
    assertEquals(PcrBatchTestFixture.OBJECTIVE, created.getObjective());
    assertEquals(PcrBatchTestFixture.POSITIVE_CONTROL, created.getPositiveControl());
    assertEquals(PcrBatchTestFixture.REACTION_VOLUME, created.getReactionVolume());
    assertEquals(PcrBatchTestFixture.REACTION_DATE, created.getReactionDate());
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
