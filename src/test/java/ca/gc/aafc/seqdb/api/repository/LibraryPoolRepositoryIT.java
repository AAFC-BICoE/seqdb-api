package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.Serializable;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.testsupport.factories.LibraryPoolFactory;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class LibraryPoolRepositoryIT extends BaseRepositoryTest {

  private LibraryPool testPool;
  
  @Inject
  private ResourceRepository<LibraryPoolDto, Serializable> poolRepository;
  
  private LibraryPool createTestPool() {
    testPool = LibraryPoolFactory.newLibraryPool().name("test pool").build();
    persist(testPool);
    return testPool;
  }
  
  @BeforeEach
  public void setup() {
    createTestPool();
  }
  
  @Test
  public void findPool_whenPoolExists_poolReturned() {
    LibraryPoolDto dto = poolRepository.findOne(
        testPool.getId(),
        new QuerySpec(LibraryPoolDto.class)
    );
    
    assertNotNull(dto);
    assertEquals("test pool", dto.getName());
  }
  
  @Test
  public void createPool_onSuccess_poolCreated() {
    LibraryPoolDto newDto = new LibraryPoolDto();
    newDto.setName("new pool");
    
    LibraryPoolDto created = poolRepository.create(newDto);
    assertNotNull(created.getLibraryPoolId());
    assertEquals("new pool", created.getName());
  }
  
  @Test
  public void updatePool_onSuccess_poolUpdated() {
    LibraryPoolDto dto = poolRepository.findOne(
        testPool.getId(),
        new QuerySpec(LibraryPoolDto.class)
    );
    
    dto.setName("updated name");
    
    LibraryPoolDto updated = poolRepository.save(dto);
    
    assertEquals("updated name", updated.getName());
    assertEquals("updated name", testPool.getName());
  }
  
  @Test
  public void deletePool_onSuccess_poolDeleted() {
    poolRepository.delete(testPool.getId());
    assertNull(entityManager.find(LibraryPool.class, testPool.getId()));
  }
  
}
