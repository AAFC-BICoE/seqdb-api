package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.ProtocolDto;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.Protocol;
import ca.gc.aafc.seqdb.api.entities.Protocol.ProtocolType;
import ca.gc.aafc.seqdb.api.testsupport.factories.ProtocolFactory;
import io.crnk.core.queryspec.QuerySpec;

public class ProtocolResourceRepositoryIT extends BaseRepositoryTest{

  private static final String TEST_PROTOCOL_NAME = "test protocol";

  private static final ProtocolType TEST_PROTOCOL_TYPE = ProtocolType.COLLECTION_EVENT;
  
  private Protocol testProtocol;
  
  private Product testKit;

  @Inject
  private ProtocolRepository protocolRepository;
  
  private Protocol createTestProtocol() {
    testKit = Product.builder().name("testKit").description("testF").build();
    persist(testKit);
    testProtocol = ProtocolFactory.newProtocol()
        .name(TEST_PROTOCOL_NAME)
        .type(TEST_PROTOCOL_TYPE)
        .version("A")
        .description("testDescription")
        .steps("14")
        .notes("testNotes")
        .reference("testReference")
        .equipment("testEquipment")
        .forwardPrimerConcentration("fpc")
        .reversePrimerConcentration("rpc")
        .reactionMixVolume("mixVolume")
        .reactionMixVolumePerTube("perTube")
        .kit(testKit)
        .build();
    persist(testProtocol);
    
    return testProtocol;
  }
  
  // Asserts entity was passed to dto properly
  private void verifyProtocol(Protocol entity, ProtocolDto dto) {
    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getType(), entity.getType());
    assertEquals(dto.getVersion(), entity.getVersion());
    assertEquals(dto.getDescription(), entity.getDescription());
    assertEquals(dto.getSteps(), entity.getSteps());
    assertEquals(dto.getNotes(), entity.getNotes());
    assertEquals(dto.getReference(), entity.getReference());
    assertEquals(dto.getEquipment(), entity.getEquipment());
    assertEquals(dto.getForwardPrimerConcentration(), entity.getForwardPrimerConcentration());
    assertEquals(dto.getReversePrimerConcentration(), entity.getReversePrimerConcentration());
    assertEquals(dto.getReactionMixVolume(), entity.getReactionMixVolume());
    assertEquals(dto.getReactionMixVolumePerTube(), entity.getReactionMixVolumePerTube());
    assertEquals(dto.getKit().getUuid(), entity.getKit().getUuid());
  }
  
  @BeforeEach
  public void setup() {
    createTestProtocol();
  }
  
  @Test
  public void findProtocol_whenNoFieldsAreSelected_protocolReturnedWithAllFields() {
    // Searches for a protocol using entity's id
    ProtocolDto protocolDto = protocolRepository.findOne(testProtocol.getUuid(),
        new QuerySpec(ProtocolDto.class));
    assertNotNull(protocolDto);
    // Verifies entity was passed to dto properly
    verifyProtocol(testProtocol, protocolDto);
  }
  
  @Test
  public void updateProtocol_whenSomeFieldsAreUpdated_protocolReturnedWithSelectedFieldsUpdated() {
    // Get the test protocol DTO.
    QuerySpec querySpec = new QuerySpec(ProtocolDto.class);

    ProtocolDto protocolDto = protocolRepository.findOne(
        testProtocol.getUuid(),querySpec);
    
    assertEquals("testDescription", protocolDto.getDescription());
    // Change the DTO's desc value.
    protocolDto.setDescription("new desc");
    
    // Save the DTO using the repository.
    protocolRepository.save(protocolDto);
    
    // Check that the entity has the new desc value.
    assertEquals("new desc", testProtocol.getDescription());
  }
 
}
