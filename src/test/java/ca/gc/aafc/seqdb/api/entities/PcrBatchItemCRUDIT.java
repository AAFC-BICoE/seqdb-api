package ca.gc.aafc.seqdb.api.entities;

import javax.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;
import ca.gc.aafc.seqdb.api.testsupport.factories.PcrBatchItemFactory;

class PcrBatchItemCRUDIT extends SequenceModuleBaseIT {

  @Test
  void create() {
    PcrBatchItem pcrBatchItem = PcrBatchItemFactory.newPcrBatchItem().build();
    pcrBatchItemService.create(pcrBatchItem);
    Assertions.assertNotNull(pcrBatchItem.getUuid());

    PcrBatchItem result =  pcrBatchItemService.findOne(pcrBatchItem.getUuid(), PcrBatchItem.class);
    Assertions.assertNotNull(result.getCreatedOn());
    Assertions.assertNotNull(result.getId());
    Assertions.assertEquals(pcrBatchItem.getGroup(), result.getGroup());
    Assertions.assertEquals(pcrBatchItem.getWellColumn(), result.getWellColumn());
    Assertions.assertEquals(pcrBatchItem.getWellRow(), result.getWellRow());
    Assertions.assertEquals(pcrBatchItem.getCreatedBy(), result.getCreatedBy());
  }

  @Test
  void validate_WhenInvalidWellColumnSizeExceptionThrown() {
    PcrBatchItem pcrBatchItem = PcrBatchItemFactory.newPcrBatchItem().build();

    pcrBatchItemService.create(pcrBatchItem);
    pcrBatchItem.setWellColumn(256);

    Assertions.assertThrows(ValidationException.class, () ->  pcrBatchItemService.update(pcrBatchItem));
  }

  @Test
  void validate_WhenInvalidWellRowSizeExceptionThrown() {
    PcrBatchItem pcrBatchItem = PcrBatchItemFactory.newPcrBatchItem().build();

    pcrBatchItemService.create(pcrBatchItem);

    pcrBatchItem.setWellRow("BBB");

    Assertions.assertThrows(ValidationException.class, () ->  pcrBatchItemService.update(pcrBatchItem));
  }


  
}
