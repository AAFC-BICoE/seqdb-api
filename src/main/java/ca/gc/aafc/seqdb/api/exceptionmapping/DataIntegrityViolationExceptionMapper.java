package ca.gc.aafc.seqdb.api.exceptionmapping;

import java.util.Collections;

import javax.inject.Named;

import org.springframework.dao.DataIntegrityViolationException;

import io.crnk.core.engine.document.ErrorData;
import io.crnk.core.engine.error.ErrorResponse;
import io.crnk.core.engine.error.ExceptionMapper;
import io.crnk.core.engine.http.HttpStatus;

/**
 * Maps org.springframework.dao.DataIntegrityViolationException to user-friendly errors to be
 * displayed in JSONAPI.
 */
@Named
public class DataIntegrityViolationExceptionMapper
    implements ExceptionMapper<DataIntegrityViolationException> {

  @Override
  public ErrorResponse toErrorResponse(DataIntegrityViolationException exception) {
    Integer status = HttpStatus.UNPROCESSABLE_ENTITY_422;
    
    return new ErrorResponse(
        Collections.singletonList(
            ErrorData.builder()
                .setStatus(status.toString())
                .setTitle("Data integrity violation")
                .setDetail(exception.getMessage())
                .build()
        ),
        status
    );
  }

  @Override
  public DataIntegrityViolationException fromErrorResponse(ErrorResponse errorResponse) {
    throw new UnsupportedOperationException("Crnk client not supported");
  }

  @Override
  public boolean accepts(ErrorResponse errorResponse) {
    throw new UnsupportedOperationException("Crnk client not supported");
  }
  
}
