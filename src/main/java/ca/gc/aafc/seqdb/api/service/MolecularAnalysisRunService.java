package ca.gc.aafc.seqdb.api.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;

import java.util.UUID;
import lombok.NonNull;

@Service
public class MolecularAnalysisRunService extends DefaultDinaService<MolecularAnalysisRun> {

  public MolecularAnalysisRunService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(MolecularAnalysisRun entity) {
    entity.setUuid(UUID.randomUUID());
  }

}
