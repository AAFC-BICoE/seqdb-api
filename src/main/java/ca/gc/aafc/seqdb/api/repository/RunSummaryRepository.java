package ca.gc.aafc.seqdb.api.repository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;

import ca.gc.aafc.dina.filter.FilterExpression;
import ca.gc.aafc.dina.filter.QueryStringParser;
import ca.gc.aafc.dina.filter.QueryComponent;
import ca.gc.aafc.seqdb.api.dto.RunSummaryDto;
import ca.gc.aafc.seqdb.api.service.RunSummaryService;

import static com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder.jsonApiModel;
import static com.toedter.spring.hateoas.jsonapi.MediaTypes.JSON_API_VALUE;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api", produces = JSON_API_VALUE)
public class RunSummaryRepository {

  private final RunSummaryService runSummaryService;

  public RunSummaryRepository(RunSummaryService runSummaryService) {
    this.runSummaryService = runSummaryService;
  }

  @GetMapping(RunSummaryDto.TYPENAME)
  public ResponseEntity<RepresentationModel<?>> handleFindAll(HttpServletRequest req) {
    String queryString = decodeQueryString(req);
    QueryComponent queryComponents = QueryStringParser.parse(queryString);

    if (queryComponents.getFilterExpression().isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    FilterExpression filterExpression = queryComponents.getFilterExpression().get();
    if (!"".equals(filterExpression.attribute())) {
      return ResponseEntity.badRequest().build();
    }

    List<RunSummaryDto> dtos = runSummaryService.findSummary(filterExpression);
    JsonApiModelBuilder builder = jsonApiModel().model(CollectionModel.of(dtos));

    return ResponseEntity.ok(builder.build());
  }

  protected static String decodeQueryString(HttpServletRequest req) {
    Objects.requireNonNull(req);

    if (StringUtils.isBlank(req.getQueryString())) {
      return "";
    }
    return URLDecoder.decode(req.getQueryString(), StandardCharsets.UTF_8);
  }

}
