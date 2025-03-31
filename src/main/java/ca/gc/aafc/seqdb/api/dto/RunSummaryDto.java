package ca.gc.aafc.seqdb.api.dto;

import java.util.List;
import java.util.UUID;

import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;


/**
 * Store the summary information about a run and its related information
 */
@AllArgsConstructor
@Getter
@Builder
@JsonApiTypeForClass(RunSummaryDto.TYPENAME)
public class RunSummaryDto {

  public static final String TYPENAME = "run-summary";

  // MolecularAnalysisRun uuid
  @JsonApiId
  private UUID id;

  private String name;

  @Singular
  private List<RunSummaryItem> items;

  public record RunSummaryItem(UUID uuid, MolecularAnalysisResultSummary result, GenericMolecularAnalysisItemSummary genericMolecularAnalysisItemSummary) {
  }

  public record MolecularAnalysisResultSummary(UUID uuid, String type) {
  }


  public record GenericMolecularAnalysisItemSummary(UUID uuid, String name, GenericMolecularAnalysisSummary genericMolecularAnalysisSummary) {
  }

  public record GenericMolecularAnalysisSummary(UUID uuid, String name, String analysisType) {
  }

}
