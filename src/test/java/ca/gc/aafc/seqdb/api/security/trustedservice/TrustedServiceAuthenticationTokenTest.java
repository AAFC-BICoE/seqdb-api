package ca.gc.aafc.seqdb.api.security.trustedservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.security.core.Authentication;

import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class TrustedServiceAuthenticationTokenTest extends TestCase {

  @Test(expected = IllegalArgumentException.class)
  public void setAuthenticated_whenTrue_throwIllegalArgumentException() {
    new TrustedServiceAuthenticationToken("principal", "credentials").setAuthenticated(true);
  }
  
  @Test
  public void setAuthenticated_whenFalse_authenticationIsFalse() {
    Authentication token = new TrustedServiceAuthenticationToken("principal", "credentials");
    token.setAuthenticated(false);
    assertEquals(false, token.isAuthenticated());
  }
  
}
