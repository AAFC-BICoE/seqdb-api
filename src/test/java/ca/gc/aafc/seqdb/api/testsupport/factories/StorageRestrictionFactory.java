package ca.gc.aafc.seqdb.api.testsupport.factories;

import ca.gc.aafc.seqdb.api.entities.StorageRestriction;

public class StorageRestrictionFactory {

  public static StorageRestriction.StorageRestrictionBuilder newStorageRestriction() {
    return StorageRestriction.builder()
            .layout(StorageRestriction.Layout.builder()
                    .fillDirection(StorageRestriction.Layout.FillDirection.BY_ROW)
                    .numberOfRows(8)
                    .numberOfColumns(12)
                    .build());
  }
}
