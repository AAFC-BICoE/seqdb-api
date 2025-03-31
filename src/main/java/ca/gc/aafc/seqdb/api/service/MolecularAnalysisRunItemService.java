package ca.gc.aafc.seqdb.api.service;

import java.util.UUID;
import lombok.NonNull;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.messaging.DinaEventPublisher;
import ca.gc.aafc.dina.messaging.EntityChanged;
import ca.gc.aafc.dina.messaging.message.DocumentOperationType;
import ca.gc.aafc.dina.service.MessageProducingService;
import ca.gc.aafc.seqdb.api.dto.RunSummaryDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;

@Service
public class MolecularAnalysisRunItemService extends MessageProducingService<MolecularAnalysisRunItem> {

  // use the type summary since this is what is indexed
  public static final String MESSAGE_TYPENAME = RunSummaryDto.TYPENAME;

  public MolecularAnalysisRunItemService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv,
    DinaEventPublisher<EntityChanged> eventPublisher) {
    super(baseDAO, sv, null, eventPublisher);
  }

  @Override
  protected void preCreate(MolecularAnalysisRunItem entity) {
    entity.setUuid(UUID.randomUUID());
  }

  @Override
  protected void triggerEvent(MolecularAnalysisRunItem persisted, DocumentOperationType op) {
    if (persisted.getRun() != null) {
      EntityChanged event = EntityChanged.builder()
        .op(DocumentOperationType.UPDATE)
        .resourceType(MESSAGE_TYPENAME)
        .uuid(persisted.getRun().getUuid())
        .build();

      publishEvent(event);
    }
  }
}
