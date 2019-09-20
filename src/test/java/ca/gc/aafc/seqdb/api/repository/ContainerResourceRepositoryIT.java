package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.Serializable;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.ContainerDto;
import ca.gc.aafc.seqdb.api.dto.ContainerTypeDto;
import ca.gc.aafc.seqdb.entities.Container;
import ca.gc.aafc.seqdb.entities.ContainerType;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class ContainerResourceRepositoryIT extends BaseRepositoryTest {

  private static final String TEST_CONTAINER_NAME = "test container";
  private static final Boolean TEST_FILLBYROW = true;

  private Container testContainer;
  private ContainerType testContainerType;

  @Inject
  private ResourceRepository<ContainerDto, Serializable> containerRepository;
  
  @Inject
  private ResourceRepository<ContainerTypeDto, Serializable> ctRepository;

  private Container createTestContainer() {
    testContainerType = new ContainerType();
    testContainerType.setName("test ct");
    testContainerType.setBaseType("basetype");
    testContainerType.setNumberOfColumns(8);
    testContainerType.setNumberOfRows(12);
    testContainerType.setNumberOfWells(
        testContainerType.getNumberOfRows() * testContainerType.getNumberOfColumns()
    );
    
    persist(testContainerType);
    
    testContainer = new Container();
    testContainer.setContainerType(testContainerType);
    testContainer.setContainerNumber(TEST_CONTAINER_NAME);
    testContainer.setFillByRow(TEST_FILLBYROW);
    
    persist(testContainer);
    
    return testContainer;
  }

  @BeforeEach
  public void setup() {
    createTestContainer();
  }

  @Test
  public void findContainer_whenExists_containerReturned() {
    ContainerDto dto = containerRepository.findOne(
        testContainer.getId(),
        new QuerySpec(ContainerDto.class)
    );
    
    assertNotNull(dto);
    assertEquals(TEST_CONTAINER_NAME, testContainer.getContainerNumber());
  }
  
  @Test
  public void createContainer_onSuccess_containerPersisted() {
    ContainerDto newDto = new ContainerDto();
    newDto.setContainerNumber(TEST_CONTAINER_NAME);
    newDto.setFillByRow(TEST_FILLBYROW);
    newDto.setContainerType(
        ctRepository.findOne(
            testContainerType.getId(),
            new QuerySpec(ContainerTypeDto.class)
        )
     );
    
    ContainerDto created = containerRepository.create(newDto);
    
    assertNotNull(created.getContainerId());
    assertEquals(TEST_CONTAINER_NAME, created.getContainerNumber());
    assertEquals(TEST_FILLBYROW, created.getFillByRow());
  }
  
  @Test
  public void updateContainer_onSucess_containerUpdated() {
    ContainerDto dto = containerRepository.findOne(
        testContainer.getId(),
        new QuerySpec(ContainerDto.class)
    );
    
    dto.setContainerNumber("new name");
    
    containerRepository.save(dto);
    
    assertEquals("new name", testContainer.getContainerNumber());
  }
  
  @Test
  public void deleteContainer_onSuccess_containerDeleted() {
    containerRepository.delete(testContainer.getId());
    assertNull(entityManager.find(Container.class, testContainer.getId()));
  }
}
