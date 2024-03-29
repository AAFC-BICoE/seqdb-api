package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.api.testsupport.factories.LibraryPoolFactory;
import io.crnk.core.queryspec.QuerySpec;

public class LibraryPoolRepositoryIT extends BaseRepositoryTest {

  private LibraryPool testPool;

  @Inject
  private LibraryPoolRepository libraryPoolRepository;
  
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
    LibraryPoolDto dto = libraryPoolRepository.findOne(
        testPool.getUuid(),
        new QuerySpec(LibraryPoolDto.class)
    );
    
    assertNotNull(dto);
    assertEquals("test pool", dto.getName());
  }
  
  @Test
  public void createPool_onSuccess_poolCreated() {
    LibraryPoolDto newDto = new LibraryPoolDto();
    newDto.setName("new pool");
    newDto.setGroup("dina");
    
    LibraryPoolDto created = libraryPoolRepository.create(newDto);
    assertNotNull(created.getUuid());
    assertEquals("new pool", created.getName());
  }
  
  @Test
  public void updatePool_onSuccess_poolUpdated() {
    LibraryPoolDto dto = libraryPoolRepository.findOne(
        testPool.getUuid(),
        new QuerySpec(LibraryPoolDto.class)
    );
    
    dto.setName("updated name");
    
    LibraryPoolDto updated = libraryPoolRepository.save(dto);
    
    assertEquals("updated name", updated.getName());
    assertEquals("updated name", testPool.getName());
  }
  
  @Test
  public void deletePool_onSuccess_poolDeleted() {
    libraryPoolRepository.delete(testPool.getUuid());
    assertNull(entityManager.find(LibraryPool.class, testPool.getId()));
  }
  
}
