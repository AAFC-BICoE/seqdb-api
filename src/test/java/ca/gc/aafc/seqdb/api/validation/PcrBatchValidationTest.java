package ca.gc.aafc.seqdb.api.validation;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;
import ca.gc.aafc.seqdb.api.testsupport.factories.PcrBatchFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PcrBatchValidationTest extends SequenceModuleBaseIT {

  @Inject
  PcrBatchValidator validator;

  @Test
  void validate_invalidBatchType_ErrorsReturned() {

    PcrBatch pcrBatch = PcrBatchFactory.newPcrBatch()
      .batchType("abc")
      .build();

    Errors errors = new BeanPropertyBindingResult(pcrBatch, pcrBatch.getUuid().toString());
    validator.validate(pcrBatch, errors);
    assertEquals(1, errors.getAllErrors().size());
    assertTrue(errors.getAllErrors().get(0).getDefaultMessage().contains("Value not found in"));

  }
}
