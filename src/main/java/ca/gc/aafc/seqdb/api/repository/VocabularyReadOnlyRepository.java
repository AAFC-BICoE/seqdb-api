package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ca.gc.aafc.seqdb.api.dto.vocabularies.BaseVocabularyDto;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrReaction;
import ca.gc.aafc.seqdb.entities.Product;
import ca.gc.aafc.seqdb.entities.Region;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ReadOnlyResourceRepositoryBase;
import io.crnk.core.resource.list.DefaultResourceList;
import io.crnk.core.resource.list.ResourceList;

public class VocabularyReadOnlyRepository
    extends ReadOnlyResourceRepositoryBase<BaseVocabularyDto, Serializable> {


  private Map<String, BaseVocabularyDto> enumMap;

  /**
   * List of enittyClasses that are exposed as DTOs in the Api.
   * This information could be obtained from a more generalized source in the future.
   */
  public static Set<Class<?>> exposedEntityClasses = new HashSet<Class<?>>() {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    {
      add(Region.class);
      add(PcrPrimer.class);
      add(PcrBatch.class);
      add(PcrReaction.class);
      add(Group.class);
      add(Product.class);
    }
  };
  
  /**
   * Scans the exposed entity classes and then filters them for enum classes
   * 
   * @param exposedClasses
   * @return a set of enum classes from
   */
  private static Set<Class<?>> getEnumClasses(Set<Class<?>> exposedClasses) {

    Set<Class<?>> resultList = new HashSet<Class<?>>();

    for (Class<?> entityClasses : exposedEntityClasses) {
      for (Class<?> possibleEnumClasses : entityClasses.getClasses()) {

        if (possibleEnumClasses.isEnum()) {
          resultList.add(possibleEnumClasses);
        }
      }
    }
    return resultList;
  }
  /**
   * Unpacks the set of enum classes into a map.
   * @return a map with the key being the enum type name and the value being the Dto with enum value.
   */
  private static Map<String, BaseVocabularyDto> getVocabulariesMap(Set<Class<?>> enumClasses) {
    Map<String, BaseVocabularyDto> resultList = new HashMap<String, BaseVocabularyDto>();
    BaseVocabularyDto vocabularyDto;

    if (enumClasses != null) {
      
      for (Class<?> clazz : enumClasses) {
        if(clazz.isEnum()) {
          String entryName = clazz.getSimpleName();
          Object[] enumConstantsArray = clazz.getEnumConstants();

          vocabularyDto = new BaseVocabularyDto(entryName, enumConstantsArray);

          resultList.put(entryName, vocabularyDto);
        }

      }
    }

    return resultList;
  }

  public VocabularyReadOnlyRepository() {
    super(BaseVocabularyDto.class);
    this.enumMap = getVocabulariesMap(getEnumClasses(exposedEntityClasses));

  }

  @Override
  public ResourceList<BaseVocabularyDto> findAll(QuerySpec querySpec) {
    ArrayList<BaseVocabularyDto> resultList = new ArrayList<BaseVocabularyDto>();
    for (String key : enumMap.keySet()) {

      resultList.add(enumMap.get(key));
    }

    return new DefaultResourceList<BaseVocabularyDto>(resultList, null, null);
  }

  @Override
  public BaseVocabularyDto findOne(Serializable id, QuerySpec querySpec) {

    BaseVocabularyDto result = enumMap.get(id);
    if (result != null) {
      return result;
    } else {
      throw new ResourceNotFoundException("resource not found: " + id);
    }
  }

}
