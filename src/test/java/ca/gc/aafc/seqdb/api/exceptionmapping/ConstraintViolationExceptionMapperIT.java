package ca.gc.aafc.seqdb.api.exceptionmapping;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.repository.BaseRepositoryTest;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import io.crnk.core.engine.document.ErrorData;
import io.crnk.core.engine.error.ErrorResponse;
import io.crnk.core.repository.ResourceRepositoryV2;

public class ConstraintViolationExceptionMapperIT extends BaseRepositoryTest {

  @Inject
  private ConstraintViolationExceptionMapper constraintViolationExceptionMapper;
  
  private ResourceRepositoryV2<PcrBatchDto, Serializable> pcrBatchRepository;

  /**
   * Get the repository facade from crnk, which will invoke all filters, decorators, etc.
   */
  @Before
  public void initRepositories() {
    this.pcrBatchRepository = this.resourceRegistry.getEntry(PcrBatchDto.class)
        .getResourceRepositoryFacade();
  }
  
  @Test
  public void persistPcrBatchWithTooLongNameAndNullPlateSize_whenConstraintViolationExceptionIsThrown_mapperCreatesReadableErrorMessages() {
    // Make a String with over 50 characters (pcr batch name length limit is 50).
    String stringWith51Chars = "01234567890123456789012345678901234567890123456789a";
    assertEquals(51, stringWith51Chars.length());
    
    // Create the batch
    PcrBatchDto testBatch = new PcrBatchDto();
    testBatch.setName(stringWith51Chars);
    testBatch.setType(PcrBatchType.SANGER);
    testBatch.setPlateSize(null);
    
    try {
      // Attempt the create.
      this.pcrBatchRepository.create(testBatch);
    } catch(ConstraintViolationException exception) {
      // This test expects the ConstraintViolationException to be thrown, it will fail otherwise.
      // Generate the error response here:
      ErrorResponse errorResponse = constraintViolationExceptionMapper.toErrorResponse(exception);
      // Get the errors sorted by detail. The default error order is not consistent.
      List<ErrorData> errors = errorResponse.getErrors()
          .stream()
          .sorted((a, b) -> a.getDetail().compareTo(b.getDetail()))
          .collect(Collectors.toList());
      
      // Assert that the correct error messages were generated.
      assertEquals(2, errors.size());
      assertEquals("name size must be between 0 and 50", errors.get(0).getDetail());
      assertEquals("plateSize must not be null", errors.get(1).getDetail());
      return;
    }
    
    // This test method should end at the "return" in the catch block.
    fail("ConstraintViolationException not thrown");
  }
  
}
