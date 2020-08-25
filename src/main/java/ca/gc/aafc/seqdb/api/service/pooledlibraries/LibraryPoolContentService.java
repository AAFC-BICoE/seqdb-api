package ca.gc.aafc.seqdb.api.service.pooledlibraries;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPoolContent;
import lombok.NonNull;

@Service
public class LibraryPoolContentService extends DinaService<LibraryPoolContent> {

  public LibraryPoolContentService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(LibraryPoolContent entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(LibraryPoolContent entity) {

  }

  @Override
  protected void preUpdate(LibraryPoolContent entity) {

  }
  
}
