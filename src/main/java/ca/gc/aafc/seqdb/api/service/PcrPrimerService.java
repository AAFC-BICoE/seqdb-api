package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import lombok.NonNull;

@Service
public class PcrPrimerService extends DefaultDinaService<PcrPrimer> {

  public PcrPrimerService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(PcrPrimer entity) {
    entity.setUuid(UUID.randomUUID());
  }
  
}
