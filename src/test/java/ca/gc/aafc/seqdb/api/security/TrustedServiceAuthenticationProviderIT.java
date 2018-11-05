package ca.gc.aafc.seqdb.api.security;

import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.entities.Account;

@RunWith(Enclosed.class)
public class TrustedServiceAuthenticationProviderIT {

  /**
   * Run tests with TrustedServiceAuthenticationProvider enabled by providing the launch property.
   */
  @TestPropertySource(properties = "seqdb.trusted-service-api-keys=testApiKey")
  public static class TrustedServiceApiKeysEnabled extends BaseIntegrationTest {
    
    @Inject
    private TrustedServiceAuthenticationProvider authProvider;
    
    @Inject
    private EntityManager entityManager;
    
    private Account testAccount;
    
    @Before
    public void initTestAccount() {
      testAccount = new Account();
      testAccount.setAccountName("trustedServiceUser");
      testAccount.setAccountPw("unknownPassword");
      testAccount.setAccountType("User");
      entityManager.persist(testAccount);
    }
    
    @After
    public void cleanUpRequestContextHolder() {
      RequestContextHolder.resetRequestAttributes();
    }
    
    @Test
    public void authenticate_whenApiKeyIsNull_returnNull() {
      MockHttpServletRequest request = new MockHttpServletRequest();
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
      
      Authentication authentication = new TestingAuthenticationToken(
          testAccount.getAccountName(),
          null // No password needed
      );
      
      assertNull(authProvider.authenticate(authentication));
    }
    
    @Test
    public void authenticate_whenApiKeyIsWrong_throwAuthenticationServiceException() {
      MockHttpServletRequest request = new MockHttpServletRequest();
      request.addHeader(TrustedServiceAuthenticationProvider.API_KEY_HEADER, "wrongKey");
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
      
      Authentication authentication = new TestingAuthenticationToken(
          testAccount.getAccountName(),
          null // No password needed
      );
      
      try {
        authProvider.authenticate(authentication);
      } catch(AuthenticationServiceException e) {
        assertEquals("Unknown service api key: wrongKey", e.getMessage());
        return;
      }
      
      fail("No AuthenticationServiceException thrown");
    }
    
    @Test
    public void authenticate_whenApiKeyIsCorrect_returnAuthentication() {
      MockHttpServletRequest request = new MockHttpServletRequest();
      request.addHeader(TrustedServiceAuthenticationProvider.API_KEY_HEADER, "testApiKey");
      RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
      
      Authentication authentication = new TestingAuthenticationToken(
          testAccount.getAccountName(),
          null // No password needed
      );
      
      Authentication trustedServiceAuthentication = authProvider.authenticate(authentication);
      assertEquals(testAccount.getAccountName(), trustedServiceAuthentication.getName());
      assertEquals("trusted-service-auth", trustedServiceAuthentication.getCredentials());
      assertTrue(trustedServiceAuthentication instanceof UsernamePasswordAuthenticationToken);
    }
    
    @Test
    public void supports_whenClassIsAuthentication_returnTrue() {
      assertTrue(authProvider.supports(Authentication.class));
      assertTrue(authProvider.supports(UsernamePasswordAuthenticationToken.class));
      assertTrue(authProvider.supports(TestingAuthenticationToken.class));
    }
    
    @Test
    public void supports_whenClassIsNotAuthentication_returnFalse() {
      assertFalse(authProvider.supports(Account.class));
      assertFalse(authProvider.supports(Integer.class));
      assertFalse(authProvider.supports(String.class));
    }
    
    @Test
    public void supports_whenClassIsNull_returnFalse() {
      assertFalse(authProvider.supports(null));
    }
    
  }
  
  /**
   * Run tests with TrustedServiceAuthenticationProvider disabled by not providing the launch
   * property.
   */
  public static class TrustedServiceApiKeysDisabled extends BaseIntegrationTest {
    
    @Inject
    private Optional<TrustedServiceAuthenticationProvider> authProvider;
    
    @Test
    public void startApp_whenTrustedServiceApiKeysDisabled_trustedServiceAuthenticationBeanNotInjected() {
      assertFalse(authProvider.isPresent());
    }
    
  }
  
}
