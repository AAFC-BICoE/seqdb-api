package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.PcrProfile;
import lombok.NonNull;

@Service
public class PcrProfileService extends DinaService<PcrProfile> {

  public PcrProfileService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(PcrProfile entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(PcrProfile entity) {

  }

  @Override
  protected void preUpdate(PcrProfile entity) {

  }
  
}
