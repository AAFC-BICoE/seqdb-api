package ca.gc.aafc.seqdb.api.entities;

import ca.gc.aafc.dina.entity.StorageGridLayout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
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
  private StorageGridLayout layout;

}
