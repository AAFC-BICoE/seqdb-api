package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.ThermocycleProfile;
import lombok.NonNull;

@Service
public class ThermocyclerProfileService extends DefaultDinaService<ThermocycleProfile> {

  public ThermocyclerProfileService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(ThermocycleProfile entity) {
    entity.setUuid(UUID.randomUUID());
  }
  
}
