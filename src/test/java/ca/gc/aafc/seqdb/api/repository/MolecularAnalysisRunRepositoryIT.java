package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

public class MolecularAnalysisRunRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private MolecularAnalysisRunRepository molecularAnalysisRunRepository;

  @Test
  public void findOne_withExternalInclude_returnsAttachments()
      throws ResourceGoneException, ResourceNotFoundException {

    MolecularAnalysisRunDto runDto = MolecularAnalysisRunTestFixture.newMolecularAnalysisRun();

    Map<String, Object> attrs = JsonAPITestHelper.toAttributeMap(runDto);

    // Add two attachments to verify include works for multiple values
    List<UUID> attachments = List.of(
      UUID.fromString("727a1f51-0556-4d55-a54b-b61388565c2a"),
      UUID.fromString("5f47e3b8-2bfa-4e67-9a1b-2f4d9f3c8e6a")
    );
    attrs.put("attachments", attachments);

    JsonApiDocument toCreate = JsonApiDocuments.createJsonApiDocument(null, MolecularAnalysisRunDto.TYPENAME, attrs);

    UUID runUuid = createWithRepository(toCreate, molecularAnalysisRunRepository::onCreate);

    var response = molecularAnalysisRunRepository.getOne(runUuid, "include=attachments");
    MolecularAnalysisRunDto found = response.getDto();

    assertNotNull(found.getUuid());
    assertNotNull(found.getAttachments(), "attachments should be included");
    assertEquals(2, found.getAttachments().size(), "should have two attachments included");
  }
}
