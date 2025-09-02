package ca.gc.aafc.seqdb.api.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.messaging.DinaEventPublisher;
import ca.gc.aafc.dina.messaging.EntityChanged;
import ca.gc.aafc.dina.messaging.message.DocumentOperationType;
import ca.gc.aafc.dina.service.MessageProducingService;
import ca.gc.aafc.seqdb.api.dto.RunSummaryDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisResult;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import lombok.NonNull;

@Service
public class MolecularAnalysisResultService extends MessageProducingService<MolecularAnalysisResult> {

  // use the type summary since this is what is indexed
  public static final String MESSAGE_TYPENAME = RunSummaryDto.TYPENAME;

  private final MolecularAnalysisRunItemService molecularAnalysisRunItemService;

  public MolecularAnalysisResultService(
    @NonNull BaseDAO baseDAO, @NonNull SmartValidator sv,
    MolecularAnalysisRunItemService molecularAnalysisRunItemService,
    DinaEventPublisher<EntityChanged> eventPublisher) {
    super(baseDAO, sv, MESSAGE_TYPENAME, eventPublisher);
    this.molecularAnalysisRunItemService = molecularAnalysisRunItemService;
  }

  @Override
  protected void preCreate(MolecularAnalysisResult entity) {
    entity.setUuid(UUID.randomUUID());
  }

  private List<MolecularAnalysisRunItem> molecularAnalysisRunItemFromResult(UUID resultUuid) {
    // load all MolecularAnalysisRunItem where the result has the provided uuid
    return molecularAnalysisRunItemService.findAll(MolecularAnalysisRunItem.class,
      (criteriaBuilder, root, em) -> {
        CriteriaQuery<MolecularAnalysisRunItem> cq = criteriaBuilder.createQuery(MolecularAnalysisRunItem.class);
        Join<MolecularAnalysisRunItem,MolecularAnalysisResult>
          joinMolecularAnalysisResult = root.join("result", JoinType.INNER);

        return new Predicate[] {cq.where(
          criteriaBuilder.equal(joinMolecularAnalysisResult.get("uuid"),
            resultUuid)).getRestriction()};
    }, null, 0, 100, Set.of(), Set.of());
  }

  @Override
  protected void triggerEvent(MolecularAnalysisResult persisted, DocumentOperationType op) {

    // can't be linked to a summary on ADD
    if (op.equals(DocumentOperationType.ADD)) {
      return;
    }

    // load all items that has that result (usually one 1)
    List<MolecularAnalysisRunItem> items = molecularAnalysisRunItemFromResult(persisted.getUuid());

    for (MolecularAnalysisRunItem runItems : items) {
      if (runItems.getRun() != null) {
        EntityChanged event = EntityChanged.builder()
          .op(DocumentOperationType.UPDATE)
          .resourceType(MESSAGE_TYPENAME)
          .uuid(runItems.getRun().getUuid())
          .build();
        publishEvent(event);
      }
    }
  }
}
