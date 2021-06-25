package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.service.AuditService;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PcrPrimerRepository extends DinaRepository<PcrPrimerDto, PcrPrimer> {

  public PcrPrimerRepository(
    @NonNull DinaService<PcrPrimer> dinaService,
    Optional<DinaAuthorizationService> groupAuthorizationService,
    @NonNull Optional<AuditService> auditService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider) {
    super(
      dinaService,
      groupAuthorizationService,
      auditService,
      new DinaMapper<>(PcrPrimerDto.class),
      PcrPrimerDto.class,
      PcrPrimer.class,
      null,
      externalResourceProvider,
      props);
  }

}
