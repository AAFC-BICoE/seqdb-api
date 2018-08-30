package ca.gc.aafc.seqdb.api.exceptionmapping;

import java.util.stream.Collectors;

import javax.inject.Named;
import javax.validation.ConstraintViolationException;

import io.crnk.core.engine.document.ErrorData;
import io.crnk.core.engine.error.ErrorResponse;
import io.crnk.core.engine.error.ExceptionMapper;
import io.crnk.core.engine.http.HttpStatus;

/**
 * Maps javax.validation.ConstraintViolationException to user-friendly errors to be displayed in
 * JSONAPI.
 */
@Named
public class ConstraintViolationExceptionMapper
    implements ExceptionMapper<ConstraintViolationException> {
  
  private static final Integer STATUS_ON_ERROR = HttpStatus.UNPROCESSABLE_ENTITY_422;
  
  @Override
  public ErrorResponse toErrorResponse(ConstraintViolationException exception) {
    return new ErrorResponse(
        exception.getConstraintViolations()
            .stream()
            .map(cv -> ErrorData.builder()
                .setStatus(STATUS_ON_ERROR.toString())
                .setTitle("Constraint violation")
                .setDetail(String.join(" ", cv.getPropertyPath().toString(), cv.getMessage()))
                .build())
            .collect(Collectors.toList()),
            STATUS_ON_ERROR
    );
  }

  @Override
  public ConstraintViolationException fromErrorResponse(ErrorResponse errorResponse) {
    throw new UnsupportedOperationException("Crnk client not supported");
  }

  @Override
  public boolean accepts(ErrorResponse errorResponse) {
    throw new UnsupportedOperationException("Crnk client not supported");
  }
  
}
