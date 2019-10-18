package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.Serializable;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.IndexSetDto;
import ca.gc.aafc.seqdb.api.dto.NgsIndexDto;
import ca.gc.aafc.seqdb.entities.libraryprep.NgsIndex;
import ca.gc.aafc.seqdb.testsupport.factories.NgsIndexFactory;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class NgsIndexRepositoryIT extends BaseRepositoryTest {

  private NgsIndex testNgsIndex;

  @Inject
  private ResourceRepository<NgsIndexDto, Serializable> ngsIndexRepository;
  @Inject
  private ResourceRepository<IndexSetDto, Serializable> indexSetRepository;

  private void createTestIndex() {
    testNgsIndex = NgsIndexFactory.newNgsIndex().build();
    testNgsIndex.setName("test index");
    persist(testNgsIndex.getIndexSet());
    persist(testNgsIndex);
    
    // This is needed in the tests to initialize the PersistentBag for the index set's "libraryPreps"
    // field for the test. This doesn't matter in prod.
    entityManager.flush();
    entityManager.refresh(testNgsIndex.getIndexSet());
  }

  @BeforeEach
  public void setup() {
    createTestIndex();
  }
  
  @Test
  public void findNgsIndex_whenNgsIndexExists_ngsIndexReturned() {
    NgsIndexDto dto = ngsIndexRepository.findOne(
        testNgsIndex.getId(),
        new QuerySpec(NgsIndexDto.class)
    );
    
    assertNotNull(dto);
    assertEquals("test index", testNgsIndex.getName());
  }
  
  @Test
  public void createNgsIndex_onSuccess_ngsIndexCreated() {
    NgsIndexDto newDto = new NgsIndexDto();
    newDto.setName("new index set");
    newDto.setIndexSet(
        indexSetRepository.findOne(
            testNgsIndex.getIndexSet().getId(),
            new QuerySpec(IndexSetDto.class)
        )
    );
    
    NgsIndexDto created = ngsIndexRepository.create(newDto);
    assertNotNull(created.getNgsIndexId());
    assertNotNull(created.getIndexSet().getIndexSetId());
    assertEquals("new index set", created.getName());
  }
  
  @Test
  public void updateNgsIndex_onSuccess_ngsIndexUpdated() {
    NgsIndexDto dto = ngsIndexRepository.findOne(
        testNgsIndex.getId(),
        new QuerySpec(NgsIndexDto.class)
    );
    
    dto.setName("updated name");
    ngsIndexRepository.save(dto);
    assertEquals("updated name", testNgsIndex.getName());
  }
  
  @Test
  public void deleteNgsIndex_onSuccess_ngsIndexDeleted() {
    ngsIndexRepository.delete(testNgsIndex.getId());
    assertNull(entityManager.find(NgsIndex.class, testNgsIndex.getId()));
  }

}
