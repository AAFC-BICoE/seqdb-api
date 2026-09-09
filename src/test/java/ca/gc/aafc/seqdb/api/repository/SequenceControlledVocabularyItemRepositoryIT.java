package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.dina.testsupport.security.WithMockKeycloakUser;
import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.seqdb.api.config.SequenceVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.dto.SequenceControlledVocabularyDto;
import ca.gc.aafc.seqdb.api.dto.SequenceControlledVocabularyItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SequenceControlledVocabularyItemTestFixture;
import jakarta.inject.Inject;

public class SequenceControlledVocabularyItemRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private SequenceControlledVocabularyItemRepository repo;

  @Test
  @WithMockKeycloakUser(groupRole = "dina-group:SUPER_USER")
  void create_recordCreated() throws ResourceGoneException, ResourceNotFoundException {
    String expectedName = "dina attribute #12";
    String expectedValue = "dina value";
    String expectedCreatedBy = "dina";
    String expectedGroup = "dina-group";

    SequenceControlledVocabularyItemDto dto = SequenceControlledVocabularyItemTestFixture.newSequenceControlledVocabularyItemDto();
    dto.setName(expectedName);
    dto.setVocabularyElementType(TypedVocabularyElement.VocabularyElementType.INTEGER);
    dto.setAcceptedValues(new String[]{expectedValue});
    dto.setDinaComponent(SequenceVocabularyConfiguration.DinaComponent.GENERIC_MOLECULAR_ANALYSIS.name());
    dto.setCreatedBy(expectedCreatedBy);
    dto.setGroup(expectedGroup);

    JsonApiDocument toCreate = JsonApiDocuments.createJsonApiDocumentWithRelToOne(
      null, dto.getJsonApiType(),
      JsonAPITestHelper.toAttributeMap(dto),
      Map.of("controlledVocabulary",  JsonApiDocument.ResourceIdentifier.builder()
      .id(SequenceVocabularyConfiguration.MANAGED_ATTRIBUTE_VOCAB_UUID)
      .type(SequenceControlledVocabularyDto.TYPENAME).build())
    );

    UUID uuid = createWithRepository(toCreate, repo::onCreate);
    SequenceControlledVocabularyItemDto result = repo.getOne(uuid, "").getDto();

    assertEquals(uuid, result.getUuid());
    assertEquals(expectedName, result.getName());
    assertEquals("dina_attribute_12_integer", result.getKey());
    assertEquals(expectedValue, result.getAcceptedValues()[0]);
    assertNotNull(result.getCreatedBy());
    assertEquals(expectedGroup, result.getGroup());
    assertEquals(TypedVocabularyElement.VocabularyElementType.INTEGER, result.getVocabularyElementType());
    assertEquals(
      SequenceVocabularyConfiguration.DinaComponent.GENERIC_MOLECULAR_ANALYSIS.name(),
      result.getDinaComponent());
  }

  @Test
  @WithMockKeycloakUser(groupRole = SequenceControlledVocabularyItemTestFixture.GROUP + ":SUPER_USER")
  void findOneByKey_whenKeyProvided_managedAttributeFetched()
      throws ResourceGoneException, ResourceNotFoundException {
    SequenceControlledVocabularyItemDto dto = SequenceControlledVocabularyItemTestFixture.newSequenceControlledVocabularyItemDto();
    dto.setName("Attribute 1");
    dto.setVocabularyElementType(TypedVocabularyElement.VocabularyElementType.INTEGER);
    dto.setDinaComponent(SequenceVocabularyConfiguration.DinaComponent.GENERIC_MOLECULAR_ANALYSIS.name());

    JsonApiDocument toCreate = JsonApiDocuments.createJsonApiDocumentWithRelToOne(
      null, dto.getJsonApiType(),
      JsonAPITestHelper.toAttributeMap(dto),
      Map.of("controlledVocabulary",  JsonApiDocument.ResourceIdentifier.builder()
      .id(SequenceVocabularyConfiguration.MANAGED_ATTRIBUTE_VOCAB_UUID)
      .type(SequenceControlledVocabularyDto.TYPENAME).build())
    );

    UUID newAttributeUuid = createWithRepository(toCreate, repo::onCreate);

    var response = repo.onFindOne("managed_attribute.attribute_1_integer.generic_molecular_analysis", null);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
