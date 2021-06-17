package ca.gc.aafc.seqdb.api.service.libraryprep;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.libraryprep.NgsIndex;
import lombok.NonNull;

@Service
public class NgsIndexService extends DefaultDinaService<NgsIndex> {

  public NgsIndexService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(NgsIndex entity) {
    entity.setUuid(UUID.randomUUID());
  }
  
}
