package ca.gc.aafc.seqdb.api.service.workflow;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.workflow.ChainTemplate;
import lombok.NonNull;

@Service
public class ChainTemplateService extends DinaService<ChainTemplate> {

  public ChainTemplateService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(ChainTemplate entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(ChainTemplate entity) {

  }

  @Override
  protected void preUpdate(ChainTemplate entity) {

  }
  
}
