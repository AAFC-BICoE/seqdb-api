package ca.gc.aafc.seqdb.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

import org.junit.Test;

import ca.gc.aafc.seqdb.entities.Product;
import ca.gc.aafc.seqdb.factories.ProductFactory;

public class MapBackedEntityManagerTest {
  
  @Test
  public void persist_onPersist_assignId() {
    EntityManager entityManager = new MapBackedEntityManager();
    
    Product testProduct = ProductFactory.newProduct().build();
    assertNull(testProduct.getId());
    entityManager.persist(testProduct);
    assertNotNull(testProduct.getId());
  }
  
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

}
