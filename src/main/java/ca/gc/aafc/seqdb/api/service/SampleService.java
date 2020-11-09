package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.Sample;
import lombok.NonNull;

@Service
public class SampleService extends DinaService<Sample> {

  public SampleService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(Sample entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(Sample entity) {

  }

  @Override
  protected void preUpdate(Sample entity) {

  }
  
}
