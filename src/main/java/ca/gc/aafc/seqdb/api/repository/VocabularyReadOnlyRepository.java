package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Functions;

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

  /**
   * List of entityClasses that are exposed as DTOs in the Api. This information could be obtained
   * from a more generalized source in the future.
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

  private static Map<String, BaseVocabularyDto> enumMap = getVocabulariesMap(exposedEntityClasses);

  /**
   * Scans the exposed entity classes and then filters them for enum classes
   * 
   * @param exposedClasses - the set 
   * @return a hashmap of the enums in the exposed 
   */
  private static Map<String, BaseVocabularyDto> getVocabulariesMap(Set<Class<?>> exposedClasses) {
    Set<Class<?>> innerClasses = new HashSet<Class<?>>();

    for (Class<?> entityClasses : exposedEntityClasses) {

      innerClasses.addAll(Arrays.asList(entityClasses.getClasses()));

    }
    
    return innerClasses.stream().filter(x -> x.isEnum())
        .map(y -> baseVocabularyDtoMaker(y.getSimpleName(), y.getEnumConstants()))
        .collect(Collectors.toMap(BaseVocabularyDto::getEnumType, Functions.identity()));

  }
  /**
   * Used in stream to construct new BaseVocabularyDto objects.
   * @param name - the Id of the object, usually the enum name.
   * @param content - the enum contents.
   * @return a new baseVocabularyDto.
   */
  private static BaseVocabularyDto baseVocabularyDtoMaker(String name, Object[] content) {
    return new BaseVocabularyDto(name, content);
  }

  public VocabularyReadOnlyRepository() {
    super(BaseVocabularyDto.class);

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
