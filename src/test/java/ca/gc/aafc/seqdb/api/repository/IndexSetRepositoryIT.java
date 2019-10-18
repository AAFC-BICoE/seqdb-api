package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.Serializable;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.IndexSetDto;
import ca.gc.aafc.seqdb.entities.libraryprep.IndexSet;
import ca.gc.aafc.seqdb.testsupport.factories.IndexSetFactory;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class IndexSetRepositoryIT extends BaseRepositoryTest {
  
  private IndexSet testIndexSet;
  
  @Inject
  private ResourceRepository<IndexSetDto, Serializable> indexSetRepository;

  
  private void createTestIndexSet() {
    testIndexSet = IndexSetFactory.newIndexSet().build();
    testIndexSet.setName("test index set");
    persist(testIndexSet);
  }
  
  @BeforeEach
  public void setup() {
    createTestIndexSet();
  }
  
  @Test
  public void findIndexSet_whenIndexSetEists_indexSetReturned() {
    IndexSetDto dto = indexSetRepository.findOne(
        testIndexSet.getId(),
        new QuerySpec(IndexSetDto.class)
    );
    
    assertNotNull(dto);
    assertEquals("test index set", testIndexSet.getName());
  }
  
  @Test
  public void createIndexSet_onSuccess_indexSetCreated() {
    IndexSetDto newDto = new IndexSetDto();
    newDto.setName("new index set");
    
    IndexSetDto created = indexSetRepository.create(newDto);
    assertNotNull(created.getIndexSetId());
    assertEquals("new index set", created.getName());
  }
  
  @Test
  public void updateIndexSet_onSuccess_indexSetUpdated() {
    IndexSetDto dto = indexSetRepository.findOne(
        testIndexSet.getId(),
        new QuerySpec(IndexSetDto.class)
    );
    
    dto.setName("updated name");
    indexSetRepository.save(dto);
    assertEquals("updated name", testIndexSet.getName());
  }
  
  @Test
  public void deleteIndexSet_onSuccess_indexSetDeleted() {
    indexSetRepository.delete(testIndexSet.getId());
    assertNull(entityManager.find(IndexSet.class, testIndexSet.getId()));
  }
  
}
