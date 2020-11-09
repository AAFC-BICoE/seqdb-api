package ca.gc.aafc.seqdb.api.service.workflow;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.workflow.ChainStepTemplate;
import lombok.NonNull;

@Service
public class ChainStepTemplateService extends DinaService<ChainStepTemplate> {

  public ChainStepTemplateService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(ChainStepTemplate entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(ChainStepTemplate entity) {

  }

  @Override
  protected void preUpdate(ChainStepTemplate entity) {

  }
  
}
