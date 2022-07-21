package ca.gc.aafc.seqdb.api.testsupport.factories;

import ca.gc.aafc.dina.entity.StorageGridLayout;
import ca.gc.aafc.seqdb.api.entities.StorageRestriction;

public class StorageRestrictionFactory {

  public static StorageRestriction.StorageRestrictionBuilder newStorageRestriction() {
    return StorageRestriction.builder()
            .layout(StorageGridLayout.builder()
                    .fillDirection(StorageGridLayout.FillDirection.BY_ROW)
                    .numberOfRows(8)
                    .numberOfColumns(12)
                    .build());
  }
}
