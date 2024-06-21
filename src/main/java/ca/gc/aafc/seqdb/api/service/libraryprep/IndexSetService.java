package ca.gc.aafc.seqdb.api.service.libraryprep;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.libraryprep.IndexSet;
import lombok.NonNull;

// CHECKSTYLE:OFF NoFinalizer
// CHECKSTYLE:OFF SuperFinalize
@Service
public class IndexSetService extends DefaultDinaService<IndexSet> {

  public IndexSetService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(IndexSet entity) {
    entity.setUuid(UUID.randomUUID());
  }

  // Fixes CT_CONSTRUCTOR_THROW
  protected final void finalize() {
    // no-op
  }
}
