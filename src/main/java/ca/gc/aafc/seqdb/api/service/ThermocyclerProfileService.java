package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.ThermocyclerProfile;
import lombok.NonNull;

// CHECKSTYLE:OFF NoFinalizer
// CHECKSTYLE:OFF SuperFinalize
@Service
public class ThermocyclerProfileService extends DefaultDinaService<ThermocyclerProfile> {

  public ThermocyclerProfileService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(ThermocyclerProfile entity) {
    entity.setUuid(UUID.randomUUID());
  }

  // Fixes CT_CONSTRUCTOR_THROW
  protected final void finalize() {
    // no-op
  }
}
