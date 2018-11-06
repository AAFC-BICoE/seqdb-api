package ca.gc.aafc.seqdb.api.security.trustedservice;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.api.security.trustedservice.TrustedServiceAuthenticationProvider;
import ca.gc.aafc.seqdb.api.security.trustedservice.TrustedServiceAuthenticationToken;
import ca.gc.aafc.seqdb.entities.Account;

@TestPropertySource(
    properties = "seqdb.trusted-service-api-keys = test-api-key, another-test-api-key"
)
public class TrustedServiceAuthenticationProviderIT extends BaseIntegrationTest {

  @Value("${seqdb.trusted-service-api-keys}")
  private String[] trustedServiceApiKeys;
  
  @Inject
  private UserDetailsService userDetailsService;
  
  private TrustedServiceAuthenticationProvider authProvider;
  
  private Account testAccount;
  
  /**
   * Add a test account.
   */
  @Before
  public void init() {
    authProvider = new TrustedServiceAuthenticationProvider(
        trustedServiceApiKeys,
        userDetailsService
    );
    
    testAccount = new Account();
    testAccount.setAccountName("trustedServiceUser");
    testAccount.setAccountPw("unknownPassword");
    testAccount.setAccountType("User");
    entityManager.persist(testAccount);
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
        "test-api-key"
    );
    
    Authentication authResult = authProvider.authenticate(authentication);
    
    assertTrue(authResult.isAuthenticated());
    assertEquals(testAccount.getAccountName(), authResult.getName());
    assertEquals("test-api-key", authResult.getCredentials().toString());
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
  
  @Test(expected = UsernameNotFoundException.class)
  public void authenticate_whenApiKeyIsCorrectAndUserDoesNotExist_throwUsernameNotFoundException() {
    Authentication authentication = new TrustedServiceAuthenticationToken(
        "thisUserDoesntExist",
        "test-api-key"
    );
    
    authProvider.authenticate(authentication);
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
