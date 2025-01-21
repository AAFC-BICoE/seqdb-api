package ca.gc.aafc.seqdb.api.service;

import org.springframework.stereotype.Service;

import com.github.tennaito.rsql.misc.ArgumentParser;
import com.querydsl.core.types.Ops;

import ca.gc.aafc.dina.filter.DinaFilterArgumentParser;
import ca.gc.aafc.dina.filter.FilterComponent;
import ca.gc.aafc.dina.filter.FilterExpression;
import ca.gc.aafc.dina.filter.SimpleFilterHandlerV2;
import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.seqdb.api.dto.RunSummaryDto;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysisItem;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.persistence.criteria.Predicate;

@Service
public class RunSummaryService {

  private final GenericMolecularAnalysisItemService genericMolecularAnalysisItemService;
  private final ArgumentParser rsqlArgumentParser = new DinaFilterArgumentParser();
  private final BaseDAO baseDAO;

  public RunSummaryService(
      BaseDAO baseDAO,
      @NonNull GenericMolecularAnalysisItemService genericMolecularAnalysisItemService
  ) {
    this.baseDAO = baseDAO;
    this.genericMolecularAnalysisItemService = genericMolecularAnalysisItemService;
  }

  public RunSummaryDto findSummary(String materialSampleId) {

    Set<String> includes = Set.of("genericMolecularAnalysis");
    FilterComponent fc = new FilterExpression("materialSample", Ops.EQ, materialSampleId);
    List<GenericMolecularAnalysisItem> entities = genericMolecularAnalysisItemService.findAll(
      GenericMolecularAnalysisItem.class,
      (criteriaBuilder, root, em) -> {
        Predicate restriction =
          SimpleFilterHandlerV2.getRestriction(root, criteriaBuilder, rsqlArgumentParser::parse,
            em.getMetamodel(), List.of(fc));
        return new Predicate[] {restriction};
      },
      (cb, root) -> List.of(), 0, 100, includes, includes);


    Map<UUID, RunSummaryDto.MolecularAnalysisRunSummary> uniqueGenericMolecularAnalysis =
      new HashMap<>();
    for (GenericMolecularAnalysisItem item : entities) {
      uniqueGenericMolecularAnalysis.putIfAbsent(item.getGenericMolecularAnalysis().getUuid(),
        new RunSummaryDto.MolecularAnalysisRunSummary(item.getGenericMolecularAnalysis().getUuid(),
          item.getGenericMolecularAnalysis().getName(), List.of()));
    }

    return RunSummaryDto.builder()
      .id(materialSampleId)
      .molecularAnalysisRunSummaries(uniqueGenericMolecularAnalysis.values())
      .build();
  }
}
