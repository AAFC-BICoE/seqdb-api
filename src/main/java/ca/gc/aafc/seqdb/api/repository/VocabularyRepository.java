package ca.gc.aafc.seqdb.api.repository;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;

import ca.gc.aafc.dina.repository.ReadOnlyDinaRepositoryV2;
import ca.gc.aafc.seqdb.api.dto.VocabularyDto;
import ca.gc.aafc.seqdb.api.service.VocabularyService;

import static com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder.jsonApiModel;
import static com.toedter.spring.hateoas.jsonapi.MediaTypes.JSON_API_VALUE;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api", produces = JSON_API_VALUE)
public class VocabularyRepository extends ReadOnlyDinaRepositoryV2<String, VocabularyDto> {

  protected VocabularyRepository(VocabularyService vocabularyService) {
    super(vocabularyService);
  }

  @GetMapping("vocabulary/{id}")
  public ResponseEntity<RepresentationModel<?>> handleFindOne(@PathVariable String id) {

    VocabularyDto dto = findOne(id);

    if (dto == null) {
      return ResponseEntity.notFound().build();
    }

    JsonApiModelBuilder builder = jsonApiModel().model(RepresentationModel.of(dto));

    return ResponseEntity.ok(builder.build());
  }

  @GetMapping("vocabulary")
  public ResponseEntity<RepresentationModel<?>> handleFindAll(HttpServletRequest req) {

    String queryString = URLDecoder.decode(req.getQueryString(), StandardCharsets.UTF_8);
    List<VocabularyDto> dtos ;
    try {
      dtos = findAll(queryString);
    } catch (IllegalArgumentException iaEx) {
      return ResponseEntity.badRequest().build();
    }

    JsonApiModelBuilder builder = jsonApiModel().model(CollectionModel.of(dtos));

    return ResponseEntity.ok(builder.build());
  }

}
