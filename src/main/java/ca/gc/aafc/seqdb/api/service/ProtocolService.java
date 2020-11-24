package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.Protocol;
import lombok.NonNull;

@Service
public class ProtocolService extends DefaultDinaService<Protocol> {

  public ProtocolService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(Protocol entity) {
    entity.setUuid(UUID.randomUUID());
  }
  
}
