package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;
import lombok.NonNull;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;
import ca.gc.aafc.seqdb.api.validation.SequenceManagedAttributeValueValidator;

@Service
public class GenericMolecularAnalysisService extends DefaultDinaService<GenericMolecularAnalysis> {

  private static final SequenceManagedAttributeValueValidator.SequenceManagedAttributeValidationContext
    GENERIC_MOLECULAR_ANALYSIS_VALIDATION_CONTEXT =
    SequenceManagedAttributeValueValidator.SequenceManagedAttributeValidationContext
      .from(SequenceManagedAttribute.ManagedAttributeComponent.GENERIC_MOLECULAR_ANALYSIS);

  private final SequenceManagedAttributeValueValidator sequenceManagedAttributeValueValidator;

  public GenericMolecularAnalysisService(
    @NonNull BaseDAO baseDAO,
    @NonNull SequenceManagedAttributeValueValidator sequenceManagedAttributeValueValidator,
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
    validateManagedAttribute(entity);
  }

  private void validateManagedAttribute(GenericMolecularAnalysis genericMolecularAnalysis) {
    if (MapUtils.isNotEmpty(genericMolecularAnalysis.getManagedAttributes())) {
      sequenceManagedAttributeValueValidator.validate(genericMolecularAnalysis,
        genericMolecularAnalysis.getManagedAttributes(),
        GENERIC_MOLECULAR_ANALYSIS_VALIDATION_CONTEXT);
    }
  }

}
