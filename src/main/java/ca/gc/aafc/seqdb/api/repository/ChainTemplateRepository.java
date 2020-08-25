package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.ChainTemplateDto;
import ca.gc.aafc.seqdb.api.entities.workflow.ChainTemplate;
import lombok.NonNull;

@Repository
public class ChainTemplateRepository extends DinaRepository<ChainTemplateDto, ChainTemplate> {

  public ChainTemplateRepository(
    @NonNull DinaService<ChainTemplate> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService
  ) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(ChainTemplateDto.class),
      ChainTemplateDto.class,
      ChainTemplate.class,
      filterResolver);
  }

}
