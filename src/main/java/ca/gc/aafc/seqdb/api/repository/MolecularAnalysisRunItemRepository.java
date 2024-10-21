package ca.gc.aafc.seqdb.api.repository;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;
import ca.gc.aafc.seqdb.api.service.MolecularAnalysisRunItemService;

import java.util.Optional;
import lombok.NonNull;

@Repository
public class MolecularAnalysisRunItemRepository extends DinaRepository<MolecularAnalysisRunItemDto, MolecularAnalysisRunItem> {

  public MolecularAnalysisRunItemRepository(
    @NonNull MolecularAnalysisRunItemService dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(MolecularAnalysisRunItemDto.class),
      MolecularAnalysisRunItemDto.class,
      MolecularAnalysisRunItem.class,
      null,
      externalResourceProvider,
      props, objMapper);
  }

}

