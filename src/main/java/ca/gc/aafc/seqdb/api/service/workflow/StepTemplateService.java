package ca.gc.aafc.seqdb.api.service.workflow;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate;
import lombok.NonNull;

@Service
public class StepTemplateService extends DefaultDinaService<StepTemplate> {

  public StepTemplateService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(StepTemplate entity) {
    entity.setUuid(UUID.randomUUID());
  }

}
