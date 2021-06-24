package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.entities.PcrProfile;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ThermocyclerProfileRepository extends DinaRepository<ThermocyclerProfileDto, PcrProfile> {

  public ThermocyclerProfileRepository(
    @NonNull DinaService<PcrProfile> dinaService,
    Optional<DinaAuthorizationService> groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(ThermocyclerProfileDto.class),
      ThermocyclerProfileDto.class,
      PcrProfile.class,
      null,
      externalResourceProvider,
      props);
  }

}
