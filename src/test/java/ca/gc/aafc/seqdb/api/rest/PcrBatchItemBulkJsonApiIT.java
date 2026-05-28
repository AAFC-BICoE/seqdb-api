package ca.gc.aafc.seqdb.api.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.http.HttpStatus;

import io.restassured.response.Response;

@Testable
public class PcrBatchItemBulkJsonApiIT extends BaseJsonApiIntegrationTest {

  @Override
  protected String getResourceUnderTest() {
    return "pcr-batch-item";
  }

  @Override
  protected Map<String, Object> buildCreateAttributeMap() {
    return Map.of("group", "dina", "createdBy", "test user");
  }

  @Override
  protected Map<String, Object> buildUpdateAttributeMap() {
    return Map.of("result", "updated");
  }

  @Test
  public void bulkCreateUpdateBulkLoad_HttpOkReturned() {
    Map<String, Object> data1 = Map.of("type", getResourceUnderTest(), "attributes", buildCreateAttributeMap());
    Map<String, Object> data2 = Map.of("type", getResourceUnderTest(), "attributes", buildCreateAttributeMap());

    Map<String, Object> bulkCreate = Map.of("data", List.of(data1, data2));

    Response createResponse = given()
      .contentType("application/vnd.api+json; ext=bulk")
      .body(bulkCreate)
      .when()
      .post(getResourceUnderTest() + "/bulk");

    createResponse.then().statusCode(HttpStatus.OK.value());
    List<String> ids = createResponse.body().jsonPath().getList("data.id");
    assertEquals(2, ids.size());

    String id1 = ids.get(0);
    String id2 = ids.get(1);

    Map<String, Object> upd1 = Map.of(
      "type", getResourceUnderTest(),
      "id", id1,
      "attributes", Map.of("result", "Good Band")
    );

    Map<String, Object> upd2 = Map.of(
      "type", getResourceUnderTest(),
      "id", id2,
      "attributes", Map.of("result", "No Band")
    );

    Map<String, Object> bulkUpdate = Map.of("data", List.of(upd1, upd2));

    Response updateResponse = given()
      .contentType("application/vnd.api+json; ext=bulk")
      .body(bulkUpdate)
      .when()
      .patch(getResourceUnderTest() + "/bulk");

    updateResponse.then().statusCode(HttpStatus.OK.value());

    updateResponse.then().body("data[0].attributes.result", equalTo("Good Band"));
    updateResponse.then().body("data[1].attributes.result", equalTo("No Band"));

    sendGet(id1).body("data.attributes.result", equalTo("Good Band"));
    sendGet(id2).body("data.attributes.result", equalTo("No Band"));
  }

}
