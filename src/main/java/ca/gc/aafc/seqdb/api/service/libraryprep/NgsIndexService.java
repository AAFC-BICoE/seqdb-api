package ca.gc.aafc.seqdb.api.service.libraryprep;

import java.util.UUID;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.libraryprep.NgsIndex;
import lombok.NonNull;

@Service
public class NgsIndexService extends DinaService<NgsIndex> {

  public NgsIndexService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
  }

  @Override
  protected void preCreate(NgsIndex entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void preDelete(NgsIndex entity) {

  }

  @Override
  protected void preUpdate(NgsIndex entity) {

  }
  
}
