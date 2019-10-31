package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;

import javax.inject.Inject;
import javax.validation.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.ContainerDto;
import ca.gc.aafc.seqdb.api.dto.LocationDto;
import ca.gc.aafc.seqdb.api.dto.SampleDto;
import ca.gc.aafc.seqdb.entities.Container;
import ca.gc.aafc.seqdb.entities.ContainerType;
import ca.gc.aafc.seqdb.entities.Location;
import ca.gc.aafc.seqdb.entities.Sample;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class LocationResourceRepositoryIT extends BaseRepositoryTest {

  private static final String TEST_CONTAINER_NAME = "test container";
  private static final Boolean TEST_FILLBYROW = true;
  
  private static final String TEST_SAMPLE_NAME = "sample name";
  private static final String TEST_SAMPLE_VERSION = "sample version";
  private static final String TEST_SAMPLE_EXPERIMENTER = "sample experimenter";
  
  private static final Integer TEST_LOCATION_COL = 7;
  private static final String TEST_LOCATION_ROW = "F";

  private Container testContainer;
  private ContainerType testContainerType;
  private Sample testSample;
  private Location testLocation;

  @Inject
  private ResourceRepository<LocationDto, Serializable> locationRepository;

  @Inject
  private ResourceRepository<SampleDto, Serializable> sampleRepository;

  @Inject
  private ResourceRepository<ContainerDto, Serializable> containerRepository;

  private Location createTestLocation() {
    testContainerType = new ContainerType();
    testContainerType.setName("test ct");
    testContainerType.setBaseType("basetype");
    testContainerType.setNumberOfColumns(8);
    testContainerType.setNumberOfRows(12);
    testContainerType.setNumberOfWells(
        testContainerType.getNumberOfRows() * testContainerType.getNumberOfColumns()
    );
    
    persist(testContainerType);
    
    testContainer = new Container();
    testContainer.setContainerType(testContainerType);
    testContainer.setContainerNumber(TEST_CONTAINER_NAME);
    testContainer.setFillByRow(TEST_FILLBYROW);
    
    persist(testContainer);
    
    testSample = new Sample();
    testSample.setName(TEST_SAMPLE_NAME);
    testSample.setVersion(TEST_SAMPLE_VERSION);
    testSample.setExperimenter(TEST_SAMPLE_EXPERIMENTER);
    
    persist(testSample);
    
    testLocation = new Location();
    testLocation.setSample(testSample);
    testLocation.setWellColumn(TEST_LOCATION_COL);
    testLocation.setWellRow(TEST_LOCATION_ROW);
    testLocation.setContainer(testContainer);
    
    persist(testLocation);
    
    return testLocation;
  }

  @BeforeEach
  public void setup() {
    createTestLocation();
  }

  @Test
  public void findLocation_whenExists_locationReturned() {
    LocationDto dto = locationRepository.findOne(
        testLocation.getId(),
        new QuerySpec(LocationDto.class)
    );
    
    assertNotNull(dto);
    assertEquals(TEST_LOCATION_COL, testLocation.getWellColumn());
  }
  
  @Test
  public void createLocation_onSuccess_locationPersisted() {
    LocationDto newDto = new LocationDto();
    newDto.setWellColumn(TEST_LOCATION_COL + 1);
    newDto.setWellRow(TEST_LOCATION_ROW);
    newDto.setContainer(
        containerRepository.findOne(
            testContainer.getId(),
            new QuerySpec(ContainerDto.class)
        )
        );
    newDto.setSample(
        sampleRepository.findOne(
            testSample.getId(),
            new QuerySpec(SampleDto.class)
        )
     );
    
    LocationDto created = locationRepository.create(newDto);
    
    assertNotNull(created.getLocationId());
    assertEquals((Integer) (TEST_LOCATION_COL + 1), created.getWellColumn());
    assertEquals(TEST_LOCATION_ROW, created.getWellRow());
  }
  
  @Test
  public void createLocation_whenWellRowLetterInvalid_throwValidationException() {
    LocationDto newDto = new LocationDto();
    newDto.setWellColumn(TEST_LOCATION_COL + 1);
    newDto.setWellRow("!");
    newDto.setContainer(
        containerRepository.findOne(
            testContainer.getId(),
            new QuerySpec(ContainerDto.class)
        )
    );
    newDto.setSample(
        sampleRepository.findOne(
            testSample.getId(),
            new QuerySpec(SampleDto.class)
        )
     );
    
    assertThrows(
        ValidationException.class,
        () -> locationRepository.create(newDto),
        "Well row must be in a letter format. (e.g: D)"
    );
  }
  
  @Test
  public void createLocation_whenWellColumnLessThan1_throwValidationException() {
    LocationDto newDto = new LocationDto();
    newDto.setWellColumn(0);
    newDto.setWellRow(TEST_LOCATION_ROW);
    newDto.setContainer(
        containerRepository.findOne(
            testContainer.getId(),
            new QuerySpec(ContainerDto.class)
        )
    );
    newDto.setSample(
        sampleRepository.findOne(
            testSample.getId(),
            new QuerySpec(SampleDto.class)
        )
     );
    
    assertThrows(
        ValidationException.class,
        () -> locationRepository.create(newDto),
        "Well column 0 is less than 1."
    );
  }
  
  @Test
  public void createLocation_whenWellColumnExceedsLimit_throwValidationException() {
    LocationDto newDto = new LocationDto();
    newDto.setWellColumn(9);
    newDto.setWellRow(TEST_LOCATION_ROW);
    newDto.setContainer(
        containerRepository.findOne(
            testContainer.getId(),
            new QuerySpec(ContainerDto.class)
        )
    );
    newDto.setSample(
        sampleRepository.findOne(
            testSample.getId(),
            new QuerySpec(SampleDto.class)
        )
    );
    
    assertThrows(
        ValidationException.class,
        () -> locationRepository.create(newDto),
        "Well column 9 exceeds container's number of columns."
    );
  }
  
  @Test
  public void createLocation_whenWellRowExceedsLimit_throwValidationException() {
    LocationDto newDto = new LocationDto();
    newDto.setWellColumn(TEST_LOCATION_COL);
    newDto.setWellRow("M");
    newDto.setContainer(
        containerRepository.findOne(
            testContainer.getId(),
            new QuerySpec(ContainerDto.class)
        )
    );
    newDto.setSample(
        sampleRepository.findOne(
            testSample.getId(),
            new QuerySpec(SampleDto.class)
        )
    );
    
    assertThrows(
        ValidationException.class,
        () -> locationRepository.create(newDto),
        "Well row M exceeds container's number of rows."
    );
  }
  
  @Test
  public void updateLocation_onSucess_locationUpdated() {
    LocationDto dto = locationRepository.findOne(
        testLocation.getId(),
        new QuerySpec(LocationDto.class)
    );
    
    dto.setWellColumn(1);
    
    locationRepository.save(dto);
    
    assertEquals((Integer) 1, testLocation.getWellColumn());
  }
  
  @Test
  public void deleteLocation_onSuccess_locationDeleted() {
    locationRepository.delete(testLocation.getId());
    assertNull(entityManager.find(Location.class, testLocation.getId()));
  }
}
