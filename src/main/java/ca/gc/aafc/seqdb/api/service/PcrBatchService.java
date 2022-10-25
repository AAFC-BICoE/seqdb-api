package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import ca.gc.aafc.seqdb.api.validation.PcrBatchValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;
import lombok.NonNull;

@Service
public class PcrBatchService extends DefaultDinaService<PcrBatch> {

  private final PcrBatchValidator validator;

  public PcrBatchService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv,
    @NonNull PcrBatchValidator validator) {
    super(baseDAO, sv);
    this.validator = validator;
  }

  @Override
  protected void preCreate(PcrBatch entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  public void validateBusinessRules(PcrBatch entity) {
    applyBusinessRule(entity, validator);
  }

}
