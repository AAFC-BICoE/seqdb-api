package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.sanger.PcrBatch;
import lombok.NonNull;

@Service
public class PcrBatchService extends DefaultDinaService<PcrBatch> {
  
  public PcrBatchService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(PcrBatch entity) {
    entity.setUuid(UUID.randomUUID());
  }

}
