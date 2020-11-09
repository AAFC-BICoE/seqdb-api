package ca.gc.aafc.seqdb.api.service.libraryprep;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import lombok.NonNull;

@Service
public class LibraryPrepBatchService extends DinaService<LibraryPrepBatch> {

  public LibraryPrepBatchService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(LibraryPrepBatch entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(LibraryPrepBatch entity) {

  }

  @Override
  protected void preUpdate(LibraryPrepBatch entity) {

  }
  
}
