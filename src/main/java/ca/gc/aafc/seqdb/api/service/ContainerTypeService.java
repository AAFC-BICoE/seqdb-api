package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import lombok.NonNull;

@Service
public class ContainerTypeService extends DinaService<ContainerType> {

  public ContainerTypeService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(ContainerType entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(ContainerType entity) {

  }

  @Override
  protected void preUpdate(ContainerType entity) {

  }
  
}
