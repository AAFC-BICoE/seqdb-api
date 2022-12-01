package ca.gc.aafc.seqdb.api.service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.SeqReaction;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import java.util.UUID;

@Service
public class SeqReactionService extends DefaultDinaService<SeqReaction> {

  public SeqReactionService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(SeqReaction entity) {
    entity.setUuid(UUID.randomUUID());
  }

}
