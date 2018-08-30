package ca.gc.aafc.seqdb.api.exceptionmapping;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

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
  
  @Inject
  private ResourceRepositoryV2<PcrBatchDto, Serializable> pcrBatchRepository;
  
  @Test
  public void persistPcrBatch_whenNameIsTooLongAndPlateSizeIsNull_mapperCreatesReadableErrorMessages() {
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
    } catch (ConstraintViolationException exception) {
      // This test expects the ConstraintViolationException to be thrown, it will fail otherwise.
      // Generate the error response here:
      ErrorResponse errorResponse = constraintViolationExceptionMapper.toErrorResponse(exception);
      
      // Assert correct http status.
      assertEquals(422, errorResponse.getHttpStatus());
      
      // Get the errors sorted by detail. The default error order is not consistent.
      List<ErrorData> errors = errorResponse.getErrors()
          .stream()
          .sorted((a, b) -> a.getDetail().compareTo(b.getDetail()))
          .collect(Collectors.toList());
      
      // 2 errors should be given.
      assertEquals(2, errors.size());
      
      // Assert correct error message, status and title (@Size name length error)
      assertEquals("name size must be between 0 and 50", errors.get(0).getDetail());
      assertEquals("422", errors.get(0).getStatus());
      assertEquals("Constraint violation", errors.get(0).getTitle());
      
      // Assert correct error message, status and title (@NotNull plateSize error)
      assertEquals("plateSize must not be null", errors.get(1).getDetail());
      assertEquals("422", errors.get(1).getStatus());
      assertEquals("Constraint violation", errors.get(1).getTitle());
      return;
    }
    
    // This test method should end at the "return" in the catch block.
    fail("ConstraintViolationException not thrown");
  }
  
}
