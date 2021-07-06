package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.StepTemplateDto;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StepTemplateRepository extends DinaRepository<StepTemplateDto, StepTemplate> {

  public StepTemplateRepository(
    @NonNull DinaService<StepTemplate> dinaService,
    Optional<DinaAuthorizationService> groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(StepTemplateDto.class),
      StepTemplateDto.class,
      StepTemplate.class,
      null,
      externalResourceProvider,
      props);
  }

}
