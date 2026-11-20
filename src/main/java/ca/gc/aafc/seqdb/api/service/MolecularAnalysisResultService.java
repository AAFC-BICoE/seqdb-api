package ca.gc.aafc.seqdb.api.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.messaging.DinaEventPublisher;
import ca.gc.aafc.dina.messaging.EntityChanged;
import ca.gc.aafc.dina.messaging.message.DocumentOperationType;
import ca.gc.aafc.dina.service.MessageProducingService;
import ca.gc.aafc.seqdb.api.dto.RunSummaryDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisResult;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.NonNull;

@Service
public class MolecularAnalysisResultService extends MessageProducingService<MolecularAnalysisResult> {

  // use the type summary since this is what is indexed
  public static final String MESSAGE_TYPENAME = RunSummaryDto.TYPENAME;

  private static final String RUN_UUIDS_FROM_RESULT_QUERY = """
    select distinct item.run.uuid
    from MolecularAnalysisRunItem item
    where item.result.uuid = :resultUuid
      and item.run is not null
    """;

  private final BaseDAO baseDAO;

  public MolecularAnalysisResultService(
    @NonNull BaseDAO baseDAO, @NonNull SmartValidator sv,
    DinaEventPublisher<EntityChanged> eventPublisher) {
    super(baseDAO, sv, MESSAGE_TYPENAME, eventPublisher);
    this.baseDAO = baseDAO;
  }

  @Override
  protected void preCreate(MolecularAnalysisResult entity) {
    entity.setUuid(UUID.randomUUID());
  }

  /**
   * Streams the uuid of every distinct MolecularAnalysisRun that has an item linked to the result.
   * The caller is responsible for consuming the stream within the current transaction.
   */
  private Stream<UUID> runUuidsFromResult(UUID resultUuid) {
    return baseDAO.streamAllByQuery(UUID.class, RUN_UUIDS_FROM_RESULT_QUERY,
      List.of(Pair.of("resultUuid", resultUuid)));
  }

  @Override
  protected void triggerEvent(MolecularAnalysisResult persisted, DocumentOperationType op) {

    // can't be linked to a summary on ADD
    if (op.equals(DocumentOperationType.ADD)) {
      return;
    }

    // try-with-resources to release the underlying query resources
    try (Stream<UUID> runUuids = runUuidsFromResult(persisted.getUuid())) {
      runUuids.forEach(runUuid -> publishEvent(EntityChanged.builder()
        .op(DocumentOperationType.UPDATE)
        .resourceType(MESSAGE_TYPENAME)
        .uuid(runUuid)
        .build()));
    }
  }
}
