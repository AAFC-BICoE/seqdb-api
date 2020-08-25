package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.StepTemplateDto;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate;
import lombok.NonNull;

@Repository
public class StepTemplateRepository extends DinaRepository<StepTemplateDto, StepTemplate> {

  public StepTemplateRepository(
    @NonNull DinaService<StepTemplate> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService
  ) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(StepTemplateDto.class),
      StepTemplateDto.class,
      StepTemplate.class,
      filterResolver);
  }

}
