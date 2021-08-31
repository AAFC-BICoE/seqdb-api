package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.entities.Protocol.ProtocolType;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqBatchTestFixture;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import ca.gc.aafc.seqdb.api.dto.ProtocolDto;


public class SeqBatchRepositoryIT extends BaseRepositoryTest {

  @Inject
  private RegionRepository regionRepository;

  @Inject
  private ThermocyclerProfileRepository thermocyclerProfileRepository;

  @Inject
  private ProtocolRepository protocolRepository;

  @Inject
  private SeqBatchRepository seqBatchRepository;

  private RegionDto regionTest;
  private ThermocyclerProfileDto thermocyclerProfileTest;
  private ProtocolDto protocolTest;

  @BeforeEach
  public void setup() {
    RegionDto region = new RegionDto();
    region.setSymbol("test");
    region.setName("region");

    regionTest = regionRepository.create(region);

    ThermocyclerProfileDto thermocyclerProfile = new ThermocyclerProfileDto();
    thermocyclerProfile.setName("thermocyclerProfile");

    thermocyclerProfileTest = thermocyclerProfileRepository.create(thermocyclerProfile);

    ProtocolDto protocol = new ProtocolDto();
    protocol.setName("protocol");
    protocol.setType(ProtocolType.SEQ_REACTION);

    protocolTest = protocolRepository.create(protocol);
  }

  @Test
  public void findSeqBatch_whenSeqBatchExists_SeqBatchReturned() {

    SeqBatchDto newDto = SeqBatchTestFixture.newSeqBatch();
    newDto.setRegion(regionTest);
    newDto.setThermocyclerProfile(thermocyclerProfileTest);
    newDto.setProtocol(protocolTest);

    SeqBatchDto created = seqBatchRepository.create(newDto);

    SeqBatchDto found = seqBatchRepository.findOne(
      created.getUuid(),
      new QuerySpec(SeqBatchDto.class)
    );

    assertNotNull(found);
    assertEquals(created.getGroup(), found.getGroup());
    assertEquals(regionTest.getUuid(), found.getRegion().getUuid());
    assertEquals(thermocyclerProfileTest.getUuid(), found.getThermocyclerProfile().getUuid());
    assertEquals(protocolTest.getUuid(), found.getProtocol().getUuid());
  }

  @Test
  public void createSeqBatch_onSuccess_SeqBatchCreated() {
    SeqBatchDto newDto = SeqBatchTestFixture.newSeqBatch();
    newDto.setRegion(regionTest);
    newDto.setThermocyclerProfile(thermocyclerProfileTest);
    newDto.setProtocol(protocolTest);

    SeqBatchDto created = seqBatchRepository.create(newDto);
    assertNotNull(created.getUuid());
    assertEquals(regionTest.getUuid(), created.getRegion().getUuid());
    assertEquals(thermocyclerProfileTest.getUuid(), created.getThermocyclerProfile().getUuid());
    assertEquals(protocolTest.getUuid(), created.getProtocol().getUuid());
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
