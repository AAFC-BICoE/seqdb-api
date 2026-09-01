package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;
import ca.gc.aafc.seqdb.api.validation.SequenceManagedAttributeValueValidatorGenericMolecularAnalysis;
import lombok.NonNull;

@Service
public class GenericMolecularAnalysisService extends DefaultDinaService<GenericMolecularAnalysis> {

  private final SequenceManagedAttributeValueValidatorGenericMolecularAnalysis sequenceManagedAttributeValueValidator;

  public GenericMolecularAnalysisService(
    @NonNull BaseDAO baseDAO,
    @NonNull SequenceManagedAttributeValueValidatorGenericMolecularAnalysis sequenceManagedAttributeValueValidator,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);

    this.sequenceManagedAttributeValueValidator = sequenceManagedAttributeValueValidator;
  }

  @Override
  protected void preCreate(GenericMolecularAnalysis entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  public void validateBusinessRules(GenericMolecularAnalysis entity) {
    sequenceManagedAttributeValueValidator.validate(entity, entity.getManagedAttributes());
  }

}
