package ca.gc.aafc.seqdb.api;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    classes = SeqdbApiLauncher.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = "server.port=8080"
)
public class AppStartsIT extends BaseIntegrationTest {

  /**
   * Tests that the application with embedded Tomcat starts up successfully.
   */
  @Test
  public void startApp_OnStartUp_NoErrorsThrown() {
  }

}
