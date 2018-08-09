package ca.gc.aafc.seqdb.api.exceptionmapping;

import java.util.stream.Collectors;

import javax.inject.Named;
import javax.validation.ConstraintViolationException;

import io.crnk.core.engine.document.ErrorData;
import io.crnk.core.engine.error.ErrorResponse;
import io.crnk.core.engine.error.ExceptionMapper;

/**
 * Maps javax.validation.ConstraintViolationException to user-friendly errors to be displayed in
 * JSONAPI.
 */
@Named
public class ConstraintViolationExceptionMapper
    implements ExceptionMapper<ConstraintViolationException> {

  @Override
  public ErrorResponse toErrorResponse(ConstraintViolationException exception) {
    return new ErrorResponse(
        exception.getConstraintViolations()
            .stream()
            .map(cv -> ErrorData.builder()
                .setStatus("400")
                .setTitle("Constraint violation")
                .setDetail(String.join(" ", cv.getPropertyPath().toString(), cv.getMessage()))
                .build())
            .collect(Collectors.toList()),
        400
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
