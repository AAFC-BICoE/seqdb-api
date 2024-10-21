package ca.gc.aafc.seqdb.api.repository;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;
import ca.gc.aafc.seqdb.api.service.MolecularAnalysisRunService;

import java.util.Optional;
import lombok.NonNull;

@Repository
public class MolecularAnalysisRunRepository extends DinaRepository<MolecularAnalysisRunDto, MolecularAnalysisRun> {

  public MolecularAnalysisRunRepository(
    @NonNull MolecularAnalysisRunService dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(MolecularAnalysisRunDto.class),
      MolecularAnalysisRunDto.class,
      MolecularAnalysisRun.class,
      null,
      externalResourceProvider,
      props, objMapper);
  }

}

