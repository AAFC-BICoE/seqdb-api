package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.PreLibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PreLibraryPrepRepository extends DinaRepository<PreLibraryPrepDto, PreLibraryPrep> {

  public PreLibraryPrepRepository(
    @NonNull DinaService<PreLibraryPrep> dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    ObjectMapper objMapper) {
    super(
      dinaService,
      // Make an exception and allow creates when the group is null:
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(PreLibraryPrepDto.class),
      PreLibraryPrepDto.class,
      PreLibraryPrep.class,
      null,
      externalResourceProvider,
      props, objMapper);
  }

}
