package ca.gc.aafc.seqdb.api;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This base class for http based will start a test web server on a random port (available in
 * testPost variable). Sample accounts are also inserted and committed into the test database to
 * make sure tests can connect using them.
 *
 */
@SpringBootTest(classes = SeqdbApiLauncher.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseHttpIntegrationTest extends BaseIntegrationTest {

  private static final AtomicBoolean initialized = new AtomicBoolean(false);

  @LocalServerPort
  protected int testPort;

}
