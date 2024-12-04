package ca.gc.aafc.seqdb.api.service;

import lombok.NonNull;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.dina.util.UUIDHelper;
import ca.gc.aafc.seqdb.api.entities.MetagenomicsBatch;

@Service
public class MetagenomicsBatchService extends DefaultDinaService<MetagenomicsBatch> {

  public MetagenomicsBatchService(
    @NonNull BaseDAO baseDAO, @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(MetagenomicsBatch entity) {
    entity.setUuid(UUIDHelper.generateUUIDv7());
  }

}
