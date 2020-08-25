package ca.gc.aafc.seqdb.api.service.libraryprep;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import lombok.NonNull;

@Service
public class LibraryPrepService extends DinaService<LibraryPrep> {

  public LibraryPrepService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(LibraryPrep entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(LibraryPrep entity) {

  }

  @Override
  protected void preUpdate(LibraryPrep entity) {

  }
  
}
