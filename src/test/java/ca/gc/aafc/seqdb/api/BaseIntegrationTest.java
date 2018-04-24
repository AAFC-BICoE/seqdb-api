package ca.gc.aafc.seqdb.api;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SeqdbApiLauncher.class)
@Transactional
public abstract class BaseIntegrationTest extends TestCase {

}
