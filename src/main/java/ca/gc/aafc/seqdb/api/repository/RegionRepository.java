package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.service.AuditService;
import ca.gc.aafc.dina.security.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.entities.Region;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RegionRepository extends DinaRepository<RegionDto, Region> {

  public RegionRepository(
    @NonNull DinaService<Region> dinaService,
    Optional<DinaAuthorizationService> groupAuthorizationService,
    @NonNull Optional<AuditService> auditService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider) {
    super(
      dinaService,
      groupAuthorizationService,
      auditService,
      new DinaMapper<>(RegionDto.class),
      RegionDto.class,
      Region.class,
      null,
      externalResourceProvider,
      props);
  }

}
