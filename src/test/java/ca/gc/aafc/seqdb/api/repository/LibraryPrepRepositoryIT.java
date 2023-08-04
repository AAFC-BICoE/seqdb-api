package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.api.testsupport.factories.ContainerTypeFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.LibraryPrepFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.ProductFactory;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.LibraryPrepTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.crnk.core.queryspec.QuerySpec;
import javax.inject.Inject;
import javax.validation.ValidationException;

public class LibraryPrepRepositoryIT extends BaseRepositoryTest {

  private static final String TEST_QUALITY = "test quality";

  private LibraryPrep testLibPrep;
  private LibraryPrepBatchDto testBatchDto;

  @Inject
  private LibraryPrepBatchRepository libraryPrepBatchRepository;

  @Inject
  private LibraryPrepRepository libraryPrepRepository;

  private LibraryPrep createTestLibraryPrep() {

    ContainerType testContainerType = ContainerTypeFactory.newContainerType()
            .build();
    
    persist(testContainerType);

    Product testProduct = ProductFactory.newProduct().build();
    persist(testProduct);

    testLibPrep = LibraryPrepFactory.newLibraryPrep()
        .quality(TEST_QUALITY)
        .build();
    testLibPrep.getLibraryPrepBatch().setContainerType(testContainerType);
    persist(testLibPrep.getLibraryPrepBatch());
    persist(testLibPrep);
    
    // This is needed in the tests to initialize the PersistentBag for the batch's "libraryPreps"
    // field for the test. This doesn't matter in prod.
    entityManager.flush();
    entityManager.refresh(testLibPrep.getLibraryPrepBatch());

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

    LibraryPrepDto newDto = LibraryPrepTestFixture.newLibraryPrep();
    newDto.setLibraryPrepBatch(testBatchDto);

    LibraryPrepDto created = libraryPrepRepository.create(newDto);
    
    assertNotNull(created.getUuid());
    assertEquals("test size", created.getSize());
    assertEquals(
      LibraryPrepTestFixture.MATERIAL_SAMPLE_UUID.toString(),
        created.getMaterialSample().getId()
    );
    assertEquals(
        testLibPrep.getLibraryPrepBatch().getUuid(),
        created.getLibraryPrepBatch().getUuid()
    );
  }
  
  @Test
  public void createLibPrep_whenCellsOverLap_setExistingCellCoordinatesToNull() {
    LibraryPrepDto prep1 = LibraryPrepTestFixture.newLibraryPrep();
    prep1.setWellRow("B");
    prep1.setWellColumn(5);
    prep1.setMaterialSample(LibraryPrepTestFixture.newMaterialSampleExternalRelationship());
    prep1.setLibraryPrepBatch(testBatchDto);
    LibraryPrepDto createdPrep1 = libraryPrepRepository.create(prep1);
    
    LibraryPrepDto prep2 = LibraryPrepTestFixture.newLibraryPrep();
    prep2.setWellRow("B");
    prep2.setWellColumn(5);
    prep2.setMaterialSample(LibraryPrepTestFixture.newMaterialSampleExternalRelationship());
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
    assertThrows(
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
    assertThrows(
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
        "Invalid well column: 100",
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
        "Invalid well row: Z",
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
