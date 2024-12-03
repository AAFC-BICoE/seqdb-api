package ca.gc.aafc.seqdb.api.service;

import lombok.NonNull;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.dina.util.UUIDHelper;
import ca.gc.aafc.seqdb.api.entities.QualityControl;

@Service
public class QualityControlService  extends DefaultDinaService<QualityControl> {

  public QualityControlService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(QualityControl entity) {
    entity.setUuid(UUIDHelper.generateUUIDv7());
  }

}
