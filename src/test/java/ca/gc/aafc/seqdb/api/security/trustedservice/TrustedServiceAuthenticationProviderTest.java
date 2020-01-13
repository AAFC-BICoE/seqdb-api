package ca.gc.aafc.seqdb.api.security.trustedservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ca.gc.aafc.seqdb.api.security.trustedservice.TrustedServiceAuthenticationProvider;
import ca.gc.aafc.seqdb.api.security.trustedservice.TrustedServiceAuthenticationToken;
import ca.gc.aafc.seqdb.entities.Account;

/**
 * Unit tests related to {@link TrustedServiceAuthenticationProvider}
 *
 */
public class TrustedServiceAuthenticationProviderTest {

  private static final String API_KEY_1 = "test-api-key";
  private static final String[] TRUSTED_SERVICE_API_KEYS = {API_KEY_1, "another-test-api-key"};
  
  // userDetailsService stub for testing purpose
  private UserDetailsService userDetailsService = (u) -> {
    if ("trustedServiceUser".equals(u)) {
      return new org.springframework.security.core.userdetails.User(u, "", 
          Arrays.asList(() -> "User"));
    }
    throw new UsernameNotFoundException("User does not exist in database: " + u);
  };
  
  private TrustedServiceAuthenticationProvider authProvider;
  
  private Account testAccount;
  
  @BeforeEach
  public void init() {
    authProvider = new TrustedServiceAuthenticationProvider(
        TRUSTED_SERVICE_API_KEYS,
        userDetailsService
    );
    
    // change to test data provider when available for Account
    testAccount = new Account();
    testAccount.setAccountName("trustedServiceUser");
    testAccount.setAccountPw("unknownPassword");
    testAccount.setAccountType("User");
  }
  
  @Test
  public void authenticate_whenApiKeyIsNull_returnNull() {
    Authentication authentication = new TrustedServiceAuthenticationToken(
        testAccount.getAccountName(),
        null
    );
    
    assertNull(authProvider.authenticate(authentication));
  }
  
  @Test
  public void authenticate_whenApiKeyIsCorrect_returnAuthenticatedToken() {
    Authentication authentication = new TrustedServiceAuthenticationToken(
        testAccount.getAccountName(),
        API_KEY_1
    );
    
    Authentication authResult = authProvider.authenticate(authentication);
    
    assertTrue(authResult.isAuthenticated());
    assertEquals(testAccount.getAccountName(), authResult.getName());
    assertEquals(API_KEY_1, authResult.getCredentials().toString());
  }
  
  @Test
  public void authenticate_whenApiKeyIsWrong_throwAuthenticationServiceException() {
    Authentication authentication = new TrustedServiceAuthenticationToken(
        testAccount.getAccountName(),
        "wrong-key"
    );
    
    try {
      authProvider.authenticate(authentication);
    } catch(AuthenticationServiceException e) {
      assertEquals("Unknown service api key: wrong-key", e.getMessage());
      return;
    }
    
    fail("No AuthenticationServiceException thrown");
  }
  
  @Test
  public void authenticate_whenApiKeyIsCorrectAndUserDoesNotExist_throwUsernameNotFoundException() {
    assertThrows(UsernameNotFoundException.class, () -> {
      Authentication authentication = new TrustedServiceAuthenticationToken("thisUserDoesntExist", API_KEY_1);
      authProvider.authenticate(authentication);
    });
  }
  
  @Test
  public void supports_whenClassIsTrustedServiceAuthenticationToken_returnTrue() {
    assertTrue(authProvider.supports(TrustedServiceAuthenticationToken.class));
  }
  
  @Test
  public void supports_whenClassIsNotTrustedServiceAuthenticationToken_returnFalse() {
    assertFalse(authProvider.supports(Authentication.class));
    assertFalse(authProvider.supports(UsernamePasswordAuthenticationToken.class));
    assertFalse(authProvider.supports(Account.class));
    assertFalse(authProvider.supports(Integer.class));
    assertFalse(authProvider.supports(String.class));
  }
  
  @Test
  public void supports_whenClassIsNull_returnFalse() {
    assertFalse(authProvider.supports(null));
  }
  
}
