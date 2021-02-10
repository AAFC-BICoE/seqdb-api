package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.MolecularSample;
import ca.gc.aafc.seqdb.api.entities.MolecularSample.MolecularSampleBuilder;

/**
 * Creates Sample entities that are populated with all the required fields.
 */
public class MolecularSampleFactory implements TestableEntityFactory<MolecularSample> {
  
  @Override
  public MolecularSample getEntityInstance() {

    return newMolecularSample().build();

  }

  /**
   * Static method that can be called to return a configured builder that can
   * be further customized to return the actual entity object, call the
   * .build() method on a builder,with specified group passed on as parameter
   * @return Pre-configured builder with all mandatory fields set
   */
  public static MolecularSample.MolecularSampleBuilder newMolecularSample() {
    
    MolecularSample.MolecularSampleBuilder bldr = MolecularSample.builder()
        .name(TestableEntityFactory.generateRandomName(10))
        .version(TestableEntityFactory.generateRandomNameLettersOnly(1));
    
    return bldr;

  }
  
  
  /**
   * A utility method to create a list of qty number of Samples with no
   * configuration.
   * 
   * @param qty
   *            The number of Samples populated in the list
   * @return List of Samples
   */
  public static List<MolecularSample> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of Sample with an
   * incrementing attribute based on the configuration argument. An example of
   * configuration would be the functional interface (bldr, index) ->
   * bldr.name(" string" + index)
   * 
   * @param qty
   *            The number of Sample that is populated in the list.
   * @param configuration
   *            the function to apply, usually to differentiate the different
   *            entities in the list.
   * @return List of Sample
   */
  public static List<MolecularSample> newListOf(int qty,
      BiFunction<MolecularSample.MolecularSampleBuilder, Integer, MolecularSample.MolecularSampleBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, MolecularSampleFactory::newMolecularSample, configuration,
    MolecularSampleBuilder::build);
  }

}
