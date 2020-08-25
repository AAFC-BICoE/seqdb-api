package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.ChainStepTemplateDto;
import ca.gc.aafc.seqdb.api.entities.workflow.ChainStepTemplate;
import lombok.NonNull;

@Repository
public class ChainStepTemplateRepository extends DinaRepository<ChainStepTemplateDto, ChainStepTemplate> {

  public ChainStepTemplateRepository(
    @NonNull DinaService<ChainStepTemplate> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService
  ) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(ChainStepTemplateDto.class),
      ChainStepTemplateDto.class,
      ChainStepTemplate.class,
      filterResolver);
  }

}
