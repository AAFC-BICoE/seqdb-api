package ca.gc.aafc.seqdb.api.service.workflow;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.workflow.ChainStepTemplate;
import lombok.NonNull;

@Service
public class ChainStepTemplateService extends DefaultDinaService<ChainStepTemplate> {

  public ChainStepTemplateService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(ChainStepTemplate entity) {
    entity.setUuid(UUID.randomUUID());
  }
  
}
