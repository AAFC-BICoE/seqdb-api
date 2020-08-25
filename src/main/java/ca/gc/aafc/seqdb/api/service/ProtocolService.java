package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.Protocol;
import lombok.NonNull;

@Service
public class ProtocolService extends DinaService<Protocol> {

  public ProtocolService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(Protocol entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(Protocol entity) {

  }

  @Override
  protected void preUpdate(Protocol entity) {

  }
  
}
