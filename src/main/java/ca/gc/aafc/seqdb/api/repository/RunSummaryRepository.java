package ca.gc.aafc.seqdb.api.repository;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;

import ca.gc.aafc.seqdb.api.dto.RunSummaryDto;
import ca.gc.aafc.seqdb.api.service.RunSummaryService;

import static com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder.jsonApiModel;
import static com.toedter.spring.hateoas.jsonapi.MediaTypes.JSON_API_VALUE;

@RestController
@RequestMapping(value = "/api", produces = JSON_API_VALUE)
public class RunSummaryRepository {

  private final RunSummaryService runSummaryService;

  public RunSummaryRepository(RunSummaryService runSummaryService) {
    this.runSummaryService = runSummaryService;
  }

  @GetMapping(RunSummaryDto.TYPENAME + "/{id}")
  public ResponseEntity<RepresentationModel<?>> handleFindOne(@PathVariable String id) {

   // VocabularyDto dto = findOne(id);
//    if (dto == null) {
//      return ResponseEntity.notFound().build();
//    }

    RunSummaryDto dto = runSummaryService.findSummary(id);

    JsonApiModelBuilder builder = jsonApiModel().model(RepresentationModel.of(dto));

    return ResponseEntity.ok(builder.build());
  }

}
