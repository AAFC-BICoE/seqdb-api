package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.PcrProfile;
import lombok.NonNull;

@Service
public class PcrProfileService extends DefaultDinaService<PcrProfile> {

  public PcrProfileService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(PcrProfile entity) {
    entity.setUuid(UUID.randomUUID());
  }
  
}
