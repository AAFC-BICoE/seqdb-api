package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.Region;
import lombok.NonNull;

@Service
public class RegionService extends DefaultDinaService<Region> {

  public RegionService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(Region entity) {
    entity.setUuid(UUID.randomUUID());
  }
  
}
