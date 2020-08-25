package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.entities.PcrProfile;
import lombok.NonNull;

@Repository
public class ThermocyclerProfileRepository extends DinaRepository<ThermocyclerProfileDto, PcrProfile> {

  public ThermocyclerProfileRepository(
    @NonNull DinaService<PcrProfile> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService
  ) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(ThermocyclerProfileDto.class),
      ThermocyclerProfileDto.class,
      PcrProfile.class,
      filterResolver);
  }

}
