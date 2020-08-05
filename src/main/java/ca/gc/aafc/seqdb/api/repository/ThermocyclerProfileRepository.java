package ca.gc.aafc.seqdb.api.repository;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.repository.filter.RsqlFilterHandler;
import ca.gc.aafc.seqdb.api.repository.filter.SimpleFilterHandler;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaDtoRepository;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.meta.JpaMetaInformationProvider;


@Component
public class ThermocyclerProfileRepository extends JpaResourceRepository<ThermocyclerProfileDto> {

  public ThermocyclerProfileRepository(JpaDtoRepository dtoRepository, SimpleFilterHandler simpleFilterHandler,
      RsqlFilterHandler rsqlFilterHandler, 
      JpaMetaInformationProvider metaInformationProvider) {
    super(ThermocyclerProfileDto.class, dtoRepository,
        Arrays.asList(simpleFilterHandler, rsqlFilterHandler),
        metaInformationProvider);
  }

}
