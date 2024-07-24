package ca.gc.aafc.seqdb.api.service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;

import ca.gc.aafc.seqdb.api.entities.SequencingFacility;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import java.util.UUID;

@Service
public class SequencingFacilityService extends DefaultDinaService<SequencingFacility> {

  public SequencingFacilityService(
          @NonNull BaseDAO baseDAO,
          @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(SequencingFacility entity) {
    entity.setUuid(UUID.randomUUID());
  }

}
