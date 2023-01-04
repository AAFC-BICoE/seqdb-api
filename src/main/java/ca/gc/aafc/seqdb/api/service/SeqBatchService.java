package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import ca.gc.aafc.seqdb.api.validation.SeqBatchValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;
import lombok.NonNull;

@Service
public class SeqBatchService extends DefaultDinaService<SeqBatch> {

  private final SeqBatchValidator validator;

  public SeqBatchService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv,
    SeqBatchValidator validator) {
    super(baseDAO, sv);
    this.validator = validator;
  }

  @Override
  protected void preCreate(SeqBatch entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  public void validateBusinessRules(SeqBatch entity) {
    applyBusinessRule(entity, validator);
  }

}
