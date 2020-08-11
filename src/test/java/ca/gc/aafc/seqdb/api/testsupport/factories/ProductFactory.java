package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.Product.ProductBuilder;

public class ProductFactory implements TestableEntityFactory<Product> {
  
  @Override
  public Product getEntityInstance() {
    return newProduct().build();
  }

  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @param group Group to be set on the {@link Product}
   * @return Pre-configured builder with all mandatory fields set
   */
  public static Product.ProductBuilder newProduct() {
    return Product.builder()
        .name(TestableEntityFactory.generateRandomName(10));
  }
  
  /**
   * A utility method to create a list of qty number of Product with no configuration.
   * 
   * @param qty The number of Product populated in the list
   * @return List of Product
   */
  public static List<Product> newListOf(int qty) {
        
    return newListOf(qty, null);
  }
  
  /**
   * A utility method to create a list of qty number of entities with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty           The number of entities that is populated in the list.
   * @param configuration the function to apply, usually to differentiate the different entities in
   *                      the list.
   * @return List of Product
   */
  public static List<Product> newListOf(int qty,
      BiFunction<Product.ProductBuilder, Integer, Product.ProductBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, ProductFactory::newProduct, configuration,
        ProductBuilder::build);

  }

}
