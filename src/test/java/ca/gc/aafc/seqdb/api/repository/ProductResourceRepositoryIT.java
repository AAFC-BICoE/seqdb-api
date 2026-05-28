package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.seqdb.api.dto.ProductDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.ProductTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

public class ProductResourceRepositoryIT extends BaseRepositoryTestV2 {

  protected static final String TEST_PRODUCT_NAME_CREATE = "product name create";
  protected static final String TEST_PRODUCT_TYPE_CREATE = "product type create";
  protected static final String TEST_PRODUCT_DESCRIPTION_CREATE = "product desc create";

  @Inject
  private ProductRepository productRepository;

  @Test
  public void createProduct_onSuccess_allFieldsHaveSetValueAfterPersisted()
      throws ResourceGoneException, ResourceNotFoundException {
    ProductDto newProduct = new ProductDto();
    newProduct.setName(TEST_PRODUCT_NAME_CREATE);
    newProduct.setDescription(TEST_PRODUCT_DESCRIPTION_CREATE);
    newProduct.setType(TEST_PRODUCT_TYPE_CREATE);

    UUID productUuid = createWithRepository(newProduct, productRepository::onCreate);

    ProductDto reloadedDto = productRepository.getOne(productUuid, "").getDto();
    //DTO has the set value
    assertNotNull(reloadedDto.getUuid());
    assertEquals(TEST_PRODUCT_NAME_CREATE, reloadedDto.getName());
    assertEquals(TEST_PRODUCT_DESCRIPTION_CREATE, reloadedDto.getDescription());
    assertEquals(TEST_PRODUCT_TYPE_CREATE, reloadedDto.getType());
  }

  @Test
  public void updateProduct_whenSomeFieldsAreUpdated_productReturnedWithSelectedFieldsUpdated()
      throws ResourceGoneException, ResourceNotFoundException {

    ProductDto productDto = ProductTestFixture.newProduct();
    UUID productUuid = createWithRepository(productDto, productRepository::onCreate);

    // Change the DTO's desc value
    productDto.setDescription("new desc");

    // Save using the repository
    JsonApiDocument productToUpdate = JsonApiDocuments.createJsonApiDocument(productUuid,
      ProductDto.TYPENAME, Map.of("description", "new desc"));
    productRepository.onUpdate(productToUpdate, productUuid);

    ProductDto reloadedDto = productRepository.getOne(productUuid, "").getDto();
    // Check that the entity has the new desc value.
    assertEquals("new desc", reloadedDto.getDescription());
  }

  @Test
  public void deleteProduct_onProductLookup_productNotFound()
      throws ResourceGoneException, ResourceNotFoundException {
    ProductDto productDto = ProductTestFixture.newProduct();
    UUID productUuid = createWithRepository(productDto, productRepository::onCreate);
    productRepository.onDelete(productUuid);
  }

  @Test
  public void deleteProduct_onProductNotFound_throwResourceNotFoundException() {
    assertThrows(
      ResourceNotFoundException.class,
      () -> productRepository.onDelete(UUID.fromString("00000000-0000-0000-0000-000000000000"))
    );
  }
}
