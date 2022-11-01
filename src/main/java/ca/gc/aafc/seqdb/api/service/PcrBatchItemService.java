package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import ca.gc.aafc.seqdb.api.validation.PcrBatchItemValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;
import lombok.NonNull;

@Service
public class PcrBatchItemService extends DefaultDinaService<PcrBatchItem> {

  private final PcrBatchItemValidator validator;

  public PcrBatchItemService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv,
    @NonNull PcrBatchItemValidator validator) {
    super(baseDAO, sv);
    this.validator = validator;
  }

  @Override
  protected void preCreate(PcrBatchItem entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  public void validateBusinessRules(PcrBatchItem entity) {
    applyBusinessRule(entity, validator);
  }

}
