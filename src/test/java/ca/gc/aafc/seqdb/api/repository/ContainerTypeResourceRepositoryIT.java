package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.Serializable;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import ca.gc.aafc.seqdb.api.dto.ContainerTypeDto;
import ca.gc.aafc.seqdb.entities.ContainerType;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class ContainerTypeResourceRepositoryIT extends BaseRepositoryTest {

  private static final String TEST_CONTAINERTYPE_NAME = "test ct";
  private static final String TEST_CONTAINERTYPE_BASETYPE = "test basetype";
  private static final Integer TEST_CONTAINERTYPE_COLS = 8;
  private static final Integer TEST_CONTAINERTYPE_ROWS = 12;

  private ContainerType testContainerType;

  @Inject
  private ResourceRepository<ContainerTypeDto, Serializable> ctRepository;

  private ContainerType createTestContainerType() {
    testContainerType = new ContainerType();
    testContainerType.setName(TEST_CONTAINERTYPE_NAME);
    testContainerType.setBaseType(TEST_CONTAINERTYPE_BASETYPE);
    testContainerType.setNumberOfColumns(TEST_CONTAINERTYPE_COLS);
    testContainerType.setNumberOfRows(TEST_CONTAINERTYPE_ROWS);
    testContainerType.setNumberOfWells(TEST_CONTAINERTYPE_COLS * TEST_CONTAINERTYPE_ROWS);
    
    persist(testContainerType);
    return testContainerType;
  }

  @BeforeEach
  public void setup() {
    createTestContainerType();
  }

  @Test
  public void findContainerType_whenExists_containerTypeReturned() {
    ContainerTypeDto dto = ctRepository.findOne(
        testContainerType.getId(),
        new QuerySpec(ContainerTypeDto.class)
    );
    
    assertNotNull(dto);
    assertEquals(TEST_CONTAINERTYPE_NAME, testContainerType.getName());
  }
  
  @Test
  public void createContainerType_onSuccess_containerTypePersisted() {
    ContainerTypeDto newDto = new ContainerTypeDto();
    newDto.setName(TEST_CONTAINERTYPE_NAME);
    newDto.setBaseType(TEST_CONTAINERTYPE_BASETYPE);
    newDto.setNumberOfColumns(TEST_CONTAINERTYPE_COLS);
    newDto.setNumberOfRows(TEST_CONTAINERTYPE_ROWS);
    
    ContainerTypeDto created = ctRepository.create(newDto);
    
    assertNotNull(created.getContainerTypeId());
    assertEquals(TEST_CONTAINERTYPE_NAME, created.getName());
    assertEquals(TEST_CONTAINERTYPE_BASETYPE, created.getBaseType());
    assertEquals(TEST_CONTAINERTYPE_COLS, created.getNumberOfColumns());
    assertEquals(TEST_CONTAINERTYPE_ROWS, created.getNumberOfRows());
    
    // The number of wells should be automatically set.
    assertEquals(
        (Integer) (TEST_CONTAINERTYPE_COLS * TEST_CONTAINERTYPE_ROWS),
        created.getNumberOfWells()
    );
  }
  
  @Test
  public void updateContainerType_onSucess_containerTypeUpdated() {
    ContainerTypeDto dto = ctRepository.findOne(
        testContainerType.getId(),
        new QuerySpec(ContainerTypeDto.class)
    );
    
    dto.setNumberOfRows(10);
    
    ctRepository.save(dto);
    
    assertEquals((Integer) 10, testContainerType.getNumberOfRows());
  }
  
  @Test
  public void deleteContainerType_onSuccess_containerTypeDeleted() {
    ctRepository.delete(testContainerType.getId());
    assertNull(entityManager.find(ContainerType.class, testContainerType.getId()));
  }
}
