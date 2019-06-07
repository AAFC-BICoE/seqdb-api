package ca.gc.aafc.seqdb.api.repository;

import javax.inject.Inject;

import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.vocabularies.BaseVocabularyDto;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchPlateSize;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
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
    ResourceList<BaseVocabularyDto> resultList = readOnlyRepo.findAll(new QuerySpec(BaseVocabularyDto.class));

    assertTrue(resultList.contains(primerType));
    assertTrue(resultList.contains(pcrBatchType));
    assertTrue(resultList.contains(pcrBatchPlateSize));
  }
  
  @Test
  public void findOne_QueryPcrPrimerType_OnePrimerTypeDtoReturned() {
    BaseVocabularyDto resultDto = readOnlyRepo.findOne(PrimerType.class.getSimpleName(), new QuerySpec(BaseVocabularyDto.class));
    
    assertTrue(resultDto.equals(primerType));
  }
  
  @Test(expected = ResourceNotFoundException.class)
  public void findOne_QueryNonExistantID_ThrowResourceNotFoundException() {
    BaseVocabularyDto resultDto = readOnlyRepo.findOne("mumbo jumbo", new QuerySpec(BaseVocabularyDto.class));
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void save_ValidDto_ThrowUnsupportedOperationException() {
    Object[] objectArray = {"Winter", "is", "Comin"};
    BaseVocabularyDto newDto = new BaseVocabularyDto("validDto", objectArray);
    readOnlyRepo.save(newDto);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void create_ValidDto_ThrowUnsupportedOperationException() {
    Object[] objectArray = {"Winter", "is", "Comin"};
    BaseVocabularyDto newDto = new BaseVocabularyDto("validDto", objectArray);
    readOnlyRepo.create(newDto);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void delete_ExistingDtoID_ThrowUnsupportedOperationException() {
    readOnlyRepo.delete(PrimerType.class.getSimpleName());
  }
  
  
}
