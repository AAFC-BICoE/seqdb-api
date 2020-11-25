package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.AuditService;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import lombok.NonNull;

@Repository
public class PcrPrimerRepository extends DinaRepository<PcrPrimerDto, PcrPrimer> {

  public PcrPrimerRepository(
    @NonNull DinaService<PcrPrimer> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService,
    @NonNull Optional<AuditService> auditService
  ) {
    super(
      dinaService,
      authService,
      auditService,
      new DinaMapper<>(PcrPrimerDto.class),
      PcrPrimerDto.class,
      PcrPrimer.class,
      filterResolver,
      null);
  }

}
