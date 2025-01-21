package ca.gc.aafc.seqdb.api.dto;

import java.util.List;
import java.util.UUID;

import com.toedter.spring.hateoas.jsonapi.JsonApiId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@AllArgsConstructor
@Getter
@Builder
public class RunSummaryDto {

  public static final String TYPENAME = "run-summary";

  @JsonApiId
  private String id;

  @Singular
  private List<MolecularAnalysisRunSummary> molecularAnalysisRunSummaries;

  public record MolecularAnalysisRunSummary(UUID uuid, String name, List<MolecularAnalysisRunSummaryItem> items) {
  }

  public record MolecularAnalysisRunSummaryItem(UUID uuid) {
  }


}
