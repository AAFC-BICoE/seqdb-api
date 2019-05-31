package ca.gc.aafc.seqdb.api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
    classes = SeqdbApiLauncher.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = "server.port=8080"
)
@TestPropertySource(properties="import-sample-accounts=true")
public abstract class BaseHttpIntegrationTest extends BaseIntegrationTest { }
