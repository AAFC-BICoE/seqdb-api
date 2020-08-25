package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import lombok.NonNull;

@Service
public class PcrPrimerService extends DinaService<PcrPrimer> {

  public PcrPrimerService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(PcrPrimer entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(PcrPrimer entity) {

  }

  @Override
  protected void preUpdate(PcrPrimer entity) {

  }
  
}
