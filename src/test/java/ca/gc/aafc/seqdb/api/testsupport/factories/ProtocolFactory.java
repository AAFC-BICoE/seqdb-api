package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.Protocol;
import ca.gc.aafc.seqdb.api.entities.Protocol.ProtocolBuilder;
import ca.gc.aafc.seqdb.api.entities.Protocol.ProtocolType;

public class ProtocolFactory implements TestableEntityFactory<Protocol> {

  @Override
  public Protocol getEntityInstance() {
    return newProtocol().build();
  }

  /**
   * Static method that can be called to return a configured builder that can
   * be further customized to return the actual entity object, call the
   * .build() method on a builder,with specified group passed on as parameter
   * 
   * @param group Group to be set on the {@link Protocol}.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static Protocol.ProtocolBuilder newProtocol() {
    return Protocol.builder()
          .name(TestableEntityFactory.generateRandomName(10))
          .type(ProtocolType.COLLECTION_EVENT);
  }  
  
  /**
   * A utility method to create a list of qty number of Protocols with no configuration.
   * 
   * @param qty The number of Protocols populated in the list
   * @return List of Protocol
   */
  public static List<Protocol> newListOf(int qty) {
        
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
   * @return List of Protocol
   */
  public static List<Protocol> newListOf(int qty,
      BiFunction<Protocol.ProtocolBuilder, Integer, Protocol.ProtocolBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, ProtocolFactory::newProtocol, configuration,
        ProtocolBuilder::build);
  }

}
