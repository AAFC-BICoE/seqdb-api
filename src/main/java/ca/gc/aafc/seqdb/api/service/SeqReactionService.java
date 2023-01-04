package ca.gc.aafc.seqdb.api.service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.SeqReaction;
import ca.gc.aafc.seqdb.api.validation.SeqReactionValidator;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import java.util.UUID;

@Service
public class SeqReactionService extends DefaultDinaService<SeqReaction> {

  private final SeqReactionValidator validator;

  public SeqReactionService(
    @NonNull BaseDAO baseDAO,
    SeqReactionValidator validator,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
    this.validator = validator;
  }

  @Override
  protected void preCreate(SeqReaction entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  public void validateBusinessRules(SeqReaction entity) {
    applyBusinessRule(entity, validator);
  }

}
