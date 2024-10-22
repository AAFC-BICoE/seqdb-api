package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;
import lombok.NonNull;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisResult;

@Service
public class MolecularAnalysisResultService extends DefaultDinaService<MolecularAnalysisResult> {

  public MolecularAnalysisResultService(
    @NonNull BaseDAO baseDAO, @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(MolecularAnalysisResult entity) {
    entity.setUuid(UUID.randomUUID());
  }

}

