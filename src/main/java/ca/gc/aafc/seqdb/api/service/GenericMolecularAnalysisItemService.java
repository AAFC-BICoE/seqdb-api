package ca.gc.aafc.seqdb.api.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.messaging.DinaEventPublisher;
import ca.gc.aafc.dina.messaging.EntityChanged;
import ca.gc.aafc.dina.messaging.message.DocumentOperationType;
import ca.gc.aafc.dina.service.MessageProducingService;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysisItem;

import java.util.UUID;
import lombok.NonNull;

@Service
public class GenericMolecularAnalysisItemService extends MessageProducingService<GenericMolecularAnalysisItem> {

  public static final String MESSAGE_TYPENAME = "material-sample";

  public GenericMolecularAnalysisItemService(
    @NonNull BaseDAO baseDAO, DinaEventPublisher<EntityChanged> eventPublisher, @NonNull SmartValidator sv) {
    super(baseDAO, sv, null, eventPublisher);
  }

  @Override
  protected void preCreate(GenericMolecularAnalysisItem entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void triggerEvent(GenericMolecularAnalysisItem persisted, DocumentOperationType op) {
    if (persisted.getMaterialSample() != null) {
      EntityChanged event = EntityChanged.builder()
        .op(DocumentOperationType.REFRESH)
        .resourceType(MESSAGE_TYPENAME)
        .uuid(persisted.getMaterialSample())
        .build();

      publishEvent(event);
    }
  }

}
