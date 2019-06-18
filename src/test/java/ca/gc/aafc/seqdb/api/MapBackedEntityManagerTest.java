package ca.gc.aafc.seqdb.api;

import java.util.List;
import javax.persistence.EntityManager;

import org.junit.Test;

import ca.gc.aafc.seqdb.entities.Product;
import ca.gc.aafc.seqdb.factories.PcrBatchFactory;
import ca.gc.aafc.seqdb.factories.ProductFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MapBackedEntityManagerTest {
  
  /**
   * Used to test entity with a getter not matching the property's name.
   *
   */
  public static class TestEntity {
    private int id;
    
    public TestEntity(int id) {
      this.id = id;
    }
    
    public int getIdentifier() {
      return id;
    }
  }
  
  /**
   * Test will check if when an entity is persisted an id is assigned to it.
   */
  @Test
  public void persist_onPersist_assignId() {
    EntityManager entityManager = new MapBackedEntityManager();
    
    Product testProduct = ProductFactory.newProduct().build();
    assertNull(testProduct.getId());
    entityManager.persist(testProduct);
    assertNotNull(testProduct.getId());
  }
  
  /**
   * Test will attempt to retrieve the entity by the id assigned to it.
   */
  @Test
  public void persist_onFindOne_entityFound() {
    EntityManager entityManager = new MapBackedEntityManager();
    
    Product testProduct = ProductFactory.newProduct().name("ABC").build();
    entityManager.persist(testProduct);
    Integer id = testProduct.getId();
    
    Product testProductRetreived = entityManager.find(Product.class, id);
    assertNotNull(testProductRetreived);
    assertEquals("ABC", testProductRetreived.getName());
  }
  
  @Test
  public void persist_registerKeyProperty_entityFound() {
    MapBackedEntityManager entityManager = new MapBackedEntityManager();
    entityManager.registerKeyProperty(TestEntity.class, "identifier");

    TestEntity testEntity = new TestEntity(2);
    entityManager.persist(testEntity);

    TestEntity testEntityRetrieved = entityManager.find(TestEntity.class, 2);
    assertNotNull(testEntityRetrieved);
    assertEquals(2, testEntityRetrieved.getIdentifier());
  }
  
  /**
   * Check if an exception is thrown when no id annotation can be found and it's not
   * registered manually.
   */
  @Test(expected = IllegalStateException.class)
  public void persist_noRegisteredId_exceptionExpected() {
    EntityManager entityManager = new MapBackedEntityManager();
    entityManager.persist("Test");
  }
  
  /**
   * Test will first check to make sure no entities are returned when the entity manager is first
   * created, then it will persist 10 product entities and try to retrieve all of them.
   * 
   * It will also create a PcrBatch which will also be stored, but it should not return it since it's
   * not a product.
   */
  @Test
  public void getPersistedEntities_retrieveList_forSpecificEntity() {
    MapBackedEntityManager entityManager = new MapBackedEntityManager();
    
    assertNull(entityManager.getPersistedEntities(Product.class));
    entityManager.persist(PcrBatchFactory.newPcrBatch().build());
    
    List<Product> testProducts = ProductFactory.newListOf(10);
    for (Product testProduct : testProducts) {
      entityManager.persist(testProduct);
    }
    
    assertEquals(10, entityManager.getPersistedEntities(Product.class).size());
  }
  
  /**
   * Test will create a product, make sure it exists in the entity manager then attempt to delete
   * it. It should return null since it now has been deleted.
   */
  @Test
  public void remove_persistedEntity_Successful() {
    EntityManager entityManager = new MapBackedEntityManager();
    
    Product testProduct = ProductFactory.newProduct().name("ABC").build();
    entityManager.persist(testProduct);
    
    Integer id = testProduct.getId();
    assertNotNull(entityManager.find(Product.class, id));
    
    entityManager.remove(testProduct);
    assertNull(entityManager.find(Product.class, id));
  }

  /**
   * This test will try to find an entity which has not been registered yet. It should
   * automatically find the id property and return null. No exceptions should be thrown
   * here.
   */
  @Test
  public void find_nonRegisteredManually_noResults() {
    EntityManager entityManager = new MapBackedEntityManager();
    assertNull(entityManager.find(Product.class, 13));
  }
  
  /**
   * Test an unsupported method to make sure the exception is thrown.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void unsupportedMethod_throwException_exceptionExpected() {
    EntityManager entityManager = new MapBackedEntityManager();
    
    // Attempt to use an unsupported method.
    entityManager.createNamedQuery("test");
  }
}
