package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.Location;
import ca.gc.aafc.seqdb.api.entities.Location.LocationBuilder;

public class LocationFactory implements TestableEntityFactory<Location> {

  @Override
  public Location getEntityInstance() {
    return newLocation().build();
  }
  
  public static Location.LocationBuilder newLocation() {
    LocationBuilder bldr = Location.builder();
      bldr.container(ContainerFactory.newContainer().build());
    return bldr;
  }

  public static List<Location> newListOf(int qty) {
    return newListOf(qty, null);
  }

  public static List<Location> newListOf(int qty, BiFunction<Location.LocationBuilder, Integer, Location.LocationBuilder> configuration) {
    return TestableEntityFactory.newEntity(qty, LocationFactory::newLocation, configuration, Location.LocationBuilder::build);
  }
}