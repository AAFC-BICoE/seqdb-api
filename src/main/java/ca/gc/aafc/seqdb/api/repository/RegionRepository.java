package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.entities.Region;
import lombok.NonNull;

@Repository
public class RegionRepository extends DinaRepository<RegionDto, Region> {

  public RegionRepository(
    @NonNull DinaService<Region> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService
  ) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(RegionDto.class),
      RegionDto.class,
      Region.class,
      filterResolver);
  }

}
