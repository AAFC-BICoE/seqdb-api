package ca.gc.aafc.seqdb.api.service;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;
import ca.gc.aafc.seqdb.api.testsupport.factories.SequenceManagedAttributeFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SequenceManagedAttributeServiceIT extends SequenceModuleBaseIT {

  private static final String GROUP = "grp";

  @Test
  void delete_WhenNotInUse_DeleteAccepted() {
    SequenceManagedAttribute attribute = newAttribute(SequenceManagedAttribute.ManagedAttributeComponent.GENERIC_MOLECULAR_ANALYSIS);
    managedAttributeService.create(attribute);

    assertNotNull(
      managedAttributeService.findOne(attribute.getUuid(), SequenceManagedAttribute.class));

    // To enable when usage is implemented
    managedAttributeService.delete(attribute);

    assertNull(
      managedAttributeService.findOne(attribute.getUuid(), SequenceManagedAttribute.class));
  }

  private static SequenceManagedAttribute newAttribute(SequenceManagedAttribute.ManagedAttributeComponent component) {
    return SequenceManagedAttributeFactory.newManagedAttribute()
      .createdBy("SequenceManagedAttributeServiceIT")
      .managedAttributeComponent(component)
      .group(GROUP)
      .acceptedValues(null)
      .build();
  }
}
