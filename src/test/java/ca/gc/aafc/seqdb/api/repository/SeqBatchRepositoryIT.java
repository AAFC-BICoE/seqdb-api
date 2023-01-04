package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import javax.inject.Inject;
import javax.validation.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqBatchTestFixture;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;


public class SeqBatchRepositoryIT extends BaseRepositoryTest {

  @Inject
  private RegionRepository regionRepository;

  @Inject
  private ThermocyclerProfileRepository thermocyclerProfileRepository;

  @Inject
  private SeqBatchRepository seqBatchRepository;

  private RegionDto regionTest;
  private ThermocyclerProfileDto thermocyclerProfileTest;
  private static final UUID TEST_PROTOCOL_UUID = UUID.randomUUID();

  @BeforeEach
  public void setup() {
    RegionDto region = new RegionDto();
    region.setSymbol("test");
    region.setName("region");

    regionTest = regionRepository.create(region);

    ThermocyclerProfileDto thermocyclerProfile = new ThermocyclerProfileDto();
    thermocyclerProfile.setName("thermocyclerProfile");

    thermocyclerProfileTest = thermocyclerProfileRepository.create(thermocyclerProfile);

  }

  @Test
  public void findSeqBatch_whenSeqBatchExists_SeqBatchReturned() {
    SeqBatchDto newDto = SeqBatchTestFixture.newSeqBatch();
    newDto.setRegion(regionTest);
    newDto.setThermocyclerProfile(thermocyclerProfileTest);
    newDto.setProtocol(ExternalRelationDto.builder().id(TEST_PROTOCOL_UUID.toString()).type("protocol").build());

    SeqBatchDto created = seqBatchRepository.create(newDto);

    SeqBatchDto found = seqBatchRepository.findOne(
      created.getUuid(),
      new QuerySpec(SeqBatchDto.class)
    );

    assertNotNull(found);
    assertEquals(created.getGroup(), found.getGroup());
    assertEquals(regionTest.getUuid(), found.getRegion().getUuid());
    assertEquals(thermocyclerProfileTest.getUuid(), found.getThermocyclerProfile().getUuid());
    assertEquals(TEST_PROTOCOL_UUID.toString(), found.getProtocol().getId());
  }

  @Test
  public void createSeqBatch_onSuccess_SeqBatchCreated() {
    SeqBatchDto newDto = SeqBatchTestFixture.newSeqBatch();
    newDto.setRegion(regionTest);
    newDto.setThermocyclerProfile(thermocyclerProfileTest);
    newDto.setProtocol(ExternalRelationDto.builder().id(TEST_PROTOCOL_UUID.toString()).type("protocol").build());

    SeqBatchDto created = seqBatchRepository.create(newDto);
    assertNotNull(created.getUuid());
    assertEquals(regionTest.getUuid(), created.getRegion().getUuid());
    assertEquals(thermocyclerProfileTest.getUuid(), created.getThermocyclerProfile().getUuid());
    assertEquals(TEST_PROTOCOL_UUID.toString(), created.getProtocol().getId());
  }

  @Test
  public void createSeqBatch_proveBothStorage_Exception() {
    SeqBatchDto newDto = SeqBatchTestFixture.newSeqBatch();
    newDto.setRegion(regionTest);
    newDto.setThermocyclerProfile(thermocyclerProfileTest);
    newDto.setStorageUnit(ExternalRelationDto.builder().id(UUID.randomUUID().toString()).type("storage-unit").build());
    newDto.setStorageUnitType(ExternalRelationDto.builder().id(UUID.randomUUID().toString()).type("storage-unit-type").build());
    assertThrows(ValidationException.class, () -> seqBatchRepository.create(newDto));
  }

  @Test
  public void updateSeqBatch_onSuccess_PcrBatchUpdated() {
    SeqBatchDto newDto = SeqBatchTestFixture.newSeqBatch();

    SeqBatchDto created = seqBatchRepository.create(newDto);
    assertNotNull(created.getUuid());

    SeqBatchDto found = seqBatchRepository.findOne(
      created.getUuid(),
      new QuerySpec(SeqBatchDto.class)
    );

    found.setName("updatedName");
    SeqBatchDto updated = seqBatchRepository.save(found);
    assertEquals("updatedName", updated.getName());
  }

  @Test
  public void deleteSeqBatch_onSuccess_SeqBatchDeleted() {
    SeqBatchDto newDto = SeqBatchTestFixture.newSeqBatch();

    SeqBatchDto created = seqBatchRepository.create(newDto);
    assertNotNull(created.getUuid());

    seqBatchRepository.delete(created.getUuid());
    assertThrows(ResourceNotFoundException.class, () -> seqBatchRepository.findOne(
      created.getUuid(),
      new QuerySpec(SeqBatchDto.class)
    ));
  }
  
}
