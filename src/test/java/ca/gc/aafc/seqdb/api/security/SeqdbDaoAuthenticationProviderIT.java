package ca.gc.aafc.seqdb.api.security;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.entities.Account;

public class SeqdbDaoAuthenticationProviderIT extends BaseIntegrationTest {

  @Inject
  private SeqdbDaoAuthenticationProvider authenticationProvider;
  
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  
  @Test
  public void localAuth_whenCorrectCredentialsAreGiven_returnsAuthentication() {
    Account testAccount = new Account();
    testAccount.setAccountName("Mat");
    testAccount.setAccountType("User");
    testAccount.setAccountPw(passwordEncoder.encode("mypassword"));
    entityManager.persist(testAccount);
    
    // This lookup should be case-insensitive
    Authentication authToken = new UsernamePasswordAuthenticationToken("mat", "mypassword");
    assertTrue(authenticationProvider.authenticate(authToken) instanceof Authentication);
  }
  
  @Test
  public void ldapAuth_whenCorrectCredentialsAreGivenWithLdapDn_returnsNull() {
    Account testAccount = new Account();
    testAccount.setAccountName("Mat");
    testAccount.setAccountType("User");
    testAccount.setAccountPw(passwordEncoder.encode("mypassword"));
    testAccount.setLdapDn("testLdapDn");
    entityManager.persist(testAccount);
    
    // This lookup should be case-insensitive
    Authentication authToken = new UsernamePasswordAuthenticationToken("mat", "mypassword");
    assertNull(authenticationProvider.authenticate(authToken));
  }
  
  @Test(expected = BadCredentialsException.class)
  public void localAuth_whenIncorrectPasswordGiven_BadCredentialsException() {
    Authentication authToken = new UsernamePasswordAuthenticationToken("Mat", "mypassword");
    authenticationProvider.authenticate(authToken);
  }
  
}
