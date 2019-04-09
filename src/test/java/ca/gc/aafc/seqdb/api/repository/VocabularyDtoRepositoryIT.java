package ca.gc.aafc.seqdb.api.repository;

import javax.inject.Inject;

import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.vocabularies.BaseVocabularyDto;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchPlateSize;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import ca.gc.aafc.seqdb.entities.PcrPrimer.PrimerType;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.resource.list.ResourceList;

public class VocabularyDtoRepositoryIT extends BaseRepositoryTest{
  
  //list of current enums we expect to be returned, add more as we expose more entities.
  private BaseVocabularyDto primerType = new BaseVocabularyDto(PrimerType.class.getSimpleName(), PrimerType.class.getEnumConstants());
  private BaseVocabularyDto pcrBatchType = new BaseVocabularyDto(PcrBatchType.class.getSimpleName(), PcrBatchType.class.getEnumConstants());
  private BaseVocabularyDto pcrBatchPlateSize = new BaseVocabularyDto(PcrBatchPlateSize.class.getSimpleName(), PcrBatchPlateSize.class.getEnumConstants());
  
  @Inject
  private VocabularyReadOnlyRepository readOnlyRepo;
  
  @Test
  public void findAll_DefaultQuerySpec_AllDtosReturned() {
    ResourceList<BaseVocabularyDto> base = readOnlyRepo.findAll(new QuerySpec(BaseVocabularyDto.class));

    assertTrue(base.contains(primerType));
    assertTrue(base.contains(pcrBatchType));
    assertTrue(base.contains(pcrBatchPlateSize));
  }
  
  @Test
  public void findOne_QueryPcrPrimerType_OnePrimerTypeDtoReturned() {
    BaseVocabularyDto base = readOnlyRepo.findOne(PrimerType.class.getSimpleName(), new QuerySpec(BaseVocabularyDto.class));
    
    assertTrue(base.equals(primerType));
  }
  
  @Test(expected = ResourceNotFoundException.class)
  public void findOne_QueryNonExistantID_ThrowResourceNotFoundException() {
    BaseVocabularyDto base = readOnlyRepo.findOne("mumbo jumbo", new QuerySpec(BaseVocabularyDto.class));
    
    assertNull(base);
  }
  
  
}
