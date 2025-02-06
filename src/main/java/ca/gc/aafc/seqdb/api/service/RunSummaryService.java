package ca.gc.aafc.seqdb.api.service;

import org.springframework.stereotype.Service;

import com.github.tennaito.rsql.misc.ArgumentParser;

import ca.gc.aafc.dina.filter.DinaFilterArgumentParser;
import ca.gc.aafc.dina.filter.FilterExpression;
import ca.gc.aafc.dina.filter.SimpleFilterHandlerV2;
import ca.gc.aafc.seqdb.api.dto.RunSummaryDto;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysisItem;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.persistence.criteria.Predicate;
import lombok.NonNull;

@Service
public class RunSummaryService {

  private static final Set<String> RELATIONSHIPS_TO_LOAD = Set.of(
    "genericMolecularAnalysis", "molecularAnalysisRunItem", "molecularAnalysisRunItem.result");
  private static final ArgumentParser RSQL_ARGUMENT_PARSER = new DinaFilterArgumentParser();

  private final GenericMolecularAnalysisItemService genericMolecularAnalysisItemService;

  public RunSummaryService(@NonNull GenericMolecularAnalysisItemService genericMolecularAnalysisItemService) {
    this.genericMolecularAnalysisItemService = genericMolecularAnalysisItemService;
  }

  public List<RunSummaryDto> findSummary(FilterExpression filterExpression) {

    List<GenericMolecularAnalysisItem> entities = genericMolecularAnalysisItemService.findAll(
      GenericMolecularAnalysisItem.class,
      (criteriaBuilder, root, em) -> {
        Predicate restriction =
          SimpleFilterHandlerV2.getRestriction(root, criteriaBuilder, RSQL_ARGUMENT_PARSER::parse,
            em.getMetamodel(), List.of(filterExpression));
        return new Predicate[] {restriction};
      },
      (cb, root) -> List.of(), 0, 100, Set.of(), RELATIONSHIPS_TO_LOAD);

    // Collect all GenericMolecularAnalysis
    Map<UUID, RunSummaryDto.RunSummaryDtoBuilder> uniqueGenericMolecularAnalysisRun = new HashMap<>(entities.size());
    for (GenericMolecularAnalysisItem item : entities) {
      // start with the MolecularAnalysisRun
      if (hasAnalysisRunItemAndRun(item)) {
        MolecularAnalysisRun molecularAnalysisRun = item.getMolecularAnalysisRunItem().getRun();
        var builder =
          uniqueGenericMolecularAnalysisRun.computeIfAbsent(molecularAnalysisRun.getUuid(),
            u -> initBuilder(molecularAnalysisRun));
        builder.item(createRunSummaryItem(item.getMolecularAnalysisRunItem(), item,
          item.getGenericMolecularAnalysis()));
      }
    }

    return uniqueGenericMolecularAnalysisRun.values().stream().map(
      RunSummaryDto.RunSummaryDtoBuilder::build).toList();
  }

  /**
   * Make sure the {@link GenericMolecularAnalysisItem} has a {@link MolecularAnalysisRunItem}
   * and an associated {@link MolecularAnalysisRun}
   * @param gmari
   * @return
   */
  private static boolean hasAnalysisRunItemAndRun(GenericMolecularAnalysisItem gmari) {
    return gmari.getMolecularAnalysisRunItem() != null &&
      gmari.getMolecularAnalysisRunItem().getRun() != null;
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

    RunSummaryDto.MolecularAnalysisResultSummary molecularAnalysisResultSummary =
      molecularAnalysisRunItem.getResult() != null ?
        new RunSummaryDto.MolecularAnalysisResultSummary(
          molecularAnalysisRunItem.getResult().getUuid(), "") : null;

    return new RunSummaryDto.RunSummaryItem(molecularAnalysisRunItem.getUuid(),
      molecularAnalysisResultSummary, new RunSummaryDto.GenericMolecularAnalysisItemSummary(genericMolecularAnalysisItem.getUuid(),
        molecularAnalysisRunItem.getName(),
        new RunSummaryDto.GenericMolecularAnalysisSummary(genericMolecularAnalysis.getUuid(),
          genericMolecularAnalysis.getName(), genericMolecularAnalysis.getAnalysisType())));
  }
}
