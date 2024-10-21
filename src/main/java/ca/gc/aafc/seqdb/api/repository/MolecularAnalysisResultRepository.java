package ca.gc.aafc.seqdb.api.repository;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisResultDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisResult;
import ca.gc.aafc.seqdb.api.service.MolecularAnalysisResultService;

import java.util.Optional;
import lombok.NonNull;

@Repository
public class MolecularAnalysisResultRepository extends DinaRepository<MolecularAnalysisResultDto, MolecularAnalysisResult> {

  public MolecularAnalysisResultRepository(
    @NonNull MolecularAnalysisResultService dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(MolecularAnalysisResultDto.class),
      MolecularAnalysisResultDto.class,
      MolecularAnalysisResult.class,
      null,
      externalResourceProvider,
      props, objMapper);
  }
}
