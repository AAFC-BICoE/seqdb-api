package ca.gc.aafc.seqdb.api;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import io.crnk.core.engine.internal.utils.PropertyUtils;

/**
 * An in memory alternative for the Entity Manager. Emulates some of the behavior such as persisting
 * and deleting to use an ArrayList instead. Also adds a bunch of utility classes to be used when testing
 * it out to be able to test what is in the database.
 * 
 * This class will try to determine what the id property is by using the @id annotation, but if it cannot be
 * found you can register the property using the registerKeyProperty method.
 * 
 * This class is partially implemented and is to be used for testing purposes only. Many of the methods are
 * not implemented and will throw exceptions if they are used. 
 */
public class MapBackedEntityManager implements EntityManager {

  /**
   * For each entity class type, a list of objects is stored. For example,
   * 
   * Accounts.class -> List(Account1, Account2)
   * PcrBatch.class -> List(PcrBatch1, PcrBatch2, PcrBatch3)
   * 
   * For each entity class type, a new list will be created to store the entities.
   */
  private final Map<Class<?>, List<Object>> entities = new HashMap<Class<?>, List<Object>>();
  
  /**
   * For some of the tests, the ability to retrieve entities by ID is needed. If you
   * register the key function for a specific entity class type, we can then retrieve
   * the id using the supplied function. In the find method we can then use the function
   * to get the id from the entity.
   */
  private final Map<Class<?>, String> entityIdProperty = new HashMap<>();
  
  /**
   * Sequentially generated ID to provide for the entity.
   */
  private final AtomicInteger idGenerator = new AtomicInteger(0);
  
  /**
   * Looks to see if the entity has been already registered. If not it will attempt to
   * automatically register it using the {@link @Id} annotation. 
   * 
   * How it works: - check to see if it's already registered, if it is just return the property name. -
   * get all the {@link Method} with a {@link @Id} annotation form the provided class -
   * ensure there is only 1 method - extract the property name by removing the "get" part of the
   * method and lowercase first letter
   * 
   * @param clazz
   *          the class of the entity
   *          
   * @return the property name which was either registered manually or automatically using the {@link @Id}
   *           annotation.
   * 
   * @throws IllegalStateException
   *           thrown if there is zero or more than one annotation found, and if the annotation is not
   *           on a getter method. 
   */
  private String locateAndGetEntityIdProperty(Class<?> clazz)
      throws IllegalStateException {

    // Check if it's already registered.
    if (entityIdProperty.containsKey(clazz)) {
      return entityIdProperty.get(clazz);
    }
    
    List<Method> methodAnnotatedWithId = MethodUtils.getMethodsListWithAnnotation(clazz, Id.class);

    // Cannot handle more than one id annotation.
    if (methodAnnotatedWithId.size() > 1) {
      throw new IllegalStateException("can't handle multiple Id annotation on the same class");
    }
    
    // Check if the annotation exists on a getter method.
    if (methodAnnotatedWithId.isEmpty() 
        || methodAnnotatedWithId.get(0).getName() == null
        || !StringUtils.startsWith(methodAnnotatedWithId.get(0).getName(), "get")) {
      
      throw new IllegalStateException("Cannot retrieve the key property for the entity: " + clazz.getSimpleName() 
            + ". It could not be automatically added since no id annotations were found in the entities class."
            + " You can add the id key property field manually using the registerKeyProperty method.");
    
    }
    
    // property name always starts with a lowercase
    String property = StringUtils.uncapitalize(StringUtils.removeStart(methodAnnotatedWithId.get(0).getName(), "get"));

    // Add it to the entityIdProperty list so it does not have to search again for it.
    entityIdProperty.put(clazz, property);
    
    // Return the property name to the user.
    return property;
  }
  
  /**
   * Based on a specific entity class, retrieve a list of all the stored entities.
   * 
   * @param entityClass the class to retrieve the list from.
   * @return list of all the entities for a given class. If nothing of that class is stored, null is returned.
   */
  public List<Object> getPersistedEntities(Class<?> entityClass) {
    // Make sure the class list exists.
    if (!entities.containsKey(entityClass)) {
      return null;
    }
    
    return entities.get(entityClass);
  }
  
  /** 
   * Register a new key function for a specific entity class if it cannot be detected automatically. 
   * This allows the find method to be able to retrieve the id since the getId method for each entity 
   * is different.
   * 
   * For example, Account.class id is mapped to getAccountId(), while PcrBatch.class is mapped to 
   * getPcrBatchId().
   * 
   * This method does not need to be used if you have an @id annotation on the entities getter for the 
   * id property. If the id annotation is found, it will do it automatically.
   * 
   * Not thread safe.
   * 
   * @param entityClass the class to register the key method.
   * @param keyFunction
   */
  public void registerKeyProperty(Class<?> entityClass, String keyProperties) {
 
    entityIdProperty.put(entityClass, keyProperties);
    
  }
  
  /**
   * Most of the methods in the mocked entity manager are not supported, this is just a helper
   * message so it's not duplicating the same thing multiple times.
   * 
   * @param methodName The unsupported method name.
   * @return Helpful unsupported message.
   */
  private String unsupportedMessage(String methodName) {
    return "The " + methodName + " method is not supported in the " + this.getClass().getSimpleName();
  }

  /**
   * Check if the entity map contains a list of it's class. If not, create the list and add an entity
   * to it's list. If it already exists, it will just add it to it's respective entity list.
   */
  @Override
  public void persist(Object entity) {
    Class<?> entityClass = entity.getClass();

    // Check if the entity class has been inserted before in the entities map.
    if (!entities.containsKey(entityClass)) {
      // New entity class being inserted. Create a list to place it in.
      entities.put(entityClass, new ArrayList<Object>());
    }
    
    // Check if an ID property function has been registered to the entity.
    String keyProperty = locateAndGetEntityIdProperty(entityClass);

    if (keyProperty != null) {
      // If the ID is null for the entity, generate one.
      Integer key = (Integer) PropertyUtils.getProperty(entity, keyProperty);
      if (key == null) {
        PropertyUtils.setProperty(entity, keyProperty, idGenerator.incrementAndGet());
      }
    }

    // Add the entity to it's respective list.
    entities.get(entityClass).add(entity);

  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public <T> T merge(T entity) {
    throw new UnsupportedOperationException(unsupportedMessage("merge"));
  }

  /**
   * Delete an entity from the in memory entity manager. Will search the array list for an equal object
   * then remove it from the specific entity class list.
   */
  @Override
  public void remove(Object entity) {
    Class<?> entityClass = entity.getClass();
    
    if (!entities.containsKey(entityClass)) {
      // Entity class was never stored in the entities map.
      return;
    }
    
    entities.get(entityClass).remove(entity);
  }

  /**
   * Find entity based on the provided primaryKey.
   * 
   * @param entityClass The entity type being used to retrieve the ID from.
   * @param primaryKey
   * @throws IllegalStateException if the method cannot locate the entity id property.
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey) {
    
    // Check if there is no entity class list. If not then it's definitely not in the entity manager.
    if (!entities.containsKey(entityClass)) {
      return null;
    }
    
    // First check if the entity class has registered a key function.
    locateAndGetEntityIdProperty(entityClass);
    
    for (Object entity : entities.get(entityClass)) {
      Integer key = (Integer)PropertyUtils.getProperty(entity, entityIdProperty.get(entityClass));
      
      // Check if the ID provided matches the primary key.
      if(primaryKey.equals(key)){
        return (T) entity;
      }
    }
    
    // Nothing was found.
    return null;
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
    throw new UnsupportedOperationException(unsupportedMessage("find (entityClass, primaryKey, properties)"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
    throw new UnsupportedOperationException(unsupportedMessage("find (entityClass, primaryKey, lockMode)"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode,
      Map<String, Object> properties) {
    throw new UnsupportedOperationException(unsupportedMessage("find (entityClass, primaryKey, lockMode, properties)"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public <T> T getReference(Class<T> entityClass, Object primaryKey) {
    throw new UnsupportedOperationException(unsupportedMessage("getReference"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public void flush() {
    throw new UnsupportedOperationException(unsupportedMessage("flush"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public void setFlushMode(FlushModeType flushMode) {
    throw new UnsupportedOperationException(unsupportedMessage("getFlushMode"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public FlushModeType getFlushMode() {
    throw new UnsupportedOperationException(unsupportedMessage("getFlushMode"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public void lock(Object entity, LockModeType lockMode) {
    throw new UnsupportedOperationException(unsupportedMessage("lock"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    throw new UnsupportedOperationException(unsupportedMessage("lock"));
  }

  /**
   * Refreshes the instances of the database. Since we using an in memory entity manager, no action is required here.
   */
  @Override
  public void refresh(Object entity) {
    // Don't do anything. No operation.
  }

  /**
   * Refreshes the instances of the database. Since we using an in memory entity manager, no action is required here.
   */
  @Override
  public void refresh(Object entity, Map<String, Object> properties) {
    // Don't do anything. No operation.
  }

  /**
   * Refreshes the instances of the database. Since we using an in memory entity manager, no action is required here.
   */
  @Override
  public void refresh(Object entity, LockModeType lockMode) {
    // Don't do anything. No operation.
  }

  /**
   * Refreshes the instances of the database. Since we using an in memory entity manager, no action is required here.
   */
  @Override
  public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    // Don't do anything. No operation.
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public void clear() {
    throw new UnsupportedOperationException(unsupportedMessage("clear"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public void detach(Object entity) {
    throw new UnsupportedOperationException(unsupportedMessage("detach"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public boolean contains(Object entity) {
    throw new UnsupportedOperationException(unsupportedMessage("contains"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public LockModeType getLockMode(Object entity) {
    throw new UnsupportedOperationException(unsupportedMessage("getLockMode"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public void setProperty(String propertyName, Object value) {
    throw new UnsupportedOperationException(unsupportedMessage("setProperty"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public Map<String, Object> getProperties() {
    throw new UnsupportedOperationException(unsupportedMessage("getProperties"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public Query createQuery(String qlString) {
    throw new UnsupportedOperationException(unsupportedMessage("createQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
    throw new UnsupportedOperationException(unsupportedMessage("createQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public Query createQuery(CriteriaUpdate updateQuery) {
    throw new UnsupportedOperationException(unsupportedMessage("createQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public Query createQuery(CriteriaDelete deleteQuery) {
    throw new UnsupportedOperationException(unsupportedMessage("createQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
    throw new UnsupportedOperationException(unsupportedMessage("createQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public Query createNamedQuery(String name) {
    throw new UnsupportedOperationException(unsupportedMessage("createNativeQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
    throw new UnsupportedOperationException(unsupportedMessage("createNativeQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public Query createNativeQuery(String sqlString) {
    throw new UnsupportedOperationException(unsupportedMessage("createNativeQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public Query createNativeQuery(String sqlString, Class resultClass) {
    throw new UnsupportedOperationException(unsupportedMessage("createNativeQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public Query createNativeQuery(String sqlString, String resultSetMapping) {
    throw new UnsupportedOperationException(unsupportedMessage("createNativeQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
    throw new UnsupportedOperationException(unsupportedMessage("createStoredProcedureQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
    throw new UnsupportedOperationException(unsupportedMessage("createStoredProcedureQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName,
      Class... resultClasses) {
    throw new UnsupportedOperationException(unsupportedMessage("createStoredProcedureQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName,
      String... resultSetMappings) {
    throw new UnsupportedOperationException(unsupportedMessage("createStoredProcedureQuery"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public void joinTransaction() {
    throw new UnsupportedOperationException(unsupportedMessage("joinTransaction"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public boolean isJoinedToTransaction() {
    throw new UnsupportedOperationException(unsupportedMessage("isJoinedToTransaction"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public <T> T unwrap(Class<T> cls) {
    throw new UnsupportedOperationException(unsupportedMessage("unwrap"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public Object getDelegate() {
    throw new UnsupportedOperationException(unsupportedMessage("getDelegate"));
  }

  /**
   * No operation should be performed since this is not using a database connection. Nothing needs
   * to be closed.
   */
  @Override
  public void close() {
    // No operation should be performed.
  }

  /**
   * Will always return true since this entity manager cannot be closed. This is because all of
   * the data is stored in memory.
   */
  @Override
  public boolean isOpen() {
    return true;
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public EntityTransaction getTransaction() {
    throw new UnsupportedOperationException(unsupportedMessage("getTransaction"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public EntityManagerFactory getEntityManagerFactory() {
    throw new UnsupportedOperationException(unsupportedMessage("getEntityManagerFactory"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    throw new UnsupportedOperationException(unsupportedMessage("getCriteriaBuilder"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public Metamodel getMetamodel() {
    throw new UnsupportedOperationException(unsupportedMessage("getMetamodel"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
    throw new UnsupportedOperationException(unsupportedMessage("createEntityGraph"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public EntityGraph<?> createEntityGraph(String graphName) {
    throw new UnsupportedOperationException(unsupportedMessage("createEntityGraph"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public EntityGraph<?> getEntityGraph(String graphName) {
    throw new UnsupportedOperationException(unsupportedMessage("getEntityGraph"));
  }

  /**
   * Unsupported. This method is currently not being supported.
   */
  @Override
  public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
    throw new UnsupportedOperationException(unsupportedMessage("getEntityGraphs"));
  }

}
