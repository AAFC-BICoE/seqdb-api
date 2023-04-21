package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.ChainTemplateDto;
import ca.gc.aafc.seqdb.api.entities.workflow.ChainTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ChainTemplateRepository extends DinaRepository<ChainTemplateDto, ChainTemplate> {

  public ChainTemplateRepository(
    @NonNull DinaService<ChainTemplate> dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(ChainTemplateDto.class),
      ChainTemplateDto.class,
      ChainTemplate.class,
      null,
      externalResourceProvider,
      props, objMapper);
  }

}
