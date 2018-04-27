package ca.gc.aafc.seqdb.api.repository;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.modelmapper.ModelMapper;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import ca.gc.aafc.seqdb.interfaces.UniqueObj;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.IncludeFieldSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * JsonApi repository that interfaces using DTOs, and uses JPA entities internally.
 *
 * @param <D> the JsonApi DTO class.
 * @param <E> the JPA entity class.
 */
@RequiredArgsConstructor
public class JpaResourceRepository<D, E extends UniqueObj>
    implements ResourceRepositoryV2<D, Serializable> {

  /**
   * The JsonApi resource class.
   */
  @Getter
  private final Class<D> resourceClass;

  /**
   * The JPA entity class.
   */
  @Getter
  private final Class<E> entityClass;

  @PersistenceContext
  private final EntityManager entityManager;

  private final ModelMapper mapper = new ModelMapper();

  @Override
  public D findOne(Serializable id, QuerySpec querySpec) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
    Root<E> root = criteriaQuery.from(entityClass);

    // Filter by entity id attribute.
    criteriaQuery.where(cb.equal(root.get(getEntityIdAttribute()), id));

    List<String> selectedFields = this.getSelectedFields(querySpec, root);
    
    criteriaQuery.multiselect(JpaResourceRepository.getSelections(selectedFields, root));

    Tuple entityResult;
    try {
      entityResult = entityManager.createQuery(criteriaQuery).getSingleResult();
    } catch (NoResultException e) {
      throw new ResourceNotFoundException(e.getMessage());
    }

    DocumentContext result = JsonPath.parse(new HashMap());

    for (int i = 0; i < selectedFields.size(); i++) {
      result.put(
          "$",
          selectedFields.get(i),
          entityResult.get(i)
      );
    }

    return mapper.map(result.json(), resourceClass);
  }

  @Override
  public ResourceList<D> findAll(QuerySpec querySpec) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResourceList<D> findAll(Iterable<Serializable> ids, QuerySpec querySpec) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends D> S save(S resource) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends D> S create(S resource) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(Serializable id) {
    E entity = entityManager.find(this.entityClass, id);
    if (entity == null) {
      throw new ResourceNotFoundException("Resource not found");
    }
    entityManager.remove(entity);
  }

  private String getEntityIdAttribute() {
    return entityManager
        .getMetamodel()
        .entity(entityClass)
        .getId(Serializable.class)
        .getName();
  }

  private List<String> getSelectedFields(QuerySpec querySpec, Root<?> root) {
    List<String> selectedFields = new ArrayList<>();

    // If no fields are specified, include all fields.
    if (querySpec.getIncludedFields().size() == 0) {
      try {
        selectedFields.addAll(
            Stream.of(
                    Introspector.getBeanInfo(querySpec.getResourceClass())
                        .getPropertyDescriptors()
                )
                // Ignore the "class" property.
                .filter(prop -> !prop.getName().equals("class"))
                .map(PropertyDescriptor::getName)
                .collect(Collectors.toList())
        );
      } catch (IntrospectionException e) {
        throw new IllegalStateException(e.getMessage(), e);
      }
    } else {
      for (IncludeFieldSpec includedField : querySpec.getIncludedFields()) {
        selectedFields.add(includedField.getAttributePath().get(0));
      }
    }
    
    // The id field is always selected, even if not explicitly requested by the user.
    String entityIdAttribute = getEntityIdAttribute();
    if (!selectedFields.contains(entityIdAttribute)) {
      selectedFields.add(entityIdAttribute);
    }
    
    return selectedFields;
  }
  
  private static Selection<?>[] getSelections(List<String> selectedFields, Root<?> root) {
    return selectedFields
        .stream()
        .map(root::get)
        .toArray(Selection[]::new);
  }

}
