package ca.gc.aafc.seqdb.api.testsupport.factories;

import ca.gc.aafc.dina.entity.StorageGridLayout;
import ca.gc.aafc.seqdb.api.entities.StorageRestriction;

public class StorageRestrictionFactory {

  public static final int DEFAULT_NUM_OF_ROWS = 8;
  public static final int DEFAULT_NUM_OF_COLUMNS = 12;

  public static StorageRestriction.StorageRestrictionBuilder newStorageRestriction() {
    return StorageRestriction.builder()
            .layout(StorageGridLayout.builder()
                    .fillDirection(StorageGridLayout.FillDirection.BY_ROW)
                    .numberOfRows(DEFAULT_NUM_OF_ROWS)
                    .numberOfColumns(DEFAULT_NUM_OF_COLUMNS)
                    .build());
  }
}
