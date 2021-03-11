package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.PreLibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PreLibraryPrepRepository extends DinaRepository<PreLibraryPrepDto, PreLibraryPrep> {

  public PreLibraryPrepRepository(
    @NonNull DinaService<PreLibraryPrep> dinaService,
    Optional<DinaAuthorizationService> authService,
    @NonNull BuildProperties props) {
    super(
      dinaService,
      // Make an exception and allow creates when the group is null:
      authService.map(auth -> new DinaAuthorizationService() {
        public void authorizeCreate(Object entity) { };
        public void authorizeDelete(Object entity) {
          auth.authorizeDelete(entity);
        };
        public void authorizeUpdate(Object entity) {
          auth.authorizeUpdate(entity);
        };
      }),
      Optional.empty(),
      new DinaMapper<>(PreLibraryPrepDto.class),
      PreLibraryPrepDto.class,
      PreLibraryPrep.class,
      null,
      null,
      props);
  }

}
