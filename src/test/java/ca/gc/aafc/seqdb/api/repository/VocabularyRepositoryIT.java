package ca.gc.aafc.seqdb.api.repository;

import javax.inject.Inject;

import ca.gc.aafc.seqdb.api.dto.VocabularyDto;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.entities.PcrPrimer.PrimerType;
import io.crnk.core.exception.MethodNotAllowedException;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.resource.list.ResourceList;

import static org.junit.jupiter.api.Assertions.*;

public class VocabularyRepositoryIT extends BaseRepositoryTest {

  @Inject
  private VocabularyRepository readOnlyRepo;
  
  @Test
  public void findAll_DefaultQuerySpec_AllDtosReturned() {
    ResourceList<VocabularyDto> resultList = readOnlyRepo.findAll(new QuerySpec(VocabularyDto.class));
    assertEquals(1, resultList.size());
  }
  
  @Test
  public void findOne_QueryPcrPrimerType_OnePrimerTypeDtoReturned() {
    VocabularyDto resultDto = readOnlyRepo.findOne("pcrType", new QuerySpec(VocabularyDto.class));
    assertEquals("pcrType", resultDto.getId());
  }
  
  @Test
  public void findOne_QueryNonExistantID_ThrowResourceNotFoundException() {
    assertThrows(ResourceNotFoundException.class,
        () -> readOnlyRepo.findOne("mumbo jumbo", new QuerySpec(VocabularyDto.class)));
  }

  @Test
  public void delete_ExistingDtoID_ThrowUnsupportedOperationException() {
    assertThrows(MethodNotAllowedException.class, () -> readOnlyRepo.delete(PrimerType.class.getSimpleName()));
  }

}
