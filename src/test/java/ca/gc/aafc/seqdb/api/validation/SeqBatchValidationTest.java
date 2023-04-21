package ca.gc.aafc.seqdb.api.validation;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;
import ca.gc.aafc.seqdb.api.testsupport.factories.SeqBatchFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeqBatchValidationTest extends SequenceModuleBaseIT {

  @Inject
  SeqBatchValidator validator;

  @Test
  void validate_invalidSequencingType_ErrorsReturned() {

    SeqBatch seqBatch = SeqBatchFactory.newSeqBatch()
      .sequencingType("abc")
      .build();

    Errors errors = new BeanPropertyBindingResult(seqBatch, seqBatch.getUuid().toString());
    validator.validate(seqBatch, errors);
    assertEquals(1, errors.getAllErrors().size());
    assertTrue(errors.getAllErrors().get(0).getDefaultMessage().contains("Value not found in"));

  }
}
