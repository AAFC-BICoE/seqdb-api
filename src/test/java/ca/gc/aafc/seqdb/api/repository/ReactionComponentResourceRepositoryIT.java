package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.ReactionComponentDto;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.Protocol;
import ca.gc.aafc.seqdb.entities.ReactionComponent;
import ca.gc.aafc.seqdb.entities.Protocol.ProtocolType;
import ca.gc.aafc.seqdb.factories.ProtocolFactory;
import ca.gc.aafc.seqdb.factories.ReactionComponentFactory;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;

public class ReactionComponentResourceRepositoryIT extends BaseRepositoryTest{
  
  private static final String TEST_COMPONENT_NAME = "test component";
  
  @Inject
  private ResourceRepositoryV2<ReactionComponentDto, Serializable> componentRepository;
  
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
        .quantity("4")
        .protocol(testProtocol)
        .build();
    persist(testComponent);
    return testComponent;
  }
  
  private void verifyComponent(ReactionComponent entity, ReactionComponentDto dto) {
    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getConcentration(), entity.getConcentration());
    assertEquals(dto.getQuantity(), entity.getQuantity());
    assertEquals(dto.getProtocol().getProtocolId(), entity.getProtocol().getProtocolId());
  }
  
  @Before
  public void setup() {
    createTestComponent();
  }
  
  @Test
  public void findReactionComponent_whenNoFieldsAreSelected_reactionComponentReturnedWithAllFields() {
    ReactionComponentDto reactionComponentDto = componentRepository.findOne(testComponent.getReactionComponentId(), new QuerySpec(ReactionComponentDto.class));
    assertNotNull(reactionComponentDto);
    verifyComponent(testComponent, reactionComponentDto);
  }
  
  @Test
  public void findReactionComponent_whenFieldsAreSelected_reactionComponentReturnedWithSelectedFields() {
    QuerySpec querySpec = new QuerySpec(ReactionComponentDto.class);
    querySpec.setIncludedFields(includeFieldSpecs("name", "protocol"));
    
    ReactionComponentDto componentDto = componentRepository.findOne(testComponent.getReactionComponentId(), querySpec);
    assertNotNull(componentDto);
    assertEquals(TEST_COMPONENT_NAME, componentDto.getName());
    assertNull(componentDto.getConcentration());
    assertEquals(testProtocol.getProtocolId(), componentDto.getProtocol().getProtocolId());
    assertNull(componentDto.getQuantity());
  }
  
  @Test
  public void deleteReactionComponent_callRepositoryDeleteOnId_reactionComponentNotFound() {
    componentRepository.delete(testComponent.getId());
    assertNull(entityManager.find(ReactionComponent.class, testComponent.getId()));
  }
  
  @Test(expected = ResourceNotFoundException.class)
  public void deleteReactionComponent_callRepositoryDeleteOnInvalidId_reactionComponentNotFound() {
    componentRepository.delete(123);
  }
  
  
}