package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.ChainStepTemplateDto;
import ca.gc.aafc.seqdb.api.entities.workflow.ChainStepTemplate;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ChainStepTemplateRepository extends DinaRepository<ChainStepTemplateDto, ChainStepTemplate> {

  public ChainStepTemplateRepository(
    @NonNull DinaService<ChainStepTemplate> dinaService,
    Optional<DinaAuthorizationService> groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(ChainStepTemplateDto.class),
      ChainStepTemplateDto.class,
      ChainStepTemplate.class,
      null,
      externalResourceProvider,
      props);
  }

}
