package ca.gc.aafc.seqdb.api.openapi;

import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;

import java.net.URL;

public class OpenAPIConstants {

  private static final String SCHEME = "https";
  static final String SPEC_HOST = "raw.githubusercontent.com";
  static final String SPEC_PATH = "DINA-Web/sequence-specs/master/schema/sequence.yml";

  public static final URL SEQDB_API_SPECS_URL = buildOpenAPISpecsURL();

  private OpenAPIConstants() {
  }

  @SneakyThrows
  private static URL buildOpenAPISpecsURL() {
    URIBuilder URI_BUILDER = new URIBuilder();
    URI_BUILDER.setScheme(SCHEME);
    URI_BUILDER.setHost(SPEC_HOST);
    URI_BUILDER.setPath(SPEC_PATH);
    return URI_BUILDER.build().toURL();
  }

}
