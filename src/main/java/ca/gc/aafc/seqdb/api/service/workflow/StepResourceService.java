package ca.gc.aafc.seqdb.api.service.workflow;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.workflow.StepResource;
import lombok.NonNull;

@Service
public class StepResourceService extends DinaService<StepResource> {

  public StepResourceService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(StepResource entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(StepResource entity) {

  }

  @Override
  protected void preUpdate(StepResource entity) {

  }
  
}
