package ca.gc.aafc.seqdb.api.service.workflow;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate;
import lombok.NonNull;

@Service
public class StepTemplateService extends DinaService<StepTemplate> {

  public StepTemplateService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(StepTemplate entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(StepTemplate entity) {

  }

  @Override
  protected void preUpdate(StepTemplate entity) {

  }
  
}
