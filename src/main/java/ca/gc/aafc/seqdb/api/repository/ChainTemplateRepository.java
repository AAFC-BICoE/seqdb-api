package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.ChainTemplateDto;
import ca.gc.aafc.seqdb.api.entities.workflow.ChainTemplate;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ChainTemplateRepository extends DinaRepository<ChainTemplateDto, ChainTemplate> {

  public ChainTemplateRepository(
    @NonNull DinaService<ChainTemplate> dinaService,
    Optional<DinaAuthorizationService> authService,
    @NonNull BuildProperties props) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(ChainTemplateDto.class),
      ChainTemplateDto.class,
      ChainTemplate.class,
      null,
      null,
      props);
  }

}
