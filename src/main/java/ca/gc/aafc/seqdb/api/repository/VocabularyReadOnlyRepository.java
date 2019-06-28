package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import ca.gc.aafc.seqdb.api.dto.vocabularies.BaseVocabularyDto;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrProfile;
import ca.gc.aafc.seqdb.entities.PcrReaction;
import ca.gc.aafc.seqdb.entities.Product;
import ca.gc.aafc.seqdb.entities.Protocol;
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
   * from a more generalized source in the future ie Reading the Seqdb.Jar file.
   */
  private static final Set<Class<?>> EXPOSED_ENTITY_CLASSES = new HashSet<>(
      Arrays.asList(
          Region.class, 
          PcrPrimer.class, 
          PcrBatch.class, 
          PcrReaction.class, 
          Group.class,
          Product.class,
          PcrProfile.class,
          Protocol.class
         ));


  private static final Map<String, BaseVocabularyDto> ENUM_MAP = initVocabulariesMap();

  /**
   * Scans the exposed entity classes and then filters them for enum classes
   * 
   * @param exposedClasses
   *          - the set
   * @return a hashmap of the enums in the exposed
   */
  private static Map<String, BaseVocabularyDto> initVocabulariesMap() {
    return EXPOSED_ENTITY_CLASSES.stream()
        .flatMap(c -> Arrays.stream(c.getClasses()))
        .filter(x -> x.isEnum())
        .map(y -> new BaseVocabularyDto(y.getSimpleName(), y.getEnumConstants()))
        .collect(Collectors.toMap(BaseVocabularyDto::getEnumType, Function.identity()));
  }

  public VocabularyReadOnlyRepository() {
    super(BaseVocabularyDto.class);
  }

  @Override
  public ResourceList<BaseVocabularyDto> findAll(QuerySpec querySpec) {
    List<BaseVocabularyDto> resultList = new ArrayList<>();
    for (String key : ENUM_MAP.keySet()) {
      resultList.add(ENUM_MAP.get(key));
    }
    return new DefaultResourceList<BaseVocabularyDto>(resultList, null, null);
  }

  @Override
  public BaseVocabularyDto findOne(Serializable id, QuerySpec querySpec) {
    BaseVocabularyDto result = ENUM_MAP.get(id);
    if (result != null) {
      return result;
    } else {
      throw new ResourceNotFoundException("resource not found: " + id);
    }
  }

}
