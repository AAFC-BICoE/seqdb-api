package ca.gc.aafc.seqdb.api.security.trustedservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class TrustedServiceAuthenticationTokenTest {

  @Test
  public void setAuthenticated_whenTrue_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> {
      new TrustedServiceAuthenticationToken("principal", "credentials").setAuthenticated(true);
    });
  }
  
  @Test
  public void setAuthenticated_whenFalse_authenticationIsFalse() {
    Authentication token = new TrustedServiceAuthenticationToken("principal", "credentials");
    token.setAuthenticated(false);
    assertEquals(false, token.isAuthenticated());
  }
  
}
