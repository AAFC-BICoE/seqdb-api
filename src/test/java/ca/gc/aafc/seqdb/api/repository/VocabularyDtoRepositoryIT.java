package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.vocabularies.BaseVocabularyDto;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchPlateSize;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import ca.gc.aafc.seqdb.entities.PcrPrimer.PrimerType;
import io.crnk.core.exception.MethodNotAllowedException;
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
  
  @Test
  public void findOne_QueryNonExistantID_ThrowResourceNotFoundException() {
    assertThrows(ResourceNotFoundException.class,
        () -> readOnlyRepo.findOne("mumbo jumbo", new QuerySpec(BaseVocabularyDto.class)));
  }

  @Test
  public void save_ValidDto_ThrowUnsupportedOperationException() {
    Object[] objectArray = { "Winter", "is", "Comin" };
    BaseVocabularyDto newDto = new BaseVocabularyDto("validDto", objectArray);
    assertThrows(MethodNotAllowedException.class, () -> readOnlyRepo.save(newDto));
  }

  @Test
  public void create_ValidDto_ThrowUnsupportedOperationException() {
    Object[] objectArray = { "Winter", "is", "Comin" };
    BaseVocabularyDto newDto = new BaseVocabularyDto("validDto", objectArray);
    assertThrows(MethodNotAllowedException.class, () -> readOnlyRepo.create(newDto));
  }

  @Test
  public void delete_ExistingDtoID_ThrowUnsupportedOperationException() {
    assertThrows(MethodNotAllowedException.class, () -> readOnlyRepo.delete(PrimerType.class.getSimpleName()));
  }

}
