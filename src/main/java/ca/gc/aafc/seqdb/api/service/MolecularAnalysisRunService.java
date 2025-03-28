package ca.gc.aafc.seqdb.api.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.messaging.DinaEventPublisher;
import ca.gc.aafc.dina.messaging.EntityChanged;
import ca.gc.aafc.dina.service.MessageProducingService;
import ca.gc.aafc.seqdb.api.dto.RunSummaryDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;

import java.util.UUID;
import lombok.NonNull;

@Service
public class MolecularAnalysisRunService extends MessageProducingService<MolecularAnalysisRun> {

  // use the type summary since this is what is indexed
  public static final String MESSAGE_TYPENAME = RunSummaryDto.TYPENAME;

  public MolecularAnalysisRunService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv,
    DinaEventPublisher<EntityChanged> eventPublisher) {
    super(baseDAO, sv, MESSAGE_TYPENAME, eventPublisher);
  }

  @Override
  protected void preCreate(MolecularAnalysisRun entity) {
    entity.setUuid(UUID.randomUUID());
  }

}
