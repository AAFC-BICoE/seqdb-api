package ca.gc.aafc.seqdb.api.service;

import lombok.NonNull;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.dina.util.UUIDHelper;
import ca.gc.aafc.seqdb.api.entities.QualityControl;
import ca.gc.aafc.seqdb.api.validation.QualityControlVocabularyValidator;

@Service
public class QualityControlService extends DefaultDinaService<QualityControl> {

  private final QualityControlVocabularyValidator qualityControlVocabularyValidator;

  public QualityControlService(
    @NonNull BaseDAO baseDAO,
    QualityControlVocabularyValidator qualityControlVocabularyValidator,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
    this.qualityControlVocabularyValidator = qualityControlVocabularyValidator;
  }

  @Override
  protected void preCreate(QualityControl entity) {
    entity.setUuid(UUIDHelper.generateUUIDv7());
  }

  @Override
  public void validateBusinessRules(QualityControl entity) {
    applyBusinessRule(entity, qualityControlVocabularyValidator);
  }

}
