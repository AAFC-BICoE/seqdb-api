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
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysisItem;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;

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

  public List<RunSummaryDto> findSummary(FilterExpression filterExpression) {

    Set<String> includes = Set.of("genericMolecularAnalysis", "molecularAnalysisRunItem");
    Set<String> relationships = Set.of("genericMolecularAnalysis", "molecularAnalysisRunItem",
        "molecularAnalysisRunItem.result");
    List<GenericMolecularAnalysisItem> entities = genericMolecularAnalysisItemService.findAll(
      GenericMolecularAnalysisItem.class,
      (criteriaBuilder, root, em) -> {
        Predicate restriction =
          SimpleFilterHandlerV2.getRestriction(root, criteriaBuilder, rsqlArgumentParser::parse,
            em.getMetamodel(), List.of(filterExpression));
        return new Predicate[] {restriction};
      },
      (cb, root) -> List.of(), 0, 100, includes, relationships);

    // Collect all GenericMolecularAnalysis
    Map<UUID, RunSummaryDto.RunSummaryDtoBuilder> uniqueGenericMolecularAnalysisRun = new HashMap<>();
    for (GenericMolecularAnalysisItem item : entities) {
      // start with the MolecularAnalysisRun
      MolecularAnalysisRun molecularAnalysisRun = item.getMolecularAnalysisRunItem().getRun();
      var builder = uniqueGenericMolecularAnalysisRun.computeIfAbsent(molecularAnalysisRun.getUuid(), (u)-> initBuilder(molecularAnalysisRun));
      builder.item(createRunSummaryItem(item.getMolecularAnalysisRunItem(), item, item.getGenericMolecularAnalysis()));
    }

    return uniqueGenericMolecularAnalysisRun.values().stream().map(
      RunSummaryDto.RunSummaryDtoBuilder::build).toList();
  }

  private static RunSummaryDto.RunSummaryDtoBuilder initBuilder(MolecularAnalysisRun molecularAnalysisRun) {
    return RunSummaryDto.builder()
      .id(molecularAnalysisRun.getUuid())
      .name(molecularAnalysisRun.getName());
  }

  private static RunSummaryDto.RunSummaryItem createRunSummaryItem(
    MolecularAnalysisRunItem molecularAnalysisRunItem,
    GenericMolecularAnalysisItem genericMolecularAnalysisItem,
    GenericMolecularAnalysis genericMolecularAnalysis) {

    return new RunSummaryDto.RunSummaryItem(molecularAnalysisRunItem.getUuid(),
      new RunSummaryDto.MolecularAnalysisResultSummary(
        molecularAnalysisRunItem.getResult().getUuid(), ""),
      new RunSummaryDto.GenericMolecularAnalysisItemSummary(genericMolecularAnalysisItem.getUuid(),
        molecularAnalysisRunItem.getName(),
        new RunSummaryDto.GenericMolecularAnalysisSummary(genericMolecularAnalysis.getUuid(),
          genericMolecularAnalysis.getName(), genericMolecularAnalysis.getAnalysisType())));
  }
}
