package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.NgsIndexDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.NgsIndex;
import lombok.NonNull;

@Repository
public class NgsIndexRepository extends DinaRepository<NgsIndexDto, NgsIndex> {

  public NgsIndexRepository(
    @NonNull DinaService<NgsIndex> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService
  ) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(NgsIndexDto.class),
      NgsIndexDto.class,
      NgsIndex.class,
      filterResolver,
      null);
  }

}
