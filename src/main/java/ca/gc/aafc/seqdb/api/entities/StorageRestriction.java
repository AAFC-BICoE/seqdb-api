package ca.gc.aafc.seqdb.api.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This should be populated on creation by collection-api, storage-unit-type GridLayoutDefinition.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageRestriction {

  @Valid
  @NotNull
  private Layout layout;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Layout {

    @NotNull
    @Min(1)
    @Max(1000)
    private Integer numberOfRows;

    @NotNull
    @Min(1)
    @Max(1000)
    private Integer numberOfColumns;

    @NotNull
    private FillDirection fillDirection;

    public enum FillDirection {
      BY_ROW("by row"), BY_COLUMN("by column");

      private final String text;

      FillDirection(String text) {
        this.text = text;
      }

      public String getText() {
        return this.text;
      }
    }
  }


}
