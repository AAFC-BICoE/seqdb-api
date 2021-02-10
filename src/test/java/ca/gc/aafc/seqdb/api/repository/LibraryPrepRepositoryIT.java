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
import ca.gc.aafc.seqdb.api.dto.MolecularSampleDto;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.Protocol;
import ca.gc.aafc.seqdb.api.entities.MolecularSample;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.api.testsupport.factories.ContainerTypeFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.LibraryPrepFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.ProductFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.ProtocolFactory;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepository;

public class LibraryPrepRepositoryIT extends BaseRepositoryTest {

  private static final String TEST_QUALITY = "test quality";
  
  private ContainerType testContainerType;
  private LibraryPrep testLibPrep;
  private LibraryPrepBatchDto testBatchDto;
  private Product testProduct;
  private Protocol testProtocol;
  private MolecularSample testMolecularSample;
  private List<MolecularSample> testSamples;
  
  @Inject
  private ResourceRepository<LibraryPrepBatchDto, Serializable> libraryPrepBatchRepository;
  
  @Inject
  private ResourceRepository<LibraryPrepDto, Serializable> libraryPrepRepository;
  
  @Inject
  private ResourceRepository<MolecularSampleDto, Serializable> sampleRepository;
  
  
  private LibraryPrep createTestLibraryPrep() {
    
    testContainerType = ContainerTypeFactory.newContainerType()
        .build();
    
    persist(testContainerType);
    
    testProduct = ProductFactory.newProduct().build();
    persist(testProduct);
    
    testProtocol = ProtocolFactory.newProtocol().build();
    persist(testProtocol);
    
    testMolecularSample = new MolecularSample();
    testMolecularSample.setName("test sample");
    testMolecularSample.setVersion("a");
    
    persist(testMolecularSample);
    
    testLibPrep = LibraryPrepFactory.newLibraryPrep()
        .quality(TEST_QUALITY)
        .molecularSample(testMolecularSample)
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
      MolecularSample sample = new MolecularSample();
      sample.setName("sample " + i);
      sample.setVersion("a");
      testSamples.add(sample);
      persist(sample);
    }
    
    testBatchDto = libraryPrepBatchRepository.findOne(
        testLibPrep.getLibraryPrepBatch().getUuid(),
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
        testLibPrep.getUuid(),
        new QuerySpec(LibraryPrepDto.class)
    );
    
    assertNotNull(dto);
    assertEquals(TEST_QUALITY, dto.getQuality());
  }
  
  @Test
  public void createLibPrep_onSuccess_libPrepCreated() {
    MolecularSampleDto newSample = new MolecularSampleDto();
    newSample.setName("new sample");
    newSample.setVersion("a");
    MolecularSampleDto newSampleCreated = sampleRepository.create(newSample);
    
    LibraryPrepDto newDto = new LibraryPrepDto();
    newDto.setSize("test size");
    newDto.setMolecularSample(newSampleCreated);
    newDto.setLibraryPrepBatch(testBatchDto);
    
    LibraryPrepDto created = libraryPrepRepository.create(newDto);
    
    assertNotNull(created.getUuid());
    assertEquals("test size", created.getSize());
    assertEquals(
        newSampleCreated.getUuid(),
        created.getMolecularSample().getUuid()
    );
    assertEquals(
        testLibPrep.getLibraryPrepBatch().getUuid(),
        created.getLibraryPrepBatch().getUuid()
    );
  }
  
  @Test
  public void createLibPrep_whenCellsOverLap_setExistingCellCoordinatesToNull() {
    LibraryPrepDto prep1 = new LibraryPrepDto();
    prep1.setWellRow("B");
    prep1.setWellColumn(5);
    prep1.setMolecularSample(
        sampleRepository.findOne(
            testSamples.get(0).getUuid(),
            new QuerySpec(MolecularSampleDto.class)
        )
    );
    prep1.setLibraryPrepBatch(testBatchDto);
    LibraryPrepDto createdPrep1 = libraryPrepRepository.create(prep1);
    
    LibraryPrepDto prep2 = new LibraryPrepDto();
    prep2.setWellRow("B");
    prep2.setWellColumn(5);
    prep2.setMolecularSample(
        sampleRepository.findOne(
            testSamples.get(1).getUuid(),
            new QuerySpec(MolecularSampleDto.class)
        )
    );
    prep2.setLibraryPrepBatch(testBatchDto);
    
    LibraryPrepDto createdPrep2 = libraryPrepRepository.create(prep2);
    LibraryPrepDto updatedPrep1 = libraryPrepRepository.findOne(
        createdPrep1.getUuid(),
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
        testLibPrep.getUuid(),
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
        testLibPrep.getUuid(),
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
        testLibPrep.getUuid(),
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
        testLibPrep.getUuid(),
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
        testLibPrep.getUuid(),
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
        testLibPrep.getUuid(),
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
        testLibPrep.getUuid(),
        new QuerySpec(LibraryPrepDto.class)
    );
    
    dto.setQuality("updated quality");
    libraryPrepRepository.save(dto);
    assertEquals("updated quality", testLibPrep.getQuality());
  }
  
  @Test
  public void deleteLibPrep_onSuccess_libPrepDeleted() {
    libraryPrepRepository.delete(testLibPrep.getUuid());
    assertNull(entityManager.find(LibraryPrep.class, testLibPrep.getId()));
  }
  
}
