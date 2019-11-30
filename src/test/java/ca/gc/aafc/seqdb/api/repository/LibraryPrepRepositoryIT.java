package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.dto.SampleDto;
import ca.gc.aafc.seqdb.entities.ContainerType;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.Product;
import ca.gc.aafc.seqdb.entities.Protocol;
import ca.gc.aafc.seqdb.entities.Sample;
import ca.gc.aafc.seqdb.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.testsupport.factories.LibraryPrepFactory;
import ca.gc.aafc.seqdb.testsupport.factories.ProductFactory;
import ca.gc.aafc.seqdb.testsupport.factories.ProtocolFactory;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class LibraryPrepRepositoryIT extends BaseRepositoryTest {

  private static final String TEST_QUALITY = "test quality";
  
  private ContainerType testContainerType;
  private LibraryPrep testLibPrep;
  private LibraryPrepBatchDto testBatchDto;
  private Product testProduct;
  private Protocol testProtocol;
  private Sample testSample;
  private List<Sample> testSamples;
  
  @Inject
  private ResourceRepository<LibraryPrepBatchDto, Serializable> libraryPrepBatchRepository;
  
  @Inject
  private ResourceRepository<LibraryPrepDto, Serializable> libraryPrepRepository;
  
  @Inject
  private ResourceRepository<SampleDto, Serializable> sampleRepository;
  
  
  private LibraryPrep createTestLibraryPrep() {
    testContainerType = new ContainerType();
    testContainerType.setName("test ct");
    testContainerType.setBaseType("basetype");
    testContainerType.setNumberOfColumns(8);
    testContainerType.setNumberOfRows(12);
    testContainerType.setNumberOfWells(
        testContainerType.getNumberOfRows() * testContainerType.getNumberOfColumns()
    );
    
    persist(testContainerType);
    
    testProduct = ProductFactory.newProduct().build();
    persist(testProduct);
    
    Group testGroup = new Group("group name");
    persistGroup(testGroup);
    
    testProtocol = ProtocolFactory.newProtocol(testGroup).build();
    entityManager.persist(testProtocol.getGroup());
    persist(testProtocol);
    
    testSample = new Sample();
    testSample.setName("test sample");
    testSample.setVersion("a");
    testSample.setExperimenter("Mat Poff");
    
    persist(testSample);
    
    testLibPrep = LibraryPrepFactory.newLibraryPrep()
        .quality(TEST_QUALITY)
        .sample(testSample)
        .build();
    testLibPrep.getLibraryPrepBatch().setContainerType(testContainerType);
    persist(testLibPrep.getLibraryPrepBatch());
    persist(testLibPrep);
    
    // This is needed in the tests to initialize the PersistentBag for the batch's "libraryPreps"
    // field for the test. This doesn't matter in prod.
    entityManager.flush();
    entityManager.refresh(testLibPrep.getLibraryPrepBatch());
    
    testSamples = new ArrayList<>();
    for (int i = 1; i <= 22; i++) {
      Sample sample = new Sample();
      sample.setName("sample " + i);
      sample.setVersion("a");
      sample.setExperimenter("Mat Poff");
      testSamples.add(sample);
      persist(sample);
    }
    
    testBatchDto = libraryPrepBatchRepository.findOne(
        testLibPrep.getLibraryPrepBatch().getId(),
        new QuerySpec(LibraryPrepBatchDto.class)
    );
    
    return testLibPrep;
  }
  
  @BeforeEach
  public void setup() {
    createTestLibraryPrep();
  }
  
  @Test
  public void findLibPrep_whenLibPrepExists_libPrepReturned() {
    LibraryPrepDto dto = libraryPrepRepository.findOne(
        testLibPrep.getId(),
        new QuerySpec(LibraryPrepDto.class)
    );
    
    assertNotNull(dto);
    assertEquals(TEST_QUALITY, dto.getQuality());
  }
  
  @Test
  public void createLibPrep_onSuccess_libPrepCreated() {
    SampleDto newSample = new SampleDto();
    newSample.setName("new sample");
    newSample.setVersion("a");
    newSample.setExperimenter("Mat Poff");
    SampleDto newSampleCreated = sampleRepository.create(newSample);
    
    LibraryPrepDto newDto = new LibraryPrepDto();
    newDto.setSize("test size");
    newDto.setSample(newSampleCreated);
    newDto.setLibraryPrepBatch(testBatchDto);
    
    LibraryPrepDto created = libraryPrepRepository.create(newDto);
    
    assertNotNull(created.getLibraryPrepId());
    assertEquals("test size", created.getSize());
    assertEquals(
        newSampleCreated.getSampleId(),
        created.getSample().getSampleId()
    );
    assertEquals(
        testLibPrep.getLibraryPrepBatch().getId(),
        created.getLibraryPrepBatch().getLibraryPrepBatchId()
    );
  }
  
  @Test
  public void createLibPrep_whenCellsOverLap_setExistingCellCoordinatesToNull() {
    LibraryPrepDto prep1 = new LibraryPrepDto();
    prep1.setWellRow("B");
    prep1.setWellColumn(5);
    prep1.setSample(
        sampleRepository.findOne(
            testSamples.get(0).getId(),
            new QuerySpec(SampleDto.class)
        )
    );
    prep1.setLibraryPrepBatch(testBatchDto);
    LibraryPrepDto createdPrep1 = libraryPrepRepository.create(prep1);
    
    LibraryPrepDto prep2 = new LibraryPrepDto();
    prep2.setWellRow("B");
    prep2.setWellColumn(5);
    prep2.setSample(
        sampleRepository.findOne(
            testSamples.get(1).getId(),
            new QuerySpec(SampleDto.class)
        )
    );
    prep2.setLibraryPrepBatch(testBatchDto);
    
    LibraryPrepDto createdPrep2 = libraryPrepRepository.create(prep2);
    LibraryPrepDto updatedPrep1 = libraryPrepRepository.findOne(
        createdPrep1.getLibraryPrepId(),
        new QuerySpec(LibraryPrepDto.class)
    );
    
    // The second created prep should have the coordinates set, and the original one should have had them set null.
    assertEquals((Integer) 5, createdPrep2.getWellColumn());
    assertEquals("B", createdPrep2.getWellRow());
    assertNull(updatedPrep1.getWellColumn());
    assertNull(updatedPrep1.getWellRow());
  }
  
  @Test
  public void updateLibPrep_whenWellRowLetterInvalid_throwValidationException() {
    LibraryPrepDto dto = libraryPrepRepository.findOne(
        testLibPrep.getId(),
        new QuerySpec(LibraryPrepDto.class)
    );
    dto.setWellRow("!");
    dto.setWellColumn(1);
    ValidationException exception = assertThrows(
        ValidationException.class,
        () -> libraryPrepRepository.save(dto)
    );
  }
  
  @Test
  public void updateLibPrep_whenRowIsSetAndColumnIsNull_throwValidationException() {
    LibraryPrepDto dto = libraryPrepRepository.findOne(
        testLibPrep.getId(),
        new QuerySpec(LibraryPrepDto.class)
    );
    dto.setWellRow("C");
    dto.setWellColumn(null);
    ValidationException exception = assertThrows(
        ValidationException.class,
        () -> libraryPrepRepository.save(dto)
    );
    assertEquals(
        "Well column cannot be null when well row is set.",
        exception.getMessage()
    );
  }
  
  @Test
  public void updateLibPrep_whenColumnIs0_throwValidationException() {
    LibraryPrepDto dto = libraryPrepRepository.findOne(
        testLibPrep.getId(),
        new QuerySpec(LibraryPrepDto.class)
    );
    dto.setWellRow("A");
    dto.setWellColumn(0);
    ValidationException exception = assertThrows(
        ValidationException.class,
        () -> libraryPrepRepository.save(dto)
    );
  }

  @Test
  public void updateLibPrep_whenColumnExceedsLimit_throwValidationException() {
    LibraryPrepDto dto = libraryPrepRepository.findOne(
        testLibPrep.getId(),
        new QuerySpec(LibraryPrepDto.class)
    );
    dto.setWellRow("A");
    dto.setWellColumn(100);
    ValidationException exception = assertThrows(
        ValidationException.class,
        () -> libraryPrepRepository.save(dto)
    );
    assertEquals(
        "Well column 100 exceeds container's number of columns.",
        exception.getMessage()
    );
  }
  
  @Test
  public void updateLibPrep_whenRowExceedsLimit_throwValidationException() {
    LibraryPrepDto dto = libraryPrepRepository.findOne(
        testLibPrep.getId(),
        new QuerySpec(LibraryPrepDto.class)
    );
    dto.setWellRow("Z");
    dto.setWellColumn(5);
    ValidationException exception = assertThrows(
        ValidationException.class,
        () -> libraryPrepRepository.save(dto)
    );
    assertEquals(
        "Row letter Z exceeds container's number of rows.",
        exception.getMessage()
    );
  }

  @Test
  public void updateLibPrep_whenColumnIsSetAndRowIsNull_throwValidationException() {
    LibraryPrepDto dto = libraryPrepRepository.findOne(
        testLibPrep.getId(),
        new QuerySpec(LibraryPrepDto.class)
    );
    dto.setWellRow(null);
    dto.setWellColumn(8);
    ValidationException exception = assertThrows(
        ValidationException.class,
        () -> libraryPrepRepository.save(dto)
    );
    assertEquals(
        "Well row cannot be null when well column is set.",
        exception.getMessage()
    );
  }
  
  @Test
  public void updateLibPrep_onSuccess_libPrepUpdated() {
    LibraryPrepDto dto = libraryPrepRepository.findOne(
        testLibPrep.getId(),
        new QuerySpec(LibraryPrepDto.class)
    );
    
    dto.setQuality("updated quality");
    libraryPrepRepository.save(dto);
    assertEquals("updated quality", testLibPrep.getQuality());
  }
  
  @Test
  public void deleteLibPrep_onSuccess_libPrepDeleted() {
    libraryPrepRepository.delete(testLibPrep.getId());
    assertNull(entityManager.find(LibraryPrep.class, testLibPrep.getId()));
  }
  
}
