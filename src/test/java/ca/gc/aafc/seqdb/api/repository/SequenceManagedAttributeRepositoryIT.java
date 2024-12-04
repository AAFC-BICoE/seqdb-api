package ca.gc.aafc.seqdb.api.repository;

import io.crnk.core.queryspec.QuerySpec;
import java.util.UUID;
import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.testsupport.security.WithMockKeycloakUser;
import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.seqdb.api.dto.SequenceManagedAttributeDto;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;

import ca.gc.aafc.seqdb.api.testsupport.fixtures.SequenceManagedAttributeTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SequenceManagedAttributeRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private SequenceManagedAttributeRepository repo;

  @Test
  @WithMockKeycloakUser(groupRole = "dina-group:SUPER_USER")
  void create_recordCreated() {
    String expectedName = "dina attribute #12";
    String expectedValue = "dina value";
    String expectedCreatedBy = "dina";
    String expectedGroup = "dina-group";

    SequenceManagedAttributeDto dto = SequenceManagedAttributeTestFixture.newManagedAttribute();
    dto.setName(expectedName);
    dto.setVocabularyElementType(TypedVocabularyElement.VocabularyElementType.INTEGER);
    dto.setAcceptedValues(new String[]{expectedValue});
    dto.setManagedAttributeComponent(SequenceManagedAttribute.ManagedAttributeComponent.GENERIC_MOLECULAR_ANALYSIS);
    dto.setCreatedBy(expectedCreatedBy);
    dto.setGroup(expectedGroup);

    UUID uuid = repo.create(dto).getUuid();
    SequenceManagedAttributeDto result = repo.findOne(uuid, new QuerySpec(
      SequenceManagedAttributeDto.class));
    assertEquals(uuid, result.getUuid());
    assertEquals(expectedName, result.getName());
    assertEquals("dina_attribute_12", result.getKey());
    assertEquals(expectedValue, result.getAcceptedValues()[0]);
    assertNotNull(result.getCreatedBy());
    assertEquals(expectedGroup, result.getGroup());
    assertEquals(TypedVocabularyElement.VocabularyElementType.INTEGER, result.getVocabularyElementType());
    assertEquals(
      SequenceManagedAttribute.ManagedAttributeComponent.GENERIC_MOLECULAR_ANALYSIS,
      result.getManagedAttributeComponent());
  }

  @Test
  @WithMockKeycloakUser(groupRole = SequenceManagedAttributeTestFixture.GROUP + ":SUPER_USER")
  void findOneByKey_whenKeyProvided_managedAttributeFetched() {
    SequenceManagedAttributeDto newAttribute = SequenceManagedAttributeTestFixture.newManagedAttribute();
    newAttribute.setName("Attribute 1");
    newAttribute.setVocabularyElementType(TypedVocabularyElement.VocabularyElementType.INTEGER);
    newAttribute.setManagedAttributeComponent(SequenceManagedAttribute.ManagedAttributeComponent.GENERIC_MOLECULAR_ANALYSIS);

    UUID newAttributeUuid = repo.create(newAttribute).getUuid();

    QuerySpec querySpec = new QuerySpec(SequenceManagedAttributeDto.class);
    SequenceManagedAttributeDto fetchedAttribute = repo.findOne("generic_molecular_analysis.attribute_1", querySpec);

    assertEquals(newAttributeUuid, fetchedAttribute.getUuid());
  }
}
