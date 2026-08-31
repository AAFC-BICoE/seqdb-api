package ca.gc.aafc.seqdb.api.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.ControlledVocabularyService;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabulary;

@Service
public class SequenceControlledVocabularyService extends ControlledVocabularyService<SequenceControlledVocabulary> {

  public SequenceControlledVocabularyService(BaseDAO baseDAO,
                                               SmartValidator smartValidator) {
    super(baseDAO, smartValidator, SequenceControlledVocabulary.class);
  }
}
