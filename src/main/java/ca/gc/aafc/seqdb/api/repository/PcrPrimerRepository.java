package ca.gc.aafc.seqdb.api.repository;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import ca.gc.aafc.dina.filter.RsqlFilterHandler;
import ca.gc.aafc.dina.filter.SimpleFilterHandler;
import ca.gc.aafc.dina.repository.JpaDtoRepository;
import ca.gc.aafc.dina.repository.JpaResourceRepository;
import ca.gc.aafc.dina.repository.meta.JpaMetaInformationProvider;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;


@Component
public class PcrPrimerRepository extends JpaResourceRepository<PcrPrimerDto> {

  public PcrPrimerRepository(JpaDtoRepository dtoRepository, SimpleFilterHandler simpleFilterHandler,
      RsqlFilterHandler rsqlFilterHandler, 
      JpaMetaInformationProvider metaInformationProvider) {
    super(PcrPrimerDto.class, dtoRepository,
        Arrays.asList(simpleFilterHandler, rsqlFilterHandler),
        metaInformationProvider);
  }
}
