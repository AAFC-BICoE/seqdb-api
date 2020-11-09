package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.Region;
import lombok.NonNull;

@Service
public class RegionService extends DinaService<Region> {

  public RegionService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(Region entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(Region entity) {

  }

  @Override
  protected void preUpdate(Region entity) {

  }
  
}
