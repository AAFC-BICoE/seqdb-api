package ca.gc.aafc.seqdb.api.security.trustedservice;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.security.core.Authentication;

@RunWith(JUnit4.class)
public class TrustedServiceAuthenticationTokenTest {

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
