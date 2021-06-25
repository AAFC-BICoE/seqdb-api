package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import ca.gc.aafc.seqdb.api.dto.ContainerTypeDto;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import io.crnk.core.queryspec.QuerySpec;

public class ContainerTypeResourceRepositoryIT extends BaseRepositoryTest {

  @Inject
  private ContainerTypeRepository containerTypeRepository;

  private static final String TEST_CONTAINERTYPE_NAME = "test ct";
  private static final Integer TEST_CONTAINERTYPE_COLS = 8;
  private static final Integer TEST_CONTAINERTYPE_ROWS = 12;

  private ContainerType testContainerType;

  private ContainerType createTestContainerType() {
    testContainerType = new ContainerType();
    testContainerType.setName(TEST_CONTAINERTYPE_NAME);
    testContainerType.setNumberOfColumns(TEST_CONTAINERTYPE_COLS);
    testContainerType.setNumberOfRows(TEST_CONTAINERTYPE_ROWS);
    
    persist(testContainerType);
    return testContainerType;
  }

  @BeforeEach
  public void setup() {
    createTestContainerType();
  }

  @Test
  public void findContainerType_whenExists_containerTypeReturned() {
    ContainerTypeDto dto = containerTypeRepository.findOne(
        testContainerType.getUuid(),
        new QuerySpec(ContainerTypeDto.class)
    );
    
    assertNotNull(dto);
    assertEquals(TEST_CONTAINERTYPE_NAME, testContainerType.getName());
  }
  
  @Test
  public void createContainerType_onSuccess_containerTypePersisted() {
    String newContainerTypeName = "new container type";

    ContainerTypeDto newDto = new ContainerTypeDto();
    newDto.setName(newContainerTypeName);
    newDto.setNumberOfColumns(TEST_CONTAINERTYPE_COLS);
    newDto.setNumberOfRows(TEST_CONTAINERTYPE_ROWS);
    
    ContainerTypeDto created = containerTypeRepository.create(newDto);
    
    assertNotNull(created.getUuid());
    assertEquals(newContainerTypeName, created.getName());
    assertEquals(TEST_CONTAINERTYPE_COLS, created.getNumberOfColumns());
    assertEquals(TEST_CONTAINERTYPE_ROWS, created.getNumberOfRows());
  }
  
  @Test
  public void updateContainerType_onSucess_containerTypeUpdated() {
    ContainerTypeDto dto = containerTypeRepository.findOne(
        testContainerType.getUuid(),
        new QuerySpec(ContainerTypeDto.class)
    );
    
    dto.setNumberOfRows(10);
    
    containerTypeRepository.save(dto);
    
    assertEquals((Integer) 10, testContainerType.getNumberOfRows());
  }
  
  @Test
  public void deleteContainerType_onSuccess_containerTypeDeleted() {
    containerTypeRepository.delete(testContainerType.getUuid());
    assertNull(entityManager.find(ContainerType.class, testContainerType.getId()));
  }
}
