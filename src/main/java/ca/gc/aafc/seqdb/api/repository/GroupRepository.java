package ca.gc.aafc.seqdb.api.repository;

import java.util.Arrays;

import javax.persistence.criteria.Path;

import org.springframework.stereotype.Component;

import ca.gc.aafc.seqdb.api.dto.GroupDto;
import ca.gc.aafc.seqdb.api.repository.filter.RsqlFilterHandler;
import ca.gc.aafc.seqdb.api.repository.filter.SimpleFilterHandler;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaDtoRepository;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.meta.JpaMetaInformationProvider;
import ca.gc.aafc.seqdb.api.security.authorization.ReadableGroupFilterHandlerFactory;
import ca.gc.aafc.seqdb.entities.Group;
import lombok.NonNull;

@Component
public class GroupRepository extends JpaResourceRepository<GroupDto> {

  public GroupRepository(@NonNull JpaDtoRepository dtoRepository, SimpleFilterHandler simpleFilterHandler,
      RsqlFilterHandler rsqlFilterHandler, ReadableGroupFilterHandlerFactory groupFilterFactory,
      JpaMetaInformationProvider metaInformationProvider) {
    super(GroupDto.class, dtoRepository,
        Arrays.asList(simpleFilterHandler, rsqlFilterHandler, groupFilterFactory.create(root -> (Path<Group>) root)),
        metaInformationProvider);
  }

}
