package ca.gc.aafc.seqdb.api.repository;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ca.gc.aafc.seqdb.api.dto.ContainerTypeDto;
import ca.gc.aafc.seqdb.api.repository.filter.RsqlFilterHandler;
import ca.gc.aafc.seqdb.api.repository.filter.SimpleFilterHandler;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaDtoRepository;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.meta.JpaMetaInformationProvider;
import ca.gc.aafc.seqdb.api.security.authorization.ReadableGroupFilterHandlerFactory;

@Component
public class ContainerTypeRepository extends JpaResourceRepository<ContainerTypeDto> {

  public ContainerTypeRepository(JpaDtoRepository dtoRepository,
      SimpleFilterHandler simpleFilterHandler, RsqlFilterHandler rsqlFilterHandler,
      ReadableGroupFilterHandlerFactory groupFilterFactory,
      JpaMetaInformationProvider metaInformationProvider) {
    super(ContainerTypeDto.class, dtoRepository, Arrays.asList(simpleFilterHandler, rsqlFilterHandler,
        groupFilterFactory.create(root -> root.get("group"))), metaInformationProvider);
  }
  
  @Override
  public <S extends ContainerTypeDto> S save(S resource) {
    setNumberOfWells(resource);
    return super.save(resource);
  }

  @Override
  public <S extends ContainerTypeDto> S create(S resource) {
    setNumberOfWells(resource);
    return super.create(resource);
  }
  
  private static void setNumberOfWells(ContainerTypeDto ct) {
    Integer cols = Optional.ofNullable(ct.getNumberOfColumns()).orElse(0);
    Integer rows = Optional.ofNullable(ct.getNumberOfRows()).orElse(0);
    
    ct.setNumberOfWells(cols * rows);
  }

}
