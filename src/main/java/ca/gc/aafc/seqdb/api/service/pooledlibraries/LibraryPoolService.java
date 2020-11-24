package ca.gc.aafc.seqdb.api.service.pooledlibraries;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import lombok.NonNull;

@Service
public class LibraryPoolService extends DefaultDinaService<LibraryPool> {

  public LibraryPoolService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(LibraryPool entity) {
    entity.setUuid(UUID.randomUUID());
  }
  
}
