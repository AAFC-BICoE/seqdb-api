package ca.gc.aafc.seqdb.api.repository;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import ca.gc.aafc.seqdb.api.dto.ContainerDto;
import ca.gc.aafc.seqdb.api.repository.filter.RsqlFilterHandler;
import ca.gc.aafc.seqdb.api.repository.filter.SimpleFilterHandler;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaDtoRepository;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.meta.JpaMetaInformationProvider;
import ca.gc.aafc.seqdb.api.security.authorization.ReadableGroupFilterHandlerFactory;

@Component
public class ContainerRepository extends JpaResourceRepository<ContainerDto> {

  public ContainerRepository(JpaDtoRepository dtoRepository,
      SimpleFilterHandler simpleFilterHandler, RsqlFilterHandler rsqlFilterHandler,
      ReadableGroupFilterHandlerFactory groupFilterFactory,
      JpaMetaInformationProvider metaInformationProvider) {
    super(ContainerDto.class, dtoRepository, Arrays.asList(simpleFilterHandler, rsqlFilterHandler,
        groupFilterFactory.create(root -> root.get("group"))), metaInformationProvider);
  }

}
