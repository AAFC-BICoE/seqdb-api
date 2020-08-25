package ca.gc.aafc.seqdb.api.service.libraryprep;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.libraryprep.IndexSet;
import lombok.NonNull;

@Service
public class IndexSetService extends DinaService<IndexSet> {

  public IndexSetService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(IndexSet entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(IndexSet entity) {

  }

  @Override
  protected void preUpdate(IndexSet entity) {

  }
  
}
