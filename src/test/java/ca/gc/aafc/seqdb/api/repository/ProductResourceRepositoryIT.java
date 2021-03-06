package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.seqdb.api.dto.ProductDto;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.testsupport.factories.ProductFactory;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

public class ProductResourceRepositoryIT extends BaseRepositoryTest {
  
  protected static final String TEST_PRODUCT_NAME = "prodcut name";
  protected static final String TEST_PRODUCT_TYPE = "product type";
  protected static final String TEST_PRODUCT_DESCRIPTION = "product desc";
  
  protected static final String TEST_PRODUCT_NAME_CREATE = "prodcut name create";
  protected static final String TEST_PRODUCT_TYPE_CREATE = "product type create";
  protected static final String TEST_PRODUCT_DESCRIPTION_CREATE = "product desc create";

  @Inject
  private BaseDAO baseDao;

  @Inject
  private ProductRepository productRepository;
  
  private Product testProduct;
  
  private Product createTestProduct() {
    testProduct = ProductFactory.newProduct()
        .name(TEST_PRODUCT_NAME)
        .description(TEST_PRODUCT_DESCRIPTION)
        .type(TEST_PRODUCT_TYPE)
        .build();
    persist(testProduct);
    return testProduct;
  }
  
  @BeforeEach
  public void setup() { 
    createTestProduct();    
  }
  
  @Test
  public void findProduct_whenNoFieldsAreSelected_productReturnedWithAllFields() {
    ProductDto productDto = productRepository.findOne(
        testProduct.getUuid(),
        new QuerySpec(ProductDto.class)
    );  
    assertNotNull(productDto);
    assertEquals(testProduct.getUuid(), productDto.getUuid());
    assertEquals(TEST_PRODUCT_NAME, productDto.getName());
    assertEquals(TEST_PRODUCT_DESCRIPTION, productDto.getDescription());
    assertEquals(TEST_PRODUCT_TYPE, productDto.getType());
  }
  
  @Test
  public void createProduct_onSuccess_allFieldsHaveSetValueAfterPersisted() {
    ProductDto newProduct = new ProductDto();
    newProduct.setName(TEST_PRODUCT_NAME_CREATE);
    newProduct.setDescription(TEST_PRODUCT_DESCRIPTION_CREATE);
    newProduct.setType(TEST_PRODUCT_TYPE_CREATE);
    
    ProductDto createdProduct = productRepository.create(newProduct);
    //DTO has the set value
    assertNotNull(createdProduct.getUuid());
    assertEquals(TEST_PRODUCT_NAME_CREATE, createdProduct.getName());
    assertEquals(TEST_PRODUCT_DESCRIPTION_CREATE, createdProduct.getDescription());
    assertEquals(TEST_PRODUCT_TYPE_CREATE, createdProduct.getType());
    //entity has the set value    
    Product productEntity = baseDao.findOneByNaturalId(createdProduct.getUuid(), Product.class);
    assertNotNull(productEntity.getId());
    assertEquals(TEST_PRODUCT_NAME_CREATE, productEntity.getName());
    assertEquals(TEST_PRODUCT_DESCRIPTION_CREATE, productEntity.getDescription());
    assertEquals(TEST_PRODUCT_TYPE_CREATE, productEntity.getType());
  }
  
  @Test
  public void updateProduct_whenSomeFieldsAreUpdated_productReturnedWithSelectedFieldsUpdated() {
     // Get the test product's DTO.
    QuerySpec querySpec = new QuerySpec(ProductDto.class);

    ProductDto productDto = productRepository.findOne(
        testProduct.getUuid(),querySpec);
    
    // Change the DTO's desc value.
    productDto.setDescription("new desc");
    
    // Save the DTO using the repository.
    productRepository.save(productDto);
    
    // Check that the entity has the new desc value.
    assertEquals("new desc", testProduct.getDescription());
  }  
  
  @Test
  public void deleteProduct_onProductLookup_productNotFound() {
    productRepository.delete(testProduct.getUuid());
    assertNull(entityManager.find(Product.class, testProduct.getId()));
  }

  @Test
  public void deleteProduct_onProductNotFound_throwResourceNotFoundException() {
    assertThrows(
      ResourceNotFoundException.class,
      () -> productRepository.delete(UUID.fromString("00000000-0000-0000-0000-000000000000"))
    );
  }
}
