package ca.gc.aafc.seqdb.api.service.libraryprep;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;

import java.util.UUID;
import lombok.NonNull;

@Service
public class LibraryPrepService extends DefaultDinaService<LibraryPrep> {

  public LibraryPrepService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(LibraryPrep entity) {
    entity.setUuid(UUID.randomUUID());
  }

}
