package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;
import lombok.NonNull;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysisItem;

@Service
public class GenericMolecularAnalysisItemService extends DefaultDinaService<GenericMolecularAnalysisItem> {

  public GenericMolecularAnalysisItemService(
    @NonNull BaseDAO baseDAO, @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(GenericMolecularAnalysisItem entity) {
    entity.setUuid(UUID.randomUUID());
  }

}
