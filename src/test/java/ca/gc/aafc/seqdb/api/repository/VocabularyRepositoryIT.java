package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import ca.gc.aafc.seqdb.api.dto.VocabularyDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import javax.inject.Inject;

public class VocabularyRepositoryIT extends BaseRepositoryTest {

  @Inject
  private VocabularyRepository readOnlyRepo;
  
  @Test
  public void findAll_DefaultQuerySpec_AllDtosReturned() {
    List<VocabularyDto> resultList = readOnlyRepo.findAll("");
    assertEquals(3, resultList.size());
  }
  
  @Test
  public void findOne_QueryPcrPrimerType_OnePrimerTypeDtoReturned() {
    VocabularyDto resultDto = readOnlyRepo.findOne("pcrBatchType");
    assertEquals("pcrBatchType", resultDto.getId());
  }
  
  @Test
  public void findOne_QueryNonExistantID_returnNotFound() {
    assertEquals(HttpStatus.NOT_FOUND, readOnlyRepo.handleFindOne("mumbo jumbo").getStatusCode());
  }

}
