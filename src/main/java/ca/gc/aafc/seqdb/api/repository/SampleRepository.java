package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.SampleDto;
import ca.gc.aafc.seqdb.api.entities.Sample;
import lombok.NonNull;

@Repository
public class SampleRepository extends DinaRepository<SampleDto, Sample> {

  public SampleRepository(
    @NonNull DinaService<Sample> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService
  ) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(SampleDto.class),
      SampleDto.class,
      Sample.class,
      filterResolver);
  }

}
