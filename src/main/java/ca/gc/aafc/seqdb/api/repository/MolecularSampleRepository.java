package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.MolecularSampleDto;
import ca.gc.aafc.seqdb.api.entities.MolecularSample;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MolecularSampleRepository extends DinaRepository<MolecularSampleDto, MolecularSample> {

  public MolecularSampleRepository(
    @NonNull DinaService<MolecularSample> dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(MolecularSampleDto.class),
      MolecularSampleDto.class,
      MolecularSample.class,
      null,
      externalResourceProvider,
      props);
  }

}
