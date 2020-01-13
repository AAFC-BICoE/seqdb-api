package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.Serializable;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.ReactionComponentDto;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.Protocol;
import ca.gc.aafc.seqdb.entities.Protocol.ProtocolType;
import ca.gc.aafc.seqdb.entities.ReactionComponent;
import ca.gc.aafc.seqdb.testsupport.factories.ProtocolFactory;
import ca.gc.aafc.seqdb.testsupport.factories.ReactionComponentFactory;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class ReactionComponentResourceRepositoryIT extends BaseRepositoryTest{
  
  private static final String TEST_COMPONENT_NAME = "test component";
  
  @Inject
  private ResourceRepository<ReactionComponentDto, Serializable> componentRepository;
  
  private ReactionComponent testComponent;
  
  private Group testGroup;
  
  private Protocol testProtocol;
  
  private ReactionComponent createTestComponent() {
    testGroup = new Group("testGroup");
    persistGroup(testGroup);
    testProtocol = ProtocolFactory.newProtocol()
        .name("testProtocol")
        .group(testGroup)
        .type(ProtocolType.SEQ_REACTION)
        .build();
    persist(testProtocol);
    testComponent = ReactionComponentFactory.newReactionComponent()
        .name(TEST_COMPONENT_NAME)
        .concentration("testConcentration")
        .quantity(4F)
        .protocol(testProtocol)
        .build();
    persist(testComponent);
    return testComponent;
  }
  
  // Asserts entity was passed to dto properly
  private void verifyComponent(ReactionComponent entity, ReactionComponentDto dto) {
    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getConcentration(), entity.getConcentration());
    assertEquals(dto.getQuantity(), entity.getQuantity());
    assertEquals(dto.getProtocol().getProtocolId(), entity.getProtocol().getProtocolId());
  }
  
  @BeforeEach
  public void setup() {
    createTestComponent();
  }
  
  @Test
  public void findReactionComponent_whenNoFieldsAreSelected_reactionComponentReturnedWithAllFields() {
    // Searches for a reactionComponent using entity's id
    ReactionComponentDto reactionComponentDto = componentRepository
        .findOne(testComponent.getReactionComponentId(), new QuerySpec(ReactionComponentDto.class));
    assertNotNull(reactionComponentDto);
    // Verifies entity was passed to dto properly
    verifyComponent(testComponent, reactionComponentDto);
  }
  
  @Test
  public void findReactionComponent_whenFieldsAreSelected_reactionComponentReturnedWithSelectedFields() {
    // Searches for a reactionComponent using entity's id and returns the name and protocol id
    QuerySpec querySpec = new QuerySpec(ReactionComponentDto.class);
    querySpec.setIncludedFields(includeFieldSpecs("name", "protocol"));
    
    ReactionComponentDto componentDto = componentRepository
        .findOne(testComponent.getReactionComponentId(), querySpec);
    assertNotNull(componentDto);
    assertEquals(TEST_COMPONENT_NAME, componentDto.getName());
    assertEquals(testProtocol.getProtocolId(), componentDto.getProtocol().getProtocolId());
    assertNull(componentDto.getConcentration());
    assertNull(componentDto.getQuantity());
  }
  
}
