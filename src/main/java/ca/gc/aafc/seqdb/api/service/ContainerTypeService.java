package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import lombok.NonNull;

@Service
public class ContainerTypeService extends DefaultDinaService<ContainerType> {

  public ContainerTypeService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(ContainerType entity) {
    entity.setUuid(UUID.randomUUID());
  }
  
}
