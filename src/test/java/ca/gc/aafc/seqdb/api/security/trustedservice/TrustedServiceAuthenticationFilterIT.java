package ca.gc.aafc.seqdb.api.security.trustedservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.entities.Account;

@TestPropertySource(
    properties = "seqdb.trusted-service-api-keys = test-api-key, another-test-api-key"
)
public class TrustedServiceAuthenticationFilterIT extends BaseIntegrationTest {
  
  @Inject
  protected AuthenticationManager authManager;
  
  protected TrustedServiceAuthenticationFilter filter;
  
  protected Account testAccount;
  
  @BeforeEach
  public void init() {
    // Setup test account.
    testAccount = new Account();
    testAccount.setAccountName("trustedServiceUser");
    testAccount.setAccountPw("unknownPassword");
    testAccount.setAccountType("User");
    entityManager.persist(testAccount);
    
    // Setup test filter.
    filter = new TrustedServiceAuthenticationFilter(authManager);
  }
  
  @AfterEach
  public void cleanUp() {
    // Remove any authentication in the context.
    SecurityContextHolder.getContext().setAuthentication(null);
  }
  
  @Test
  public void filter_whenAuthorizationHeaderIsNull_doNothing()
      throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    
    filter.doFilter(request, response, filterChain);
    
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }
  
  @Test
  public void filter_whenAuthorizationHeaderIsUsingBasicAuthentication_doNothing()
      throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    
    request.addHeader("Authorization", "Basic 87aeb6trf");
    
    filter.doFilter(request, response, filterChain);
    
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }
  
  @Test
  public void filter_whenAuthorizationHeaderIsWrongFormat_throwBadCredentialsException()
      throws ServletException, IOException {
    assertThrows(BadCredentialsException.class, () -> {
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockHttpServletResponse response = new MockHttpServletResponse();
      MockFilterChain filterChain = new MockFilterChain();
      request.addHeader("Authorization", "TrustedService username and api-key");
      filter.doFilter(request, response, filterChain);
    });
  }
  
  @Test
  public void filter_whenAuthorizationHeaderIsCorrect_userIsAuthenticated()
      throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    
    request.addHeader("Authorization", "TrustedService trustedServiceUser test-api-key");
    
    filter.doFilter(request, response, filterChain);
    
    TrustedServiceAuthenticationToken authentication = (TrustedServiceAuthenticationToken) SecurityContextHolder
        .getContext().getAuthentication();
    
    assertEquals("trustedServiceUser", authentication.getName());
    assertEquals("test-api-key", authentication.getCredentials().toString());
    assertTrue(authentication.isAuthenticated());
  }
  
  @Test
  public void filter_whenAuthorizationHeaderIsCorrectAndUsernameHasSpaces_userIsAuthenticated()
      throws ServletException, IOException {
    Account testAccountWithSpaces = new Account();
    testAccountWithSpaces.setAccountName("Mat Poff");
    testAccountWithSpaces.setAccountPw("unknownPassword");
    testAccountWithSpaces.setAccountType("User");
    entityManager.persist(testAccountWithSpaces);
    
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    
    request.addHeader("Authorization", "TrustedService Mat%20Poff test-api-key");
    
    filter.doFilter(request, response, filterChain);
    
    TrustedServiceAuthenticationToken authentication = (TrustedServiceAuthenticationToken) SecurityContextHolder
        .getContext().getAuthentication();
    
    assertEquals("Mat Poff", authentication.getName());
    assertEquals("test-api-key", authentication.getCredentials().toString());
    assertTrue(authentication.isAuthenticated());
  }
    
}
