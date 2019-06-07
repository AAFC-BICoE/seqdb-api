package ca.gc.aafc.seqdb.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(
    classes = SeqdbApiLauncher.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class BaseHttpIntegrationTest extends BaseIntegrationTest { 
  @LocalServerPort
  protected int testPort;
}
