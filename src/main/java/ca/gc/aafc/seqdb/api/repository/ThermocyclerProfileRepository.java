package ca.gc.aafc.seqdb.api.repository;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.repository.filter.FilterHandler;
import ca.gc.aafc.seqdb.api.repository.filter.RsqlFilterHandler;
import ca.gc.aafc.seqdb.api.repository.filter.SimpleFilterHandler;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaDtoRepository;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.meta.JpaMetaInformationProvider;
import ca.gc.aafc.seqdb.api.security.authorization.ReadableGroupFilterHandlerFactory;
import lombok.NonNull;

@Component
public class ThermocyclerProfileRepository extends JpaResourceRepository<ThermocyclerProfileDto> {

  public ThermocyclerProfileRepository(@NonNull JpaDtoRepository dtoRepository, SimpleFilterHandler simpleFilterHandler,
      RsqlFilterHandler rsqlFilterHandler, ReadableGroupFilterHandlerFactory groupFilterFactory,
      JpaMetaInformationProvider metaInformationProvider) {
    super(ThermocyclerProfileDto.class, dtoRepository,
        Arrays.asList(simpleFilterHandler, rsqlFilterHandler, groupFilterFactory.create(root -> root.get("group"))),
        metaInformationProvider);
  }

}
